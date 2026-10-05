package com.example.zuki

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthorsScreen(onBack: () -> Unit) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Авторы", color = Color.White, textAlign = TextAlign.Center) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(" Назад", color = Color.White, fontSize = 20.sp )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PinkMain)
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // -------- Аватар в круге --------
            Box(
                modifier = Modifier
                    .size(270.dp)
                    .clip(CircleShape)
                    .background(PinkLight),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.avtor),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = 1.3f,             // ← увеличение (1.0 = 100%, 1.3 = 130%)
                            scaleY = 1.3f
                        )

                        .offset(x = (-40).dp, y = (-30).dp),          // ← сдвигаем картинку ВВЕРХ на 30dp
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // -------- Имя --------
            Text(
                text = "Корнеева Ульяна",
                style = MaterialTheme.typography.headlineSmall,
                color = PinkMain,
                textAlign = TextAlign.Center
            )

            // -------- Группа --------
            Card(
                colors = CardDefaults.cardColors(containerColor = PinkLight),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ){
                Text(
                    text = "Группа ИП-316",
                    style = MaterialTheme.typography.bodyLarge,
                    color = PinkDark,
                    textAlign = TextAlign.Left
                )

                // -------- Роль --------
                Text(
                    text = "Разработчик, дизайнер",
                    style = MaterialTheme.typography.bodyLarge,
                    color = PinkDark,
                    textAlign = TextAlign.Left
                )
                }
            }


            Spacer(modifier = Modifier.weight(1f))

            // -------- Нижняя подпись --------
            Text(
                text = "СибГУТИ · 2026",
                style = MaterialTheme.typography.bodyLarge,
                color = PinkDark,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


// ============================================================
//  СТРОКА "ПОДПИСЬ — ЗНАЧЕНИЕ"
// ============================================================

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = PinkDark
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = PinkMain
        )
    }
}

