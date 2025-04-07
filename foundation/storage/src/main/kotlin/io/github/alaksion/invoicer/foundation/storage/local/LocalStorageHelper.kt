package io.github.alaksion.invoicer.foundation.storage.local

import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.deleteIfExists

internal class LocalStorageHelper : LocalStorage {

    override fun createDirectory(path: String): String {
        val projectPath = Path("")

        val tempDirectory = File(projectPath.absolutePathString(), "/temp/pdfs")

        if (tempDirectory.exists().not()) {
            tempDirectory.mkdirs()
        }

        return tempDirectory.absolutePath
    }

    override fun deleteFile(path: String) {
        Path(path).deleteIfExists()
    }

    override fun getRootPath(): String {
        return Path("").absolutePathString()
    }
}