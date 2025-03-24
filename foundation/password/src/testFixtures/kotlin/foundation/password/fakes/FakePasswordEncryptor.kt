package foundation.password.fakes

import foundation.password.PasswordEncryption


class FakePasswordEncryption : PasswordEncryption {
    var encryptResponse: () -> String = { "" }
    var compareResponse: () -> Boolean = { true }

    // Encrypt the raw password
    override fun encryptPassword(rawPassword: String?): String {
        return encryptResponse()
    }

    // Compare the raw password with the encrypted password stored in the database
    override fun comparePassword(encryptedPassword: String?, rawPassword: String?): Boolean {
        return compareResponse()
    }
}