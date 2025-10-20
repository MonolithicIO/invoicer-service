package io.github.monolithic.invoicer.foundation.storage.di

import io.github.monolithic.invoicer.foundation.storage.local.LocalStorage
import io.github.monolithic.invoicer.foundation.storage.local.LocalStorageHelper
import io.github.monolithic.invoicer.foundation.storage.remote.FileUploader
import io.github.monolithic.invoicer.foundation.storage.remote.SecureFileLinkGenerator
import io.github.monolithic.invoicer.foundation.storage.remote.cloudflare.CloudflareFileUploader
import io.github.monolithic.invoicer.foundation.storage.remote.cloudflare.CloudflareSecureLink
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val storageDiModule = DI.Module("storage-di") {
    bindProvider<FileUploader> {
        CloudflareFileUploader(
            secretsProvider = instance(),
            logger = instance()
        )
    }

    bindProvider<SecureFileLinkGenerator> {
        CloudflareSecureLink(
            secretsProvider = instance(),
            logger = instance()
        )
    }

    bindProvider<LocalStorage> { LocalStorageHelper() }
}
