package com.example.zuki

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(settings: GameSettings,
                   onSettingsChange: (GameSettings) -> Unit) {


    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Настройки игры", color = Color.White) },
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {


            SliderSetting(
                title = "Скорость игры",
                value = settings.speed.toFloat(),
                onValueChange = {
                    onSettingsChange(settings.copy(speed = it.toInt()))
                },
                valueRange = 1f..10f,
                steps = 8,                      // 10 позиций: 1,2,...,10
                valueLabel = "${settings.speed}"
            )

            SliderSetting(
                title = "Максимум тараканов на экране",
                value = settings.maxRoaches.toFloat(),
                onValueChange = {
                    onSettingsChange(settings.copy(maxRoaches = it.toInt()))
                },
                valueRange = 1f..50f,
                steps = 10,                     // 30 позиций: 1..30
                valueLabel = "${settings.maxRoaches}"
            )


            SliderSetting(
                title = "Интервал появления бонусов",
                value = settings.bonusIntervalSec.toFloat(),
                onValueChange = {
                    onSettingsChange(settings.copy(bonusIntervalSec = it.toInt()))
                },
                valueRange = 1f..30f,
                steps = 6,
                valueLabel = "${settings.bonusIntervalSec} сек"
            )


            SliderSetting(
                title = "Длительность раунда",
                value = settings.roundDurationSec.toFloat(),
                onValueChange = {
                    onSettingsChange(settings.copy(roundDurationSec = it.toInt()))
                },
                valueRange = 30f..180f,
                steps = 6,
                valueLabel = "${settings.roundDurationSec} сек"
            )

            Spacer(modifier = Modifier.height(8.dp))


            Button(
                onClick = {
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PinkMain,
                    contentColor   = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Сохранить", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}



@Composable
private fun SliderSetting(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    valueLabel: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = PinkDark
            )
            Text(
                text = valueLabel,
                style = MaterialTheme.typography.titleMedium,
                color = PinkMain
            )
        }

        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = steps,
            colors = SliderDefaults.colors(
                thumbColor         = PinkMain,
                activeTrackColor   = PinkMain,
                inactiveTrackColor = PinkLight,
                activeTickColor    = Color.White,
                inactiveTickColor  = PinkMain
            )
        )
    }
}

