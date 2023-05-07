package db

import model.Note
import model.Notes
import model.Notes.mapNotes
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class NotesDao {
    fun list(): List<Note> = transaction {
        Notes.selectAll().mapNotes().filter { !it.removed }
    }

    fun get(id: EntityID<Int>): Note = transaction {
        Notes.select { Notes.id eq id }.mapNotes().first()
    }

    fun remove(id: EntityID<Int>) = transaction {
        Notes.update ({ Notes.id eq id }) {
            it[removed] = true
        }
    }

    fun addNote(title: String, content: String) = transaction {
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
}