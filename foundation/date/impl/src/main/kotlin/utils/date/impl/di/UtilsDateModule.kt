package utils.date.impl.di

import org.kodein.di.DI
import org.kodein.di.bindProvider
import utils.date.impl.DateProvider
import utils.date.impl.DateProviderImplementation

val utilsDateModule = DI.Module("utils-date") {
    bindProvider<DateProvider> { DateProviderImplementation  }
}