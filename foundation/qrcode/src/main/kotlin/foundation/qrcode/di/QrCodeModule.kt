package foundation.qrcode.di

import foundation.qrcode.QrCodeGenerator
import foundation.qrcode.impl.ZXingQrCodeGenerator
import org.kodein.di.DI
import org.kodein.di.bindProvider

val qrCodeModule = DI.Module("qrCodeModule") {
    bindProvider<QrCodeGenerator> { ZXingQrCodeGenerator() }
}