package services.impl.intermediary

import kotlinx.datetime.LocalDate
import models.intermediary.IntermediaryModel

object IntermediaryTestData {

    val intermediary = IntermediaryModel(
        name = "Name",
        iban = "Iban",
        swift = "Swift,",
        bankName = "bank name",
        bankAddress = "bank address",
        userId = "6da1cca3-6784-4f75-8af8-36390b67a5e0",
        id = "d593ba02-c2bb-4be8-bd97-e71c02d229d3",
        createdAt = LocalDate(2000, 6, 19),
        updatedAt = LocalDate(2000, 6, 19)
    )
}
