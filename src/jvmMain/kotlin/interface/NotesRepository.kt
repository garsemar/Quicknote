package `interface`

import model.Note

interface NotesRepository {
    fun list(): List<Note>
    fun get(id: Int): Note
    fun remove(id: Int)
    fun addNote(title: String, content: String)
    fun config()
    fun editNote(id: Int, title: String, content: String)
}