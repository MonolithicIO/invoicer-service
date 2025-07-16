package io.github.monolithic.invoicer.foundation.storage.local

interface LocalStorage {
    fun createDirectory(path: String): String
    fun deleteFile(path: String)
    fun getRootPath(): String
}
