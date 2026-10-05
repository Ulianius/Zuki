package com.example.zuki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.util.Calendar
import java.util.Locale
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextAlign


val PinkMain  = Color(0xFFEC6A9C)
val PinkDark  = Color(0xFFC94F7C)
val PinkLight = Color(0xFFF8C8DC)
val PinkText  = Color(0xFFFFFFFF)
const val MIN_YEAR = 1926
const val MAX_YEAR = 2026
enum class Screen {
    Menu,
    Register,
    Difficulty,
    Authors,
    Rules
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
fun Tabs(){
    var screen by remember { mutableStateOf(Screen.Menu) }
    when (screen) {
        Screen.Menu -> MenuScreen(
            onRegister   = { screen = Screen.Register },
            onDifficulty = { screen = Screen.Difficulty },
            onAuthors    = { screen = Screen.Authors },
            onRules     = {screen = Screen.Rules}
        )
        Screen.Register   -> PlayerFormScreen(onBack = { screen = Screen.Menu })
        Screen.Difficulty -> PlayerFormScreen(onBack = { screen = Screen.Menu })
        Screen.Rules      -> RulesScreen(onBack      = { screen = Screen.Menu })
        Screen.Authors    -> AuthorsScreen(onBack    = { screen = Screen.Menu })
    }
}

@Composable
private fun navItemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor   = Color.White,
    selectedTextColor   = PinkMain,
    indicatorColor      = PinkMain,
    unselectedIconColor = PinkMain,
    unselectedTextColor = PinkMain
)

@Composable
fun MenuScreen(
    onRegister: () -> Unit,
    onDifficulty: () -> Unit,
    onAuthors: () -> Unit,
    onRules: () -> Unit
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
            contentAlignment = Alignment.TopCenter         // ← выравниваем по ВЕРХУ
        ) {
            Image(
                painter = painterResource(id = R.drawable.ob),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(y = 40.dp),                    // ← сдвигаем вниз на 40dp
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
        MenuButton("Настройка игры", onDifficulty)
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

