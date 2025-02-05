package foundation.cache.impl.di

import foundation.cache.impl.CacheHandler
import foundation.cache.impl.lettuce.LettuceCacheHandlerImpl
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val cacheDiModule = DI.Module("cacheDiModule") {

    bindSingleton<CacheHandler> {
        LettuceCacheHandlerImpl(
            secretsProvider = instance()
        )
    }
}