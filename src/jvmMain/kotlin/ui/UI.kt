package ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import db.NotesDao
import model.Note

class UI(private val notesDao: NotesDao){
    @Composable
    fun ItemList(onIdSet: (Int) -> Unit, onAddClick: () -> Unit) {
        val notes = notesDao.list()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(128.dp),
                content = {
                    notes.forEach { it ->
                        items(1) { i ->
                            Row {
                                Box(
                                    modifier = Modifier
                                        .clickable { onIdSet(it.id) }
                                        .border(1.dp, Color(143, 143, 143), RoundedCornerShape(10.dp))
                                        .widthIn(max = 250.dp)
                                        .padding(9.dp, 9.dp, 20.dp, 13.dp),
                                    contentAlignment = Alignment.TopStart
                                ) {
                                    Column {
                                        Text(
                                            text = it.title,
                                            style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Text(
                                            text = if(it.content.length > 100) it.content.substring(0, 100) + "..." else it.content,
                                            style = TextStyle(fontSize = 16.sp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            )

            FloatingActionButton(
                onClick = {
                    onAddClick()
                },
                modifier = Modifier
                    .padding(10.dp)
                    .size(50.dp)
                    .align(Alignment.BottomEnd)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    }

    @Composable
    fun ItemDetail(id: Int, onBackClick: () -> Unit, onEditClick: (note: Note) -> Unit) {
        val scrollState = rememberScrollState()
        val note = notesDao.get(id)
        Box{
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize()

            ) {
                IconButton(onClick = { onBackClick() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Text(note.title, style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))
                Text(note.content, style = TextStyle(fontSize = 16.sp), modifier = Modifier.verticalScroll(scrollState))
            }
            Row(
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                FloatingActionButton(
                    onClick = {
                        onEditClick(note)
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                FloatingActionButton(
                    onClick = {
                        notesDao.remove(id)
                        onBackClick()
                    },
                    modifier = Modifier
                        .padding(10.dp)
                        .size(50.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove")
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(scrollState)
            )
        }
    }

    @Composable
    fun ItemAdd(onBackClick: () -> Unit) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var title by remember { mutableStateOf("") }
            Box{
                OutlinedTextField(
                    title,
                    onValueChange = {
                        title = it
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.None
                    ),
                    shape = CircleShape,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                    ),
                    placeholder = {
                        Text(text = "Title")
                    },
                    maxLines = 1,
                    singleLine = true
                )
            }

            var content by remember { mutableStateOf("") }
            Box{
                OutlinedTextField(
                    content,
                    onValueChange = { content = it },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .widthIn(min = 500.dp, max = 500.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.None
                    ),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                    ),
                    placeholder = {
                        Text(text = "Content")
                    },
                    maxLines = 20,
                    singleLine = false,
                )
            }

            Button(modifier = Modifier
                .width(150.dp),
                onClick = {
                    if(title.isNotEmpty()){
                        notesDao.addNote(title, content)
                        onBackClick()
                    }
                }
            ) {
                Text(color = Color.White, text = "Add")
            }
            Button(onClick = { onBackClick() }){
                Text("Cancel")
            }
        }
    }

    @Composable
    fun ItemEdit(onBackClick: (id: Int) -> Unit, note: Note) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var title by remember { mutableStateOf(note.title) }
            Box{
                OutlinedTextField(
                    title,
                    onValueChange = {
                        title = it
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.None
                    ),
                    shape = CircleShape,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                    ),
                    placeholder = {
                        Text(text = "Title")
                    },
                    maxLines = 1,
                    singleLine = true
                )
            }

            var content by remember { mutableStateOf(note.content) }
            Box{
                OutlinedTextField(
                    content,
                    onValueChange = { content = it },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .widthIn(min = 500.dp, max = 500.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Characters,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.None
                    ),
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                    ),
                    placeholder = {
                        Text(text = "Content")
                    },
                    maxLines = 20,
                    singleLine = false,
                )
            }

            Button(modifier = Modifier
                .width(150.dp),
                onClick = {
                    if(title.isNotEmpty()){
                        notesDao.editNote(note.id, title, content)
                        onBackClick(note.id)
                    }
                }
            ) {
                Text(color = Color.White, text = "Edit")
            }
            Button(onClick = { onBackClick(note.id) }){
                Text("Cancel")
            }
        }
    }
}