package repository.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import repository.CustomerRepository
import repository.CustomerRepositoryImpl
import repository.InvoicePdfRepository
import repository.InvoicePdfRepositoryImpl
import repository.InvoiceRepository
import repository.InvoiceRepositoryImpl
import repository.PaymentAccountRepository
import repository.PaymentAccountRepositoryImpl
import repository.QrCodeTokenRepository
import repository.QrCodeTokenRepositoryImpl
import repository.RefreshTokenRepository
import repository.RefreshTokenRepositoryImpl
import repository.UserCompanyRepository
import repository.UserCompanyRepositoryImpl
import repository.UserRepository
import repository.UserRepositoryImpl


val repositoryModule = DI.Module("invocer-repository") {

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

    bindProvider<PaymentAccountRepository> { PaymentAccountRepositoryImpl(clock = instance()) }

    bindProvider<UserCompanyRepository> { UserCompanyRepositoryImpl(clock = instance()) }

    bindProvider<CustomerRepository> {
        CustomerRepositoryImpl(
            clock = instance()
        )
    }
}
