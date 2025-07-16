package io.github.monolithic.invoicer.server.app.plugins


import io.ktor.http.HttpHeaders
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.request.path
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics
import io.micrometer.core.instrument.binder.system.FileDescriptorMetrics
import io.micrometer.core.instrument.binder.system.ProcessorMetrics
import io.micrometer.core.instrument.binder.system.UptimeMetrics
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import org.slf4j.event.Level

@OptIn(ExperimentalUuidApi::class)
fun Application.configureMonitoring() {
    install(CallLogging) {
        level = Level.DEBUG
        filter { call -> call.request.path().startsWith("/") }
        callIdMdc("call-id")
    }

    install(CallId) {
        generate { Uuid.random().toString() }
        replyToHeader(HttpHeaders.XRequestId)
    }

    installPrometheus()
}

private fun Application.installPrometheus() {
    val prometheusRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

    install(MicrometerMetrics) {
        registry = prometheusRegistry

        meterBinders = listOf(
            ClassLoaderMetrics(),
            JvmMemoryMetrics(),
            JvmGcMetrics(),
            ProcessorMetrics(),
            JvmThreadMetrics(),
            FileDescriptorMetrics(),
            UptimeMetrics()
        )
    }

    routing {
        get("/metrics") {
            call.respond(prometheusRegistry.scrape())
        }
    }
}
