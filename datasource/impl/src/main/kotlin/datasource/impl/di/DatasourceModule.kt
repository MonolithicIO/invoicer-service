package datasource.impl.di

import datasource.api.database.*
import datasource.impl.database.*
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val datasourceModule = DI.Module("datasourceModule") {

    bindProvider<BeneficiaryDatabaseSource> {
        BeneficiaryDatabaseSourceImpl(
            dateProvider = instance(),
        )
    }

    bindProvider<IntermediaryDatabaseSource> {
        IntermediaryDatabaseSourceImpl(
            dateProvider = instance(),
        )
    }

    bindProvider<InvoiceDatabaseSource> {
        InvoiceDatabaseSourceImpl(
            dateProvider = instance(),
        )
    }

    bindProvider<RefreshTokenDatabaseSource> {
        RefreshTokenDatabaseSourceImpl(
            dateProvider = instance(),
        )
    }

    bindProvider<UserDatabaseSource> {
        UserDatabaseSourceImpl(
            dateProvider = instance(),
        )
    }

    bindProvider<QrCodeTokenDatabaseSource> {
        QrCodeDatabaseSourceImpl(
            dateProvider = instance()
        )
    }

}