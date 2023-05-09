package `interface`

import model.Note
import org.jetbrains.exposed.dao.id.EntityID

sealed interface Screen {
    object ItemList : Screen
    class ItemDetail(val id: Int) : Screen
    object ItemAdd : Screen
    class ItemEdit(val note: Note) : Screen
}