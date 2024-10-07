package buildlogic.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.UnknownPluginException

class RequireKoverPlugin : Plugin<Project> {

    private val ignoredProjects = listOf(
        "server",
        "test",
        "entities",
        "models",
    )

    override fun apply(target: Project) {
        target.run {

            val koverPluginId = "org.jetbrains.kotlinx.kover"
            val projectName = this.name

            // Check if project is final
            if (subprojects.isEmpty()) {
                tasks.register("requireKoverPlugin") {
                    if (ignoredProjects.contains(projectName).not()) {
                        println("Checking if ${target.displayName} contains kover")
                        try {
                            plugins.getPlugin(koverPluginId)
                        } catch (e: UnknownPluginException) {
                            throw IllegalStateException("Project ${target.displayName} is missing code coverage plugin")
                        }
                    }
                }
            }
        }
    }
}