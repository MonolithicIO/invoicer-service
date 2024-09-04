package domains.user.data.api.di

import domains.user.data.api.datasource.UserDataSource
import domains.user.data.api.datasource.UserDataSourceImpl
import domains.user.data.api.repository.UserRepositoryImpl
import domains.user.domain.repository.UserRepository
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val userDataModule = DI.Module("user_data_module") {

    bindProvider<UserDataSource> {
        UserDataSourceImpl()
    }

    bindProvider<UserRepository> {
        UserRepositoryImpl(
            dataSource = instance()
        )
    }
}