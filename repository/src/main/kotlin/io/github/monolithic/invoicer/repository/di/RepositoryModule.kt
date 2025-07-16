package io.github.monolithic.invoicer.repository.di

import io.github.monolithic.invoicer.repository.CustomerRepository
import io.github.monolithic.invoicer.repository.CustomerRepositoryImpl
import io.github.monolithic.invoicer.repository.InvoicePdfRepository
import io.github.monolithic.invoicer.repository.InvoicePdfRepositoryImpl
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance
import io.github.monolithic.invoicer.repository.InvoiceRepository
import io.github.monolithic.invoicer.repository.InvoiceRepositoryImpl
import io.github.monolithic.invoicer.repository.PaymentAccountRepository
import io.github.monolithic.invoicer.repository.PaymentAccountRepositoryImpl
import io.github.monolithic.invoicer.repository.QrCodeTokenRepository
import io.github.monolithic.invoicer.repository.QrCodeTokenRepositoryImpl
import io.github.monolithic.invoicer.repository.RefreshTokenRepository
import io.github.monolithic.invoicer.repository.RefreshTokenRepositoryImpl
import io.github.monolithic.invoicer.repository.UserCompanyRepository
import io.github.monolithic.invoicer.repository.UserCompanyRepositoryImpl
import io.github.monolithic.invoicer.repository.UserRepository
import io.github.monolithic.invoicer.repository.UserRepositoryImpl
import io.github.monolithic.invoicer.repository.datasource.CustomerDataSource
import io.github.monolithic.invoicer.repository.datasource.CustomerDataSourceImpl
import io.github.monolithic.invoicer.repository.datasource.InvoiceDataSource
import io.github.monolithic.invoicer.repository.datasource.InvoiceDataSourceImpl
import io.github.monolithic.invoicer.repository.datasource.InvoicePdfDataSource
import io.github.monolithic.invoicer.repository.datasource.InvoicePdfDataSourceImpl
import io.github.monolithic.invoicer.repository.datasource.PaymentAccountDataSource
import io.github.monolithic.invoicer.repository.datasource.PaymentAccountDataSourceImpl
import io.github.monolithic.invoicer.repository.datasource.QrCodeTokenDataSource
import io.github.monolithic.invoicer.repository.datasource.QrCodeTokenDataSourceImpl
import io.github.monolithic.invoicer.repository.datasource.RefreshTokenDataSource
import io.github.monolithic.invoicer.repository.datasource.RefreshTokenDataSourceImpl
import io.github.monolithic.invoicer.repository.datasource.UserCompanyDataSource
import io.github.monolithic.invoicer.repository.datasource.UserCompanyDataSourceImpl
import io.github.monolithic.invoicer.repository.datasource.UserDataSource
import io.github.monolithic.invoicer.repository.datasource.UserDataSourceImpl


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
