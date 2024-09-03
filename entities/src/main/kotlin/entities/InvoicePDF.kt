package entities

//internal object InvoicePDFTable : UUIDTable("t_invoice_pdf") {
//    val path = varchar("path", 100).nullable()
//    val status = customEnumeration(
//        name = "status",
//        sql = "pdf_status",
//        fromDb = { value -> InvoicePDFStatus.valueOf(value as String) },
//        toDb = { PostgreEnum("pdf_status", it) })
//    val invoice = reference(name = "invoice_id", foreign = InvoiceTable, onDelete = ReferenceOption.CASCADE)
//    val createdAt = date("created_at")
//    val updatedAt = date("updated_at")
//}
//
//internal class InvoicePDFEntity(id: EntityID<UUID>) : UUIDEntity(id) {
//    companion object : UUIDEntityClass<InvoicePDFEntity>(InvoicePDFTable)
//
//    var path: String? by InvoicePDFTable.path
//    var status: InvoicePDFStatus by InvoicePDFTable.status
//    var createdAt: LocalDate by InvoicePDFTable.createdAt
//    var updatedAt: LocalDate by InvoicePDFTable.updatedAt
//    var invoice by InvoiceEntity referencedOn InvoicePDFTable.invoice
//}
//
//enum class InvoicePDFStatus {
//    ok,
//    pending,
//    failed;
//}
//
//internal fun InvoicePDFStatus.toModel(): InvoicePdfStatusModel = when (this) {
//    InvoicePDFStatus.ok -> InvoicePdfStatusModel.Ok
//    InvoicePDFStatus.failed -> InvoicePdfStatusModel.Failed
//    InvoicePDFStatus.pending -> InvoicePdfStatusModel.Pending
//}
//
//internal fun InvoicePDFEntity.toModel(): InvoicePdfModel = InvoicePdfModel(
//    id = this.id.value.toString(),
//    path = this.path,
//    createdAt = this.createdAt,
//    updatedAt = this.updatedAt,
//    status = this.status.toModel()
//)