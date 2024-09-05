package domains.intermediary.data.api.di

import domains.intermediary.data.api.datasource.IntermediaryDataSource
import domains.intermediary.data.api.datasource.IntermediaryDataSourceImpl
import domains.intermediary.data.api.repository.IntermediaryRepositoryImpl
import domains.intermediary.domain.api.repository.IntermediaryRepository
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val intermediaryDataModule = DI.Module("intermediary-data") {
    bindProvider<IntermediaryDataSource> { IntermediaryDataSourceImpl() }
    bindProvider<IntermediaryRepository> {
        IntermediaryRepositoryImpl(
            dataSource = instance()
        )
    }
}