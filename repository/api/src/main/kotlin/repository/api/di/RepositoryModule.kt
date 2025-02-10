package repository.api.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import repository.api.repository.*

val repositoryModule = DI.Module("invocer-repository") {

    bindProvider<BeneficiaryRepository> {
        BeneficiaryRepositoryImpl(
            databaseSource = instance(),
            cacheHandler = instance()
        )
    }

    bindProvider<IntermediaryRepository> {
        IntermediaryRepositoryImpl(
            databaseSource = instance()
        )
    }

    bindProvider<InvoiceRepository> {
        InvoiceRepositoryImpl(
            databaseSource = instance()
        )
    }

    bindProvider<UserRepository> {
        UserRepositoryImpl(
            databaseSource = instance()
        )
    }

    bindProvider<RefreshTokenRepository> {
        RefreshTokenRepositoryImpl(
            databaseSource = instance()
        )
    }
}