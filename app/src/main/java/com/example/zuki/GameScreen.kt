package com.example.zuki

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.draw.clipToBounds
import kotlin.math.roundToInt
import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.graphicsLayer

private data class Splash(
    val id: Long,
    @DrawableRes val splashRes: Int,
    val x: Float,
    val y: Float,
    val size: Float,
    val startTime: Long
)
private var splashIdCounter = 0L
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    settings: GameSettings,
    onExit: () -> Unit
) {

    var score  by remember { mutableIntStateOf(0) }
    var hits   by remember { mutableIntStateOf(0) }
    var misses by remember { mutableIntStateOf(0) }
    var timeLeftMs by remember { mutableLongStateOf(settings.roundDurationMs) }
    var roundNumber by remember { mutableIntStateOf(0) }

    val bugs = remember { mutableStateListOf<Bug>() }
    val splashes = remember { mutableStateListOf<Splash>() }
    var finished by remember { mutableStateOf(false) }

    // ---------- ИТОГОВЫЙ ЭКРАН ----------
    if (finished) {
        val accuracy = if (hits + misses == 0) 0
        else (hits.toFloat() / (hits + misses) * 100).roundToInt()

        val heroImage: Int = remember(roundNumber, accuracy) {
            val heroIndex = roundNumber % 3                  // 0, 1, 2 по кругу
            val isAngry   = accuracy < 30                    // < 30% — агрессивный

            when (heroIndex) {
                0 -> if (isAngry) R.drawable.hero_1_sad else R.drawable.hero_1_happy
                1 -> if (isAngry) R.drawable.hero_2_sad else R.drawable.hero_2_happy
                else -> if (isAngry) R.drawable.hero_3_sad else R.drawable.hero_3_happy
            }
        }
        val quote = when {
            accuracy >= 80 -> "Отличная работа! Так держать!"
            accuracy >= 50 -> "Неплохо. Можешь ещё лучше!"
            accuracy >= 30 -> "Так себе. Подтянись!"
            else           -> "Это провал. Соберись!"
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PinkLight)
        ) {
            val heroOffsetX = if (heroImage == R.drawable.hero_3_happy ||
                heroImage == R.drawable.hero_3_sad) (-10).dp else (-40).dp
            Image(
                painter = painterResource(id = heroImage),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.95f)           // 85% ширины
                    .graphicsLayer(
                        scaleX = if (heroImage == R.drawable.hero_3_happy ||
                            heroImage == R.drawable.hero_3_sad) 3f else 1f,
                        scaleY = if (heroImage == R.drawable.hero_3_happy ||
                            heroImage == R.drawable.hero_3_sad) 3f else 1f

                    )
                    .offset(x = heroOffsetX)
                    .align(Alignment.BottomStart)      // внизу слева — как на макете
            )

            // ---------- КАРТОЧКИ И КНОПКИ: справа, ПОВЕРХ персонажа ----------
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(y = 80.dp)
                    .fillMaxWidth(0.55f)               // 55% ширины — перекрытие с картинкой
                    .verticalScroll(rememberScrollState())
                    .padding(
                        top = 24.dp,
                        bottom = 24.dp,
                        end = 16.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // ---- Цитата ----
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = quote,
                        color = PinkMain,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // ---- Статистика ----
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Очки: $score", color = PinkMain,
                            style = MaterialTheme.typography.titleMedium)
                        Text("Промахи: $misses", color = PinkMain,
                            style = MaterialTheme.typography.titleMedium)
                        Text("Попадания: $hits", color = PinkMain,
                            style = MaterialTheme.typography.titleMedium)
                        Text("Точность: $accuracy %", color = PinkMain,
                            style = MaterialTheme.typography.titleMedium)
                    }
                }

                // ---- Кнопка "Играть снова" ----
                Button(
                    onClick = {
                        score = 0; hits = 0; misses = 0
                        timeLeftMs = settings.roundDurationMs
                        bugs.clear(); finished = false
                        splashes.clear()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PinkDark,
                        contentColor   = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("Играть снова", style = MaterialTheme.typography.titleLarge)
                }

                // ---- Кнопка "В меню" ----
                Button(
                    onClick = onExit,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PinkMain.copy(alpha = 0.6f),
                        contentColor   = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("В меню", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
        return
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    TextButton(onClick = onExit) {
                        Text("← Выйти", color = Color.White, fontSize = 20.sp)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PinkMain),
                windowInsets = WindowInsets.statusBars
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // HUD
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PinkLight)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Очки: $score", color = PinkDark,
                    style = MaterialTheme.typography.titleMedium)
                Text("Время: ${(timeLeftMs / 1000).toInt()} с", color = PinkDark,
                    style = MaterialTheme.typography.titleMedium)
                Text("Промахи: $misses", color = PinkDark,
                    style = MaterialTheme.typography.titleMedium)
            }

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFF0F5))
                    .clipToBounds()
            ) {
                val fieldW = constraints.maxWidth.toFloat()
                val fieldH = constraints.maxHeight.toFloat()


                LaunchedEffect(Unit) {
                    val frame = 16L
                    var spawnAcc = 0L
                    val spawnInterval = 1000L             // новый таракан раз в секунду

                    while (timeLeftMs > 0) {
                        delay(frame)
                        timeLeftMs -= frame

                        // двигаем
                        for (i in bugs.indices) {
                            val b = bugs[i]
                            bugs[i] = b.copy(
                                x = b.x + b.vx * (frame / 1000f),
                                y = b.y + b.vy * (frame / 1000f)
                            )
                        }

                        // убираем ушедших за край
                        bugs.removeAll { b ->
                            b.x < -0.3f || b.x > 1.3f || b.y < -0.3f || b.y > 1.3f
                        }

                        // спавним нового, если не превышен лимит из настроек
                        spawnAcc += frame
                        if (spawnAcc >= spawnInterval &&
                            bugs.size < settings.maxRoaches) {
                            bugs.add(BugFactory.spawn(settings.speedFactor, fastChance = 0.2f))
                            spawnAcc = 0L
                        }
                    }

                    roundNumber++
                    finished = true
                }
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(50)
                        val now = System.currentTimeMillis()
                        splashes.removeAll { now - it.startTime > 500L }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                misses++
                                score -= 5
                            })
                        }
                )
                splashes.forEach { splash ->

                    val sizePx = splash.size * minOf(fieldW, fieldH)


                    val shiftPx = -sizePx * 0.375f / 2f
                    val shiftDp = with(LocalDensity.current) { shiftPx.toDp() }

                    val xDp = with(LocalDensity.current) {
                        (splash.x * fieldW).toDp()
                    } + shiftDp
                    val yDp = with(LocalDensity.current) {
                        (splash.y * fieldH).toDp()
                    } + shiftDp

                    val sizeDp = with(LocalDensity.current) {
                        (splash.size * minOf(fieldW, fieldH)).toDp()
                    }

                    // плавное затухание: 0 мс — alpha=1, 500 мс — alpha=0
                    val elapsed = System.currentTimeMillis() - splash.startTime
                    val alpha = (1f - elapsed / 500f).coerceIn(0f, 1f)

                    Image(
                        painter = painterResource(id = splash.splashRes),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .offset(x = xDp, y = yDp)
                            .size(sizeDp)
                            .graphicsLayer(alpha = alpha)
                    )
                }


                bugs.forEach { bug ->
                    val xDp = with(LocalDensity.current) { (bug.x * fieldW).toDp() }
                    val yDp = with(LocalDensity.current) { (bug.y * fieldH).toDp() }
                    val sizeDp = with(LocalDensity.current) {
                        (bug.size * minOf(fieldW, fieldH)).toDp()
                    }

                    Image(
                        painter = painterResource(id = bug.iconRes),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .offset(x = xDp, y = yDp)
                            .size(sizeDp)
                            .pointerInput(bug.id) {
                                detectTapGestures(onTap = {

                                    score += bug.points
                                    hits++

                                    splashes.add(
                                        Splash(
                                            id        = splashIdCounter++,
                                            splashRes  = bug.splashRes,
                                            x         = bug.x,
                                            y         = bug.y,
                                            size      = bug.size * 1.6f,
                                            startTime = System.currentTimeMillis()
                                        )
                                    )

                                    bugs.removeAll { it.id == bug.id }
                                })
                            }
                    )
                }
            }
        }
    }
}