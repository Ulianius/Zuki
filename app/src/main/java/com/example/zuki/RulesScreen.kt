package com.example.zuki

import android.text.Html
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.ui.viewinterop.AndroidView


// ============================================================
//  ПРАВИЛА ИГРЫ (HTML)
// ============================================================

private val RULES_HTML = """

<p>Цель игры — <b>уничтожать тараканов</b>, бегающих по экрану, и набирать очки за отведённое время.</p>

<h2>Насекомые и очки</h2>
<ul>
  <p><img src="tarakan.png"/> <b>Обычный таракан</b> — 10 очков</p>
  <p><img src="tarakan_fast.png"/><b>Жёлтый таракан</b> — 30 очков, бегает быстрее обычного</p>
  <p><img src="tarakan_rare.png"/><b>Красный таракан</b> — 100 очков, редкий и медленный</p>
  <p><img src="grecha.png"/><b>Греча</b> — 50 очков, появляется раз в несколько секунд</p>
</ul>

<h2>Промах</h2>
<p>Клик по пустому месту отнимает <b>5 очков</b> и увеличивает счётчик промахов.</p>

<h2>Раунд</h2>
<p>Длительность раунда задаётся в настройках. По истечении времени игра заканчивается, и показывается результат.</p>

<h2>Результат</h2>
<p>В конце раунда выводятся очки, попадания, промахи и точность. Можно играть снова или выйти в меню.</p>
""".trimIndent()


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Правила", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PinkMain),
                windowInsets = WindowInsets.statusBars
            )
        }
    ) { innerPadding ->                         // ← здесь появляется innerPadding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)     // ← теперь работает
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // ★ HTML-текст
            val context = LocalContext.current
            AndroidView(
                factory = { ctx ->
                    TextView(ctx).apply {
                        setPadding(0, 0, 0, 0)
                        textSize = 16f
                        setTextColor(android.graphics.Color.parseColor("#C94F7C"))
                        setLineSpacing(6f, 1f)
                    }
                },
                update = { textView ->
                    textView.text = Html.fromHtml(
                        RULES_HTML,
                        Html.FROM_HTML_MODE_COMPACT,
                        // ★ ImageGetter: превращает <img src="..."> в Drawable
                        { source ->
                            val resId = when (source) {
                                "tarakan.png"      -> R.drawable.tarakan
                                "tarakan_fast.png" -> R.drawable.tarakan_fast
                                "tarakan_rare.png" -> R.drawable.tarakan_rare
                                "grecha.png"       -> R.drawable.grecha
                                else               -> null
                            }
                            if (resId != null) {
                                val d = ContextCompat.getDrawable(context, resId)
                                // Размер картинки в пикселях: 40dp → px
                                val px = (40 * context.resources.displayMetrics.density).toInt()
                                d?.setBounds(0, 0, px, px)
                                d
                            } else null
                        },
                        null    // TagHandler не нужен
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
