package io.github.alaksion.invoicer.foundation.storage.di

import io.github.alaksion.invoicer.foundation.storage.FileDownloader
import io.github.alaksion.invoicer.foundation.storage.FileUploader
import io.github.alaksion.invoicer.foundation.storage.minIO.MinIOFileUploader
import io.github.alaksion.invoicer.foundation.storage.minIO.MinIOFilerDownloader
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val storageDiModule = DI.Module("storage-di") {
    bindProvider<FileUploader> { MinIOFileUploader(secretsProvider = instance()) }

    bindProvider<FileDownloader> { MinIOFilerDownloader(secretsProvider = instance()) }
}