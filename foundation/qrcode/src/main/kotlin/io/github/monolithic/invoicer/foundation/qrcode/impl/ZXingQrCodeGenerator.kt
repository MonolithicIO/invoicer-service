package io.github.monolithic.invoicer.foundation.qrcode.impl

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.client.j2se.MatrixToImageWriter
import io.github.monolithic.invoicer.foundation.qrcode.QrCodeGenerator
import java.io.ByteArrayOutputStream
import java.util.*

internal class ZXingQrCodeGenerator : QrCodeGenerator {

    override fun generateBase64QrCode(
        text: String,
        size: Int,
    ): String {
        val writer = MultiFormatWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size)
        val outputStream = ByteArrayOutputStream()

        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream)

        return Base64.getEncoder().encodeToString(outputStream.toByteArray())
    }
}
