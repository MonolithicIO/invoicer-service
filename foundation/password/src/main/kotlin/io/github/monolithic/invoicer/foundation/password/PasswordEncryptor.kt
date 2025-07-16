package io.github.monolithic.invoicer.foundation.password

import org.mindrot.jbcrypt.BCrypt


interface PasswordEncryption {

    fun encryptPassword(rawPassword: String?): String

    // Compare the raw password with the encrypted password stored in the database
    fun comparePassword(encryptedPassword: String?, rawPassword: String?): Boolean
}

internal class PasswordEncryptionImpl : PasswordEncryption {
    // Encrypt the raw password
    override fun encryptPassword(rawPassword: String?): String {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt())
    }

    // Compare the raw password with the encrypted password stored in the database
    override fun comparePassword(encryptedPassword: String?, rawPassword: String?): Boolean {
        return BCrypt.checkpw(rawPassword, encryptedPassword)
    }
}
