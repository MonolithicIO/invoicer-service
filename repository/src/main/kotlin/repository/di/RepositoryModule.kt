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
import repository.datasource.CustomerDataSource
import repository.datasource.CustomerDataSourceImpl
import repository.datasource.InvoiceDataSource
import repository.datasource.InvoiceDataSourceImpl
import repository.datasource.InvoicePdfDataSource
import repository.datasource.InvoicePdfDataSourceImpl
import repository.datasource.PaymentAccountDataSource
import repository.datasource.PaymentAccountDataSourceImpl
import repository.datasource.QrCodeTokenDataSource
import repository.datasource.QrCodeTokenDataSourceImpl
import repository.datasource.RefreshTokenDataSource
import repository.datasource.RefreshTokenDataSourceImpl
import repository.datasource.UserCompanyDataSource
import repository.datasource.UserCompanyDataSourceImpl
import repository.datasource.UserDataSource
import repository.datasource.UserDataSourceImpl


val repositoryModule = DI.Module("invocer-repository") {

    bindProvider<InvoiceRepository> {
        InvoiceRepositoryImpl(
            invoiceDataSource = instance(),
            cacheHandler = instance()
        )
    }

    bindProvider<UserRepository> {
        UserRepositoryImpl(
            userDataSource = instance(),
        )
    }

    bindProvider<RefreshTokenRepository> {
        RefreshTokenRepositoryImpl(
            dataSource = instance(),
        )
    }

    bindProvider<QrCodeTokenRepository> {
        QrCodeTokenRepositoryImpl(
            cacheHandler = instance(),
            qrCodeTokenDataSource = instance(),
        )
    }

    bindProvider<InvoicePdfRepository> {
        InvoicePdfRepositoryImpl(invoicePdfDataSource = instance())
    }

    bindProvider<PaymentAccountRepository> { PaymentAccountRepositoryImpl(dataSource = instance()) }

    bindProvider<UserCompanyRepository> { UserCompanyRepositoryImpl(datasource = instance()) }

    bindProvider<CustomerRepository> {
        CustomerRepositoryImpl(
            customerDataSource = instance(),
        )
    }

    bindProvider<CustomerDataSource> {
        CustomerDataSourceImpl(
            clock = instance()
        )
    }

    bindProvider<InvoiceDataSource> {
        InvoiceDataSourceImpl(
            clock = instance(),
        )
    }

    bindProvider<InvoicePdfDataSource> {
        InvoicePdfDataSourceImpl(
            clock = instance()
        )
    }

    bindProvider<QrCodeTokenDataSource> {
        QrCodeTokenDataSourceImpl(
            clock = instance(),
        )
    }

    bindProvider<RefreshTokenDataSource> {
        RefreshTokenDataSourceImpl(
            dateProvider = instance()
        )
    }

    bindProvider<UserCompanyDataSource> {
        UserCompanyDataSourceImpl(
            clock = instance()
        )
    }

    bindProvider<UserDataSource> {
        UserDataSourceImpl(clock = instance())
    }

    bindProvider<PaymentAccountDataSource> {
        PaymentAccountDataSourceImpl(
            clock = instance()
        )
    }
}
