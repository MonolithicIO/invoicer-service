package io.github.monolithic.invoicer.services.pdf.pdfwriter.itext.components

import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal object PdfStyle {

    object FontSize {
        const val XLarge = 24F
        const val Medium = 16F
        const val Small = 12F
        const val XSmall = 8F
    }

    object Spacing {
        const val XLarge4 = 36f
        const val Small = 12f
    }


    object Color {
        @Suppress("MagicNumber")
        val Primary = DeviceRgb(66, 133, 244)
        val Background = ColorConstants.WHITE
        val Overlay = ColorConstants.LIGHT_GRAY
    }

    fun formatDate(date: LocalDate): String {
        return "${date.dayOfMonth}/${date.monthNumber}/${date.year}"
    }

    fun formatDate(instant: Instant): String {
        val formatter = DateTimeFormatter
            .ofPattern("dd/MM/yyyy")
            .withZone(ZoneId.systemDefault())
        return formatter.format(instant.toJavaInstant())
    }

    @Suppress("MagicNumber")
    fun formatCurrency(value: Long): String {
        val int = value / 100
        val decimals = value % 100
        return "$ $int,${decimals.toString().padStart(2, '0')}"
    }
}
