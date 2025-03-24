package foundation.env

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val invoicerEnvironmentDiModule = DI.Module("environment") {
    bindProvider<InvoicerEnvironment> {
        InvoicerEnvironmentImpl(
            application = instance()
        )
    }
}