package io.github.monolithic.invoicer.foundation.storage.di

import io.github.monolithic.invoicer.foundation.storage.local.LocalStorage
import io.github.monolithic.invoicer.foundation.storage.local.LocalStorageHelper
import io.github.monolithic.invoicer.foundation.storage.remote.FileDownloader
import io.github.monolithic.invoicer.foundation.storage.remote.FileUploader
import io.github.monolithic.invoicer.foundation.storage.remote.SecureFileLinkGenerator
import io.github.monolithic.invoicer.foundation.storage.remote.google.GoogleFileUploader
import io.github.monolithic.invoicer.foundation.storage.remote.minIO.MinIOFilerDownloader
import io.github.monolithic.invoicer.foundation.storage.remote.minIO.MinIOSecureFileLinkGenerator
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val storageDiModule = DI.Module("storage-di") {
    bindProvider<FileUploader> {
        GoogleFileUploader(
            secretsProvider = instance(),
            logger = instance()
        )
    }

    bindProvider<FileDownloader> { MinIOFilerDownloader(secretsProvider = instance()) }

    bindProvider<SecureFileLinkGenerator> { MinIOSecureFileLinkGenerator(secretsProvider = instance()) }

    bindProvider<LocalStorage> { LocalStorageHelper() }
}
