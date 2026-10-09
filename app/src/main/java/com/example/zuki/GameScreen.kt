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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.draw.clipToBounds
import kotlin.math.roundToInt


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

    val bugs = remember { mutableStateListOf<Bug>() }
    var finished by remember { mutableStateOf(false) }

    // ---------- ИТОГОВЫЙ ЭКРАН ----------
    if (finished) {
        val accuracy = if (hits + misses == 0) 0
        else (hits.toFloat() / (hits + misses) * 100).roundToInt()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(PinkLight)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Раунд завершён!",
                style = MaterialTheme.typography.headlineMedium,
                color = PinkMain)

            Spacer(Modifier.height(24.dp))

            Text("Очки: $score", style = MaterialTheme.typography.titleLarge, color = PinkDark)
            Text("Попадания: $hits", style = MaterialTheme.typography.titleLarge, color = PinkDark)
            Text("Промахи: $misses", style = MaterialTheme.typography.titleLarge, color = PinkDark)
            Text("Точность: $accuracy %", style = MaterialTheme.typography.titleLarge, color = PinkDark)

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    score = 0; hits = 0; misses = 0
                    timeLeftMs = settings.roundDurationMs
                    bugs.clear(); finished = false
                },
                colors = ButtonDefaults.buttonColors(containerColor = PinkDark),
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("Играть снова", color = Color.White,
                    style = MaterialTheme.typography.titleLarge)
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onExit,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text("В меню", color = PinkMain,
                    style = MaterialTheme.typography.titleLarge)
            }
        }
        return
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Игра", color = Color.White) },
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
                            bugs.add(BugFactory.spawn(settings.speedFactor))
                            spawnAcc = 0L
                        }
                    }

                    finished = true
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


                bugs.forEach { bug ->
                    val xDp = with(LocalDensity.current) { (bug.x * fieldW).toDp() }
                    val yDp = with(LocalDensity.current) { (bug.y * fieldH).toDp() }
                    val sizeDp = with(LocalDensity.current) {
                        (bug.size * minOf(fieldW, fieldH)).toDp()
                    }

                    Image(
                        painter = painterResource(id = R.drawable.tarakan),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .offset(x = xDp, y = yDp)
                            .size(sizeDp)
                            .pointerInput(bug.id) {
                                detectTapGestures(onTap = {
                                    score += 10
                                    hits++
                                    bugs.remove(bug)
                                })
                            }
                    )
                }
            }
        }
    }
}