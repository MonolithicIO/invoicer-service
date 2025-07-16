package io.github.monolithic.invoicer.foundation.qrcode.fakes

import io.github.monolithic.invoicer.foundation.qrcode.QrCodeGenerator

class FakeQrCodeGenerator : QrCodeGenerator {

    var response = "1234"

    override fun generateBase64QrCode(text: String, size: Int): String {
        return response
    }
}