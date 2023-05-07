package db

import model.Notes
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class SQLConfig {
    fun configSQLite(){
        Database.connect("jdbc:sqlite:notes.db", "org.sqlite.JDBC")

        transaction {
            SchemaUtils.create(Notes)
        }
    }
}