import org.ktorm.database.Database


object DatabaseFactory {
    val database by lazy {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/invoicer-api",
            driver = "org.postgresql.Driver",
            user = "invoicer",
            password = "1234"
        )
    }
}