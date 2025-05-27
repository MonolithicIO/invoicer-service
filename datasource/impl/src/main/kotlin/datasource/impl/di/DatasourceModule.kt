package datasource.impl.di

import datasource.api.database.*
import datasource.impl.database.*
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val datasourceModule = DI.Module("datasourceModule") {

    bindProvider<BeneficiaryDatabaseSource> {
        BeneficiaryDatabaseSourceImpl(
            clock = instance(),
        )
    }

    bindProvider<IntermediaryDatabaseSource> {
        IntermediaryDatabaseSourceImpl(
            clock = instance(),
        )
    }

    bindProvider<InvoiceDatabaseSource> {
        InvoiceDatabaseSourceImpl(
            clock = instance(),
        )
    }

    bindProvider<RefreshTokenDatabaseSource> {
        RefreshTokenDatabaseSourceImpl(
            dateProvider = instance(),
        )
    }

    bindProvider<UserDatabaseSource> {
        UserDatabaseSourceImpl(
            clock = instance(),
        )
    }

    bindProvider<QrCodeTokenDatabaseSource> {
        QrCodeDatabaseSourceImpl(
            clock = instance()
        )
    }

    bindProvider<InvoicePdfDatabaseSource> { InvoicePdfDatabaseSourceImpl(clock = instance()) }

    bindProvider<CompanyDatabaseSource> {
        CompanyDatabaseSourceImpl(
            clock = instance()
        )
    }
}