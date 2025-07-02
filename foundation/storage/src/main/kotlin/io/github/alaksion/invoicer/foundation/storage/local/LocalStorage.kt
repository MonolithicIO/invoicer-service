package io.github.alaksion.invoicer.foundation.storage.local

interface LocalStorage {
    fun createDirectory(path: String): String
    fun deleteFile(path: String)
    fun getRootPath(): String
}
