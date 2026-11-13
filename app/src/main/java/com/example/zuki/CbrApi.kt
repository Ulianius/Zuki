package com.example.zuki

import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object CbrClient {
    suspend fun fetchGoldRate(): Double = withContext(Dispatchers.IO) {
        try {
            val fmt = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val today = fmt.format(Date())
            val weekAgo = fmt.format(Date(System.currentTimeMillis() - 7L * 24 * 3600 * 1000))

            val urlStr = "https://www.cbr.ru/scripts/xml_metall.asp?date_req1=$weekAgo&date_req2=$today"
            val url = URL(urlStr)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.setRequestProperty("User-Agent", "Mozilla/5.0")

            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val xmlString = connection.inputStream.bufferedReader().use { it.readText() }

                val goldRegex = Regex("""Code="1".*?<Buy>([\d,]+)</Buy>""", RegexOption.DOT_MATCHES_ALL)
                val matches = goldRegex.findAll(xmlString)

                val lastMatch = matches.lastOrNull()

                if (lastMatch != null) {
                    val rateString = lastMatch.groupValues[1].replace(",", ".")
                    return@withContext rateString.toDoubleOrNull() ?: 0.0
                }
            }
            return@withContext 0.0
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext 0.0
        }
    }
}