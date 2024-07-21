package io.github.alaksion.invoicer.server.files.pdfgenerator.components

import com.lowagie.text.Paragraph

internal fun OpenPdfSpacer(lines: Int = 1): Paragraph {
    require(lines >= 1)
    val blankSpaceString = (0..lines).map { "\n" }.joinToString(separator = "")

    return Paragraph(blankSpaceString)
}