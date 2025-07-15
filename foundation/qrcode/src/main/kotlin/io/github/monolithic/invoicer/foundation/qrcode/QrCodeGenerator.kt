package io.github.monolithic.invoicer.foundation.qrcode

interface QrCodeGenerator {
    /**
     * Returns a Base64 encoded string representing a QR code image of the given text.
     *
     * @param text The text to encode in the QR code.
     * @param size The size of the QR code image.
     * */
    fun generateBase64QrCode(text: String, size: Int): String
}
