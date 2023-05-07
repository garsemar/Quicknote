package model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Query

object Notes: IntIdTable() {
    fun Query.mapNotes(): List<Note> =
        this.map {
            Note(
                it[id],
                it[title],
                it[content],
                it[done],
                it[removed]
            )
        }
    val title = varchar("title", 50)
    val content = varchar("content", 200)
    val done = bool("done").default(false)
    val removed = bool("removed").default(false)
}