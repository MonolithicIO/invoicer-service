package datasource.api.model.paymentaccount

enum class PaymentAccountType(
    val descriptor: String
) {
    Primary("primary"),
    Intermediary("intermediary")
}