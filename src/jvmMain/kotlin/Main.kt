import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import db.NotesDao
import `interface`.Screen
import ui.UI

@Composable
@Preview
fun Menu() {
    val notesDao = NotesDao()
    notesDao.config()
    val ui = UI(notesDao)
    var screen by remember { mutableStateOf<Screen>(Screen.ItemList) }

    MaterialTheme {
        when (val currentScreen = screen) {
            Screen.ItemList -> ui.ItemList(onIdSet = { id ->
                screen = Screen.ItemDetail(id)
            }, onAddClick = {
                    screen = Screen.ItemAdd
            })

            is Screen.ItemDetail -> ui.ItemDetail(currentScreen.id, onBackClick = {
                screen = Screen.ItemList
            },
            onEditClick = {
                screen = Screen.ItemEdit(notesDao.get(currentScreen.id))
            })

            is Screen.ItemAdd -> ui.ItemAdd(onBackClick = {
                screen = Screen.ItemList
            })

            is Screen.ItemEdit -> ui.ItemEdit(note = currentScreen.note, onBackClick = {
                screen = Screen.ItemDetail(currentScreen.note.id)
            })
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Flyer") {
        Menu()
    }
}