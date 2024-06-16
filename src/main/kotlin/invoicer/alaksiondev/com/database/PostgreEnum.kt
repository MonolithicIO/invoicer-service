package invoicer.alaksiondev.com.database

import org.postgresql.util.PGobject

class PostgreEnum<T : Enum<T>>(
    enumTypeName: String,
    enumValue: T?
) : PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}