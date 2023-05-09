package db

import `interface`.NotesRepository
import model.Note
import model.Notes
import model.Notes.mapNotes
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class NotesDao: NotesRepository {
    init {
        config()
    }

    override fun list(): List<Note> = transaction {
        Notes.selectAll().mapNotes().filter { !it.removed }
    }

    override fun get(id: Int): Note = transaction {
        Notes.select { Notes.id eq id }.mapNotes().first()
    }

    override fun remove(id: Int): Unit = transaction {
        Notes.update ({ Notes.id eq id }) {
            it[removed] = true
        }
    }

    override fun addNote(title: String, content: String): Unit = transaction {
        try {
            Notes.insertAndGetId {
                it[Notes.title] = title
                it[Notes.content] = content
            }
        }
        catch (e: ExposedSQLException){
            println(e)
        }
    }

    override fun editNote(id: Int, title: String, content: String): Unit = transaction {
        Notes.update({ Notes.id eq id }){
            it[Notes.title] = title
            it[Notes.content] = content
        }
    }

    override fun config(){
        Database.connect("jdbc:sqlite:notes.db", "org.sqlite.JDBC")

        transaction {
            SchemaUtils.create(Notes)
        }
    }
}