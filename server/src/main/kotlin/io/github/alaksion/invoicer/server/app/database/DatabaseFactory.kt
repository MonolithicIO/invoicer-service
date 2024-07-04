import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun connect() {
        val env = dotenv()
        val database = env["DB_NAME"]
        val password = env["DB_PASSWORD"]
        val username = env["DB_USERNAME"]

        Database.connect(
            url = "jdbc:postgresql://localhost:5432/${database}",
            driver = "org.postgresql.Driver",
            user = username,
            password = password,
        )
    }
}