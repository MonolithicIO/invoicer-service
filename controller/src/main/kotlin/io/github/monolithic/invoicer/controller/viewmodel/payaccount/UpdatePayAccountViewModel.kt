package io.github.monolithic.invoicer.controller.viewmodel.payaccount

import io.github.monolithic.invoicer.controller.validation.requiredString
import kotlinx.serialization.Serializable
import io.github.monolithic.invoicer.models.paymentaccount.UpdatePaymentAccountModel
import java.util.*

@Serializable
internal data class UpdatePayAccountViewModel(
    val bankName: String? = null,
    val bankAddress: String? = null,
    val swift: String? = null,
    val iban: String? = null
)

internal fun UpdatePayAccountViewModel.toModel(
    id: UUID
): UpdatePaymentAccountModel {
    return UpdatePaymentAccountModel(
        id = id,
        bankName = requiredString(bankName, "Missing bank name"),
        bankAddress = requiredString(bankAddress, "Missing bank address"),
        swift = requiredString(swift, "Required SWIFT code"),
        iban = requiredString(iban, "Required IBAN code")
    )
}
