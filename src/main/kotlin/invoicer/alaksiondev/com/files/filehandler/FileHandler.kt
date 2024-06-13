package invoicer.alaksiondev.com.files.filehandler

import java.io.File

interface FileHandler {
    fun createFile(path: String, fileName: String): String
    fun removeFile(path: String): Boolean
}

internal object TempFileHandler : FileHandler {

    override fun createFile(path: String, fileName: String): String {
        val rootDir = File(".").absolutePath
        val outputDir = File(rootDir, "temp/$path")

        if (outputDir.exists().not()) {
            outputDir.mkdirs()
        }

        val pdfFileDir = File(outputDir, fileName)

        if (pdfFileDir.exists()) {
            removeFile(pdfFileDir.absolutePath)
        }

        return pdfFileDir.absolutePath

    }

    override fun removeFile(path: String): Boolean {
        val file = File(path)
        if (file.exists()) file.delete()
        return file.exists()
    }

}