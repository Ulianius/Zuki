package com.example.zuki

import kotlin.random.Random
import androidx.annotation.DrawableRes
enum class BugType(
    @DrawableRes val iconRes: Int,
    @DrawableRes val splashRes: Int,
    val points: Int,          // очки за попадание
    val speedMul: Float,      // множитель скорости
    val baseSize: Float       // базовый размер (доля от min стороны поля)
) {
    NORMAL(R.drawable.tarakan, splashRes = R.drawable.smert, points = 10, speedMul = 1.0f, baseSize = 0.18f),
    FAST  (R.drawable.tarakan_fast,  splashRes = R.drawable.smert_fast,  points = 30, speedMul = 2.8f, baseSize = 0.20f)
}
data class Bug(
    val id: Long,
    val type: BugType,
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val size: Float
){
    val points: Int get() = type.points
    val iconRes: Int get() = type.iconRes
    val splashRes: Int get() = type.splashRes
}

object BugFactory {
    private var nextId = 1L

    fun spawn(speedFactor: Float,
              fastChance: Float = 0.2f
        ): Bug {

        val type = if (Random.nextFloat() < fastChance) BugType.FAST
        else BugType.NORMAL

        // Направление — случайное (0..2π)
        val angle = Random.nextFloat() * 2f * Math.PI.toFloat()

        // Базовая скорость 0.10..0.25 * speedFactor * speedMul
        val baseSpeed = (Random.nextFloat() * 0.15f + 0.10f) * speedFactor * type.speedMul

        val vx = kotlin.math.cos(angle) * baseSpeed
        val vy = kotlin.math.sin(angle) * baseSpeed

        // Размер — с небольшим разбросом от базового
        val size = type.baseSize * 1.2f

        return Bug(
            id = nextId++,
            type = type,
            x = Random.nextFloat() * 0.8f + 0.1f,
            y = Random.nextFloat() * 0.8f + 0.1f,
            vx = vx,
            vy = vy,
            size = size
        )
    }
}