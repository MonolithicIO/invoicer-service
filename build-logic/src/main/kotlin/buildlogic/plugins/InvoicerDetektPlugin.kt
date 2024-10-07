package buildlogic.plugins

import io.gitlab.arturbosch.detekt.DetektPlugin
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure

class InvoicerDetektPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply(DetektPlugin::class)

            configure<DetektExtension> {
                config.setFrom("${target.rootDir.absolutePath}/config/detekt/detekt.yml")
                buildUponDefaultConfig = false
                enableCompilerPlugin.set(true)
            }
        }
    }

}