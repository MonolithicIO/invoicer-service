package services.impl.beneficiary

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import models.beneficiary.BeneficiaryModel

object BeneficiaryTestData {

    val beneficiary = BeneficiaryModel(
        name = "Name",
        iban = "Iban",
        swift = "Swift,",
        bankName = "bank name",
        bankAddress = "bank address",
        userId = "6da1cca3-6784-4f75-8af8-36390b67a5e0",
        id = "d593ba02-c2bb-4be8-bd97-e71c02d229d3",
        createdAt = Instant.parse("2000-06-19T00:00:00Z"),
        updatedAt = Instant.parse("2000-06-19T00:00:00Z"),
    )
}
