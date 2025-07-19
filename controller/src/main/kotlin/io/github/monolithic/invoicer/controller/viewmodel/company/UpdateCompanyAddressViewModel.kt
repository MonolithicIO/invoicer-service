package io.github.monolithic.invoicer.controller.viewmodel.company

import io.github.monolithic.invoicer.controller.validation.requiredString
import io.github.monolithic.invoicer.models.company.UpdateCompanyAddressModel
import io.github.monolithic.invoicer.utils.uuid.parseUuid
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateCompanyAddressViewModel(
    val addressLine: String? = null,
    val addressLine2: String? = null,
    val city: String? = null,
    val state: String? = null,
    val postalCode: String? = null
)

internal fun UpdateCompanyAddressViewModel.toModel(companyId: String) =
    UpdateCompanyAddressModel(
        companyId = parseUuid(companyId),
        addressLine = requiredString(addressLine, "field addressLine is required"),
        addressLine2 = addressLine2,
        city = requiredString(city, "field city is required"),
        state = requiredString(state, "field state is required"),
        postalCode = requiredString(postalCode, "field postalCode is required")
    )