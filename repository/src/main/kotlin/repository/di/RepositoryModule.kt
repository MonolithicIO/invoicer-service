package repository.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import repository.*


val repositoryModule = DI.Module("invocer-repository") {

    bindProvider<BeneficiaryRepository> {
        BeneficiaryRepositoryImpl(
            databaseSource = instance(),
            cacheHandler = instance()
        )
    }

    bindProvider<IntermediaryRepository> {
        IntermediaryRepositoryImpl(
            databaseSource = instance(),
            cacheHandler = instance()
        )
    }

    bindProvider<InvoiceRepository> {
        InvoiceRepositoryImpl(
            databaseSource = instance(),
            cacheHandler = instance()
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

    bindProvider<QrCodeTokenRepository> {
        QrCodeTokenRepositoryImpl(
            databaseSource = instance(),
            cacheHandler = instance()
        )
    }

    bindProvider<InvoicePdfRepository> { InvoicePdfRepositoryImpl(databaseSource = instance()) }

    bindProvider<PaymentAccountRepository> {
        PaymentAccountRepositoryImpl(
            databaseSource = instance()
        )
    }
}