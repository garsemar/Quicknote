import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.AppConfiguration
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.exceptions.InvalidCredentialsException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val realmApp = App.create(AppConfiguration.Builder("quicknote-tolnt").build())

sealed interface Screen{
    object LoginScreen: Screen
    class MenuScreen(val userName: String): Screen
}

/*
* val user = realmApp.currentUser!!

    val config = SyncConfiguration.Builder(user, setOf(Posicions::class))
        .initialSubscriptions { realm ->
            add(
                realm.query<Posicions>(),
                "All Items"
            )
        }
        .waitForInitialRemoteData()
        .build()
    val realm = Realm.open(config)
    GlobalScope.launch {
        realm.subscriptions.waitForSynchronization()
    }
* */

@Composable
@Preview
fun Menu() {
    var screen by remember { mutableStateOf<Screen>(Screen.LoginScreen) }

    MaterialTheme {
        when(val currentScreen = screen){
            Screen.LoginScreen -> LoginScreen(onNameSet = { username ->
                screen = Screen.MenuScreen(username)
            })
            is Screen.MenuScreen -> MenuScreen(currentScreen.userName)
        }
    }
}

@Composable
fun LoginScreen(onNameSet : (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var user by remember { mutableStateOf("") }
        Box{
            OutlinedTextField(
                user,
                onValueChange = {
                    user = it
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
                    Text(text = "User")
                },
                maxLines = 1,
                singleLine = true
            )
        }

        var password by remember { mutableStateOf("") }
        Box(
            modifier = Modifier
                .padding(start = 6.dp, end = 6.dp, bottom = 6.dp, top = 6.dp)
        ) {
            OutlinedTextField(
                password,
                onValueChange = {
                    password = it
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
                    Text(text = "Password")
                },
                visualTransformation = PasswordVisualTransformation(),
                maxLines = 1,
                singleLine = true
            )
        }

        Button(modifier = Modifier
            .width(150.dp),
            onClick = {
                if(login(user, password) == true){
                    onNameSet("Welcome")
                }
            }
        ) {
            Text(color = Color.White, text = "Login")
        }
    }
}

@Composable
fun MenuScreen(userName: String) {
    Text(userName)
}

fun login(user: String, password: String): Boolean? {
    var valid: Boolean? = null
    runBlocking{
        val creds: Credentials = Credentials.emailPassword(user, password)
        try {
            realmApp.login(creds)
            valid = true
        }
        catch (e: InvalidCredentialsException){
            println("Invalid password/user")
            valid = false
        }
    }
    return valid
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "Flyer") {
        Menu()
    }
}