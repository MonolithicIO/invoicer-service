package io.github.alaksion.invoicer.foundation.storage.di

import io.github.alaksion.invoicer.foundation.storage.local.LocalStorage
import io.github.alaksion.invoicer.foundation.storage.local.LocalStorageHelper
import io.github.alaksion.invoicer.foundation.storage.remote.FileDownloader
import io.github.alaksion.invoicer.foundation.storage.remote.FileUploader
import io.github.alaksion.invoicer.foundation.storage.remote.SecureFileLinkGenerator
import io.github.alaksion.invoicer.foundation.storage.remote.minIO.MinIOFileUploader
import io.github.alaksion.invoicer.foundation.storage.remote.minIO.MinIOFilerDownloader
import io.github.alaksion.invoicer.foundation.storage.remote.minIO.MinIOSecureFileLinkGenerator
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val storageDiModule = DI.Module("storage-di") {
    bindProvider<FileUploader> { MinIOFileUploader(secretsProvider = instance()) }

    bindProvider<FileDownloader> { MinIOFilerDownloader(secretsProvider = instance()) }

    bindProvider<SecureFileLinkGenerator> { MinIOSecureFileLinkGenerator(secretsProvider = instance()) }

    bindProvider<LocalStorage> { LocalStorageHelper() }
}