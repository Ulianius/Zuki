package com.example.zuki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


val PinkMain  = Color(0xFFEC6A9C)
val PinkDark  = Color(0xFFC94F7C)
val PinkLight = Color(0xFFF8C8DC)
val PinkText  = Color(0xFFFFFFFF)



const val MIN_YEAR = 1926
const val MAX_YEAR = 2026


enum class Screen {
    Menu,
    Register,
    Settings,
    Rules,
    Authors
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Tabs()
                }
            }
        }
    }
}


@Composable
fun Tabs() {
    var screen by remember { mutableStateOf(Screen.Menu) }
    when (screen) {
        Screen.Menu -> MenuScreen(
            onRegister = { screen = Screen.Register },
            onSettings = { screen = Screen.Settings },
            onRules    = { screen = Screen.Rules },
            onAuthors  = { screen = Screen.Authors }
        )
        Screen.Register -> PlayerFormScreen(onBack = { screen = Screen.Menu })
        Screen.Settings -> SettingsScreen(onBack   = { screen = Screen.Menu })
        Screen.Rules    -> RulesScreen(onBack      = { screen = Screen.Menu })
        Screen.Authors  -> AuthorsScreen(onBack    = { screen = Screen.Menu })
    }
}


@Composable
fun MenuScreen(
    onRegister: () -> Unit,
    onSettings: () -> Unit,
    onRules: () -> Unit,
    onAuthors: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PinkLight)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = painterResource(id = R.drawable.ob),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = 40.dp),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Игра Будни общаги",
            style = MaterialTheme.typography.displayMedium,
            color = PinkMain,
            textAlign = TextAlign.Center
        )

        Text(
            text = "При поддержке общежития №4 СибГУТИ",
            style = MaterialTheme.typography.bodyLarge,
            color = PinkDark,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        MenuButton("Регистрация", onRegister)
        Spacer(modifier = Modifier.height(12.dp))
        MenuButton("Настройки игры", onSettings)
        Spacer(modifier = Modifier.height(12.dp))
        MenuButton("Правила игры", onRules)
        Spacer(modifier = Modifier.height(12.dp))
        MenuButton("Авторы", onAuthors)
    }
}


@Composable
private fun MenuButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = PinkMain,
            contentColor   = Color.White
        ),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(text, style = MaterialTheme.typography.titleLarge)
    }
}