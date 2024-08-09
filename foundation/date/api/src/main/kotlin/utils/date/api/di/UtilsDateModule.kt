package utils.date.api.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import utils.date.api.DateProvider
import utils.date.api.DateProviderImplementation

val utilsDateModule = DI.Module("utils-date") {
    bindProvider<DateProvider> { DateProviderImplementation  }
}