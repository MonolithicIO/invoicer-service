package repository.api.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import repository.api.repository.*

val repositoryModule = DI.Module("invocer-repository") {

    bindProvider<BeneficiaryRepository> {
        BeneficiaryRepositoryImpl(
            dateProvider = instance()
        )
    }

    bindProvider<IntermediaryRepository> {
        IntermediaryRepositoryImpl(
            dateProvider = instance()
        )
    }

    bindProvider<InvoiceRepository> {
        InvoiceRepositoryImpl(
            dateProvider = instance()
        )
    }

    bindProvider<UserRepository> {
        UserRepositoryImpl(
            dateProvider = instance()
        )
    }
}