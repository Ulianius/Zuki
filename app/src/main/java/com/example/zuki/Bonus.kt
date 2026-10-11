package com.example.zuki

import kotlin.random.Random

// Бонус-очки: монетка, которая появляется на поле и даёт очки за клик
data class Bonus(
    val id: Long,
    val x: Float,       // центр по X (0..1)
    val y: Float,       // центр по Y
    val size: Float,    // размер (доля от min стороны поля)
    val points: Int,    // сколько очков даёт
    val startTime: Long // момент появления
)

object BonusFactory {
    private var nextId = 1L

    fun spawn(speedFactor: Float): Bonus {
        val size = 0.3f
        val half = size / 2f

        return Bonus(
            id        = nextId++,
            x         = Random.nextFloat() * (1f - 2 * half) + half,
            y         = Random.nextFloat() * (1f - 2 * half) + half,
            size      = size,
            points    = 50,
            startTime = System.currentTimeMillis()
        )
    }
}