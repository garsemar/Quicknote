package model

import org.jetbrains.exposed.dao.id.EntityID

data class Note(val id: EntityID<Int>, val title: String, val content: String, val done: Boolean, val removed: Boolean)