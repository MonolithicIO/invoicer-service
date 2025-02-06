package io.github.alaksion.invoicer.server.app

import connectDatabase
import controller.rootController
import foundation.cache.impl.CacheHandler
import io.github.alaksion.invoicer.server.app.plugins.*
import io.ktor.server.application.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.serializer
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    installDi()
    installAuth()
    connectDatabase()
    configureSerialization()
    installStatusPages()
    configureMonitoring()
    rootController()

    val redisTest by closestDI().instance<CacheHandler>()

    runBlocking {
        redisTest.set("test", "test", serializer = String.serializer())
    }
}
