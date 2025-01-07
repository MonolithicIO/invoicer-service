package foundation.env

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val environmentDiModule = DI.Module("environment") {
    bindProvider<Environment> {
        EnvironmentImpl(
            application = instance()
        )
    }
}