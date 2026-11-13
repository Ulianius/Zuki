package com.example.zuki

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class GoldWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            // Берем курс
            val rate = CbrClient.fetchGoldRate()

            val views = RemoteViews(context.packageName, R.layout.widget_gold)

            // Форматируем курс
            val displayRate = if (rate > 0) String.format(Locale.US, "%.2f", rate) else "Нет сети"
            views.setTextViewText(R.id.widget_gold_rate, displayRate)

            // Считаем очки для жука (делим на 10)
            val bugPoints = if (rate > 0) (rate / 10).toInt() else 100
            views.setTextViewText(R.id.widget_bug_points, "Жук: +$bugPoints оч.") // <-- Выводим очки

            // Обновляем виджет
            for (id in appWidgetIds) {
                appWidgetManager.updateAppWidget(id, views)
            }
            pendingResult.finish()
        }
    }
}