package io.github.monolithic.invoicer.foundation.qrcode.di

import io.github.monolithic.invoicer.foundation.qrcode.QrCodeGenerator
import io.github.monolithic.invoicer.foundation.qrcode.impl.ZXingQrCodeGenerator
import org.kodein.di.DI
import org.kodein.di.bindProvider

val qrCodeModule = DI.Module("qrCodeModule") {
    bindProvider<QrCodeGenerator> { ZXingQrCodeGenerator() }
}
