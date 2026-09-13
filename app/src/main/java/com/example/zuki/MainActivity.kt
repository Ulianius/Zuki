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


private val PinkMain  = Color(0xFFEC6A9C)
private val PinkDark  = Color(0xFFC94F7C)
private val PinkLight = Color(0xFFF8C8DC)
private val PinkText  = Color(0xFFFFFFFF)


private const val MIN_YEAR = 1926
private const val MAX_YEAR = 2026

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PlayerFormScreen()
                }
            }
        }
    }
}

private fun filterNameInput(input: String): String {
    val allowed = input.filter { ch ->
        ch.isLetter() || ch == ' ' || ch == '-'
    }
    // Убираем многократные пробелы подряд и ведущие/замыкающие пробелы
    return allowed.replace(Regex("\\s+"), " ")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerFormScreen() {
    var fullName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Мужской") }
    var course by remember { mutableStateOf("1 курс") }
    var difficulty by remember { mutableFloatStateOf(5f) }

    // ---- Поля даты ----
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var dateError by remember { mutableStateOf<String?>(null) }

    var nameError by remember { mutableStateOf<String?>(null) }

    var player by remember { mutableStateOf<Player?>(null) }

    val courses = listOf("1 курс", "2 курс", "3 курс", "4 курс", "Магистратура")

    val pinkFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor   = PinkMain,
        unfocusedBorderColor = PinkMain,
        focusedLabelColor    = PinkMain,
        unfocusedLabelColor  = PinkMain,
        cursorColor          = PinkMain,
        focusedTextColor     = Color.Black,
        unfocusedTextColor   = Color.Black,
        focusedTrailingIconColor   = PinkMain,
        unfocusedTrailingIconColor = PinkMain
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(32.dp))


        OutlinedTextField(
            value = fullName,
            onValueChange = { input ->
                fullName = filterNameInput(input)
                nameError = null
            },
            label = { Text("ФИО") },
            placeholder = { Text("Иванов Иван Иванович") },
            isError = nameError != null,
            colors = pinkFieldColors,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier.fillMaxWidth()
        )
        nameError?.let {
            Text(it, color = Color.Red)
        }

        Text("Пол")
        Row(verticalAlignment = Alignment.CenterVertically) {
            listOf("Мужской", "Женский").forEach { option ->
                Row(
                    modifier = Modifier
                        .selectable(
                            selected = gender == option,
                            onClick = { gender = option }
                        )
                        .padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = gender == option,
                        onClick = { gender = option },
                        colors = RadioButtonDefaults.colors(
                            selectedColor   = PinkMain,
                            unselectedColor = PinkMain
                        )
                    )
                    Text(option)
                }
            }
        }


        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = course,
                onValueChange = {},
                readOnly = true,
                label = { Text("Курс") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                colors = pinkFieldColors,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                courses.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            course = item
                            expanded = false
                        }
                    )
                }
            }
        }


        Text("Уровень сложности: ${difficulty.toInt()}")
        Slider(
            value = difficulty,
            onValueChange = { difficulty = it },
            valueRange = 0f..10f,
            steps = 9,
            colors = SliderDefaults.colors(
                thumbColor         = PinkMain,
                activeTrackColor   = PinkMain,
                inactiveTrackColor = PinkLight,
                activeTickColor    = Color.White,
                inactiveTickColor  = PinkMain
            )
        )


        Text("Дата рождения")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = day,
                onValueChange = { input ->
                    day = input.filter { it.isDigit() }.take(2)
                    dateError = null
                },
                label = { Text("ДД") },
                singleLine = true,
                isError = dateError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = pinkFieldColors,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = month,
                onValueChange = { input ->
                    month = input.filter { it.isDigit() }.take(2)
                    dateError = null
                },
                label = { Text("ММ") },
                singleLine = true,
                isError = dateError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = pinkFieldColors,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = year,
                onValueChange = { input ->
                    year = input.filter { it.isDigit() }.take(4)
                    dateError = null
                },
                label = { Text("ГГГГ") },
                singleLine = true,
                isError = dateError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = pinkFieldColors,
                modifier = Modifier.weight(1.4f)
            )
        }

        dateError?.let {
            Text(it, color = Color.Red)
        }


        Button(
            onClick = {
                // 1. ФИО
                val cleanName = fullName.trim()
                if (cleanName.isBlank()) {
                    nameError = "Введите ФИО"
                    return@Button
                }
                if (cleanName.split(" ").size < 2) {
                    nameError = "Введите минимум фамилию и имя"
                    return@Button
                }

                // 2. Дата
                val d = day.toIntOrNull()
                val m = month.toIntOrNull()
                val y = year.toIntOrNull()

                if (d == null || m == null || y == null) {
                    dateError = "Заполните дату в формате ДД.ММ.ГГГГ"
                    return@Button
                }
                if (y < MIN_YEAR || y > MAX_YEAR) {
                    dateError = "Год должен быть от $MIN_YEAR до $MAX_YEAR"
                    return@Button
                }
                if (m !in 1..12) {
                    dateError = "Месяц должен быть от 01 до 12"
                    return@Button
                }
                if (d !in 1..31) {
                    dateError = "День должен быть от 01 до 31"
                    return@Button
                }

                val cal = Calendar.getInstance().apply {
                    setLenient(false)
                    clear()
                    set(y, m - 1, d)
                }
                val valid = try {
                    cal.time
                    true
                } catch (e: Exception) {
                    false
                }
                if (!valid) {
                    dateError = "Такой даты не существует"
                    return@Button
                }

                val today = Calendar.getInstance()
                if (cal.after(today)) {
                    dateError = "Дата рождения не может быть в будущем"
                    return@Button
                }

                val birthDateStr = String.format(
                    Locale.getDefault(),
                    "%02d.%02d.%04d", d, m, y
                )
                val zodiac = getZodiacSign(d, m)

                player = Player(
                    fullName   = cleanName,
                    gender     = gender,
                    course     = course,
                    difficulty = difficulty.toInt(),
                    birthDate  = birthDateStr,
                    zodiac     = zodiac
                )
                dateError = null
                nameError = null
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = PinkMain,
                contentColor   = PinkText
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Зарегистрировать")
        }

        player?.let { p ->
            Text(
                buildString {
                    appendLine("ФИО: ${p.fullName}")
                    appendLine("Пол: ${p.gender}")
                    appendLine("Курс: ${p.course}")
                    appendLine("Уровень сложности: ${p.difficulty}")
                    appendLine("Дата рождения: ${p.birthDate}")
                    appendLine("Знак зодиака: ${p.zodiac}")
                }
            )
        }
    }
}

private fun getZodiacSign(day: Int, month: Int): String = when (month) {
    1  -> if (day <= 19) "Козерог" else "Водолей"
    2  -> if (day <= 18) "Водолей" else "Рыбы"
    3  -> if (day <= 20) "Рыбы" else "Овен"
    4  -> if (day <= 19) "Овен" else "Телец"
    5  -> if (day <= 20) "Телец" else "Близнецы"
    6  -> if (day <= 20) "Близнецы" else "Рак"
    7  -> if (day <= 22) "Рак" else "Лев"
    8  -> if (day <= 22) "Лев" else "Дева"
    9  -> if (day <= 22) "Дева" else "Весы"
    10 -> if (day <= 22) "Весы" else "Скорпион"
    11 -> if (day <= 21) "Скорпион" else "Стрелец"
    12 -> if (day <= 21) "Стрелец" else "Козерог"
    else -> "Неизвестно"
}