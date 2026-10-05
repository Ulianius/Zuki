package com.example.zuki

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import java.util.Locale


// ============================================================
//  ЗОДИАК: ЗНАК → РЕСУРС
// ============================================================

@DrawableRes
fun getZodiacIcon(zodiac: String): Int = when (zodiac) {
    "Овен"     -> R.drawable.zodiac_1
    "Телец"    -> R.drawable.zodiac_2
    "Близнецы" -> R.drawable.zodiac_3
    "Рак"      -> R.drawable.zodiac_4
    "Лев"      -> R.drawable.zodiac_5
    "Дева"     -> R.drawable.zodiac_6
    "Весы"     -> R.drawable.zodiac_7
    "Скорпион" -> R.drawable.zodiac_8
    "Стрелец"  -> R.drawable.zodiac_9
    "Козерог"  -> R.drawable.zodiac_10
    "Водолей"  -> R.drawable.zodiac_11
    "Рыбы"     -> R.drawable.zodiac_12
    else       -> R.drawable.zodiac_1
}


// ============================================================
//  ЗОДИАК: ДАТА → ЗНАК
// ============================================================

private fun getZodiacSign(day: Int, month: Int): String = when (month) {
    1  -> if (day <= 19) "Козерог"  else "Водолей"
    2  -> if (day <= 18) "Водолей"  else "Рыбы"
    3  -> if (day <= 20) "Рыбы"     else "Овен"
    4  -> if (day <= 19) "Овен"     else "Телец"
    5  -> if (day <= 20) "Телец"    else "Близнецы"
    6  -> if (day <= 20) "Близнецы" else "Рак"
    7  -> if (day <= 22) "Рак"      else "Лев"
    8  -> if (day <= 22) "Лев"      else "Дева"
    9  -> if (day <= 22) "Дева"     else "Весы"
    10 -> if (day <= 22) "Весы"     else "Скорпион"
    11 -> if (day <= 21) "Скорпион" else "Стрелец"
    12 -> if (day <= 21) "Стрелец"  else "Козерог"
    else -> "Неизвестно"
}


// ============================================================
//  ФИЛЬТР ВВОДА ФИО
// ============================================================

private fun filterNameInput(input: String): String {
    val allowed = input.filter { ch ->
        ch.isLetter() || ch == ' ' || ch == '-'
    }
    return allowed.replace(Regex("\\s+"), " ")
}


// ============================================================
//  ЭКРАН "РЕГИСТРАЦИЯ"
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerFormScreen(onBack: () -> Unit) {

    var fullName   by remember { mutableStateOf("") }
    var gender     by remember { mutableStateOf("Мужской") }
    var course     by remember { mutableStateOf("1 курс") }
    var difficulty by remember { mutableFloatStateOf(5f) }

    var day   by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year  by remember { mutableStateOf("") }

    var dateError by remember { mutableStateOf<String?>(null) }
    var nameError by remember { mutableStateOf<String?>(null) }
    var player    by remember { mutableStateOf<Player?>(null) }

    val scrollState = rememberScrollState()
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

    LaunchedEffect(player) {
        if (player != null) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Регистрация игрока", color = Color.White) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Назад", color = Color.White, fontSize = 20.sp)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = PinkMain
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ---------- ФИО ----------
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
            nameError?.let { Text(it, color = Color.Red) }

            // ---------- Пол ----------
            Text("Пол")
            Row(verticalAlignment = Alignment.CenterVertically) {
                listOf("Мужской", "Женский").forEach { option ->
                    Row(
                        modifier = Modifier
                            .selectable(selected = gender == option, onClick = { gender = option })
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

            // ---------- Курс ----------
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
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    courses.forEach { item ->
                        DropdownMenuItem(
                            text = { Text(item) },
                            onClick = { course = item; expanded = false }
                        )
                    }
                }
            }

            // ---------- Сложность ----------
            Text("Уровень сложности: ${difficulty.toInt()}")
            Slider(
                value = difficulty,
                onValueChange = { difficulty = it },
                valueRange = 1f..10f,
                steps = 9,
                colors = SliderDefaults.colors(
                    thumbColor         = PinkMain,
                    activeTrackColor   = PinkMain,
                    inactiveTrackColor = PinkLight,
                    activeTickColor    = Color.White,
                    inactiveTickColor  = PinkMain
                )
            )

            // ---------- Дата ----------
            Text("Дата рождения")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = day,
                    onValueChange = { day = it.filter { c -> c.isDigit() }.take(2); dateError = null },
                    label = { Text("ДД") },
                    singleLine = true,
                    isError = dateError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = pinkFieldColors,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = month,
                    onValueChange = { month = it.filter { c -> c.isDigit() }.take(2); dateError = null },
                    label = { Text("ММ") },
                    singleLine = true,
                    isError = dateError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = pinkFieldColors,
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = year,
                    onValueChange = { year = it.filter { c -> c.isDigit() }.take(4); dateError = null },
                    label = { Text("ГГГГ") },
                    singleLine = true,
                    isError = dateError != null,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = pinkFieldColors,
                    modifier = Modifier.weight(1.4f)
                )
            }
            dateError?.let { Text(it, color = Color.Red) }

            // ---------- Кнопка "Зарегистрировать" ----------
            Button(
                onClick = {
                    val cleanName = fullName.trim()
                    if (cleanName.isBlank()) { nameError = "Введите ФИО"; return@Button }
                    if (cleanName.split(" ").size < 2) { nameError = "Введите минимум фамилию и имя"; return@Button }

                    val d = day.toIntOrNull()
                    val m = month.toIntOrNull()
                    val y = year.toIntOrNull()
                    if (d == null || m == null || y == null) { dateError = "Заполните дату"; return@Button }
                    if (y < MIN_YEAR || y > MAX_YEAR) { dateError = "Год от $MIN_YEAR до $MAX_YEAR"; return@Button }
                    if (m !in 1..12) { dateError = "Месяц 01..12"; return@Button }
                    if (d !in 1..31) { dateError = "День 01..31"; return@Button }

                    val cal = Calendar.getInstance().apply {
                        setLenient(false); clear(); set(y, m - 1, d)
                    }
                    val valid = try { cal.time; true } catch (e: Exception) { false }
                    if (!valid) { dateError = "Такой даты не существует"; return@Button }
                    if (cal.after(Calendar.getInstance())) { dateError = "Дата не в будущем"; return@Button }

                    val birthDateStr = String.format(Locale.getDefault(), "%02d.%02d.%04d", d, m, y)
                    player = Player(
                        fullName   = cleanName,
                        gender     = gender,
                        course     = course,
                        difficulty = difficulty.toInt(),
                        birthDate  = birthDateStr,
                        zodiac     = getZodiacSign(d, m)
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

            // ---------- Результат ----------
            player?.let { p ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Информация о пользователе",
                        style = MaterialTheme.typography.titleMedium,
                        color = PinkMain
                    )

                    Text(
                        buildString {
                            appendLine("ФИО: ${p.fullName}")
                            appendLine("Пол: ${p.gender}")
                            appendLine("Курс: ${p.course}")
                            appendLine("Уровень сложности: ${p.difficulty}")
                            appendLine("Дата рождения: ${p.birthDate}")
                        }
                    )

                    Text(
                        text = p.zodiac,
                        style = MaterialTheme.typography.headlineSmall,
                        color = PinkMain
                    )

                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(PinkLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = getZodiacIcon(p.zodiac)),
                            contentDescription = p.zodiac,
                            modifier = Modifier.size(110.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}