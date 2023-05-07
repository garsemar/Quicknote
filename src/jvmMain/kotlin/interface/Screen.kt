package `interface`

import org.jetbrains.exposed.dao.id.EntityID

sealed interface Screen {
    object ItemList : Screen
    class ItemDetail(val id: EntityID<Int>) : Screen
    object ItemAdd : Screen
}