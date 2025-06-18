package repository.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import repository.*


val repositoryModule = DI.Module("invocer-repository") {

    bindProvider<BeneficiaryRepository> {
        BeneficiaryRepositoryImpl(
            clock = instance(),
            cacheHandler = instance()
        )
    }

    bindProvider<IntermediaryRepository> {
        IntermediaryRepositoryImpl(
            clock = instance(),
            cacheHandler = instance()
        )
    }

    bindProvider<InvoiceRepository> {
        InvoiceRepositoryImpl(
            clock = instance(),
            cacheHandler = instance()
        )
    }

    bindProvider<UserRepository> {
        UserRepositoryImpl(
            clock = instance(),
        )
    }

    bindProvider<RefreshTokenRepository> {
        RefreshTokenRepositoryImpl(
            dateProvider = instance(),
        )
    }

    bindProvider<QrCodeTokenRepository> {
        QrCodeTokenRepositoryImpl(
            clock = instance(),
            cacheHandler = instance()
        )
    }

    bindProvider<InvoicePdfRepository> {
        InvoicePdfRepositoryImpl(clock = instance())
    }

    bindProvider<PaymentAccountRepository> { PaymentAccountRepositoryImpl() }

    bindProvider<UserCompanyRepository> { UserCompanyRepositoryImpl(clock = instance()) }
}