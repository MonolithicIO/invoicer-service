package io.github.monolithic.invoicer.models.fixtures

import kotlinx.datetime.Instant
import io.github.monolithic.invoicer.models.company.CompanyAddressModel
import io.github.monolithic.invoicer.models.company.CompanyDetailsModel
import io.github.monolithic.invoicer.models.paymentaccount.PaymentAccountModel
import io.github.monolithic.invoicer.models.paymentaccount.PaymentAccountTypeModel
import io.github.monolithic.invoicer.models.user.UserModel
import java.util.*

val companyDetailsFixture = CompanyDetailsModel(
    name = "Test Company",
    document = "123456789",
    createdAt = Instant.parse("2023-01-01T00:00:00Z"),
    updatedAt = Instant.parse("2023-01-02T00:00:00Z"),
    isDeleted = false,
    id = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
    address = CompanyAddressModel(
        addressLine1 = "123 Test St",
        addressLine2 = null,
        city = "Test City",
        state = "Test State",
        postalCode = "12345",
        countryCode = "US"
    ),
    paymentAccount = PaymentAccountModel(
        swift = "ABCDEF12",
        iban = "DE89370400440532013000",
        bankName = "Bank of Beneficiary",
        bankAddress = "789 Bank St",
        type = PaymentAccountTypeModel.Primary,
        isDeleted = false,
        createdAt = Instant.parse("2023-01-01T00:00:00Z"),
        updatedAt = Instant.parse("2023-01-02T00:00:00Z"),
        id = UUID.fromString("123e4567-e89b-12d3-a456-426614174003"),
        companyId = UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
    ),
    intermediaryAccount = null,
    user = UserModel(
        id = UUID.fromString("123e4567-e89b-12d3-a456-426614174002"),
        email = "testuser@example.com",
        password = "123123",
        verified = true,
        createdAt = Instant.parse("2023-01-01T00:00:00Z"),
        updatedAt = Instant.parse("2023-01-02T00:00:00Z"),
        identityProviderUuid = null,
    )
)