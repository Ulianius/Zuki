package com.example.zuki

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.zuki.data.AppDao
import com.example.zuki.data.TopScore
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import kotlin.math.roundToInt
import androidx.compose.material3.Surface
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordsScreen(dao: AppDao) {
    var topScores by remember { mutableStateOf<List<TopScore>>(emptyList()) }

    LaunchedEffect(Unit) {
        topScores = dao.getTopScores()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Рекорды", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PinkMain),
                windowInsets = WindowInsets.statusBars
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PinkLight)
                .padding(16.dp)
        ) {
            if (topScores.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Пока нет рекордов. Сыграйте партию!", color = PinkDark)
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(topScores) { index, item ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Левая часть: Место и Имя
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${index + 1}.",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = PinkMain,
                                        modifier = Modifier.width(32.dp)
                                    )
                                    Text(
                                        text = item.playerName,
                                        fontSize = 16.sp,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val cleanScore = if (item.difficultyMultiplier > 0f) {
                                        (item.score / item.difficultyMultiplier).roundToInt()
                                    } else {
                                        item.score
                                    }

                                    Text(
                                        text = "$cleanScore",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = PinkDark
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "очков",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // Плашка со сложностью
                                    Surface(
                                        color = PinkMain.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = "x${String.format(Locale.getDefault(), "%.1f", item.difficultyMultiplier)}",
                                            color = PinkDark,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}