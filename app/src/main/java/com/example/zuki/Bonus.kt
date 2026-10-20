package com.example.zuki

import kotlin.random.Random

data class Bonus(
    val id: Long,
    val x: Float,
    val y: Float,
    val size: Float,
    val points: Int,
    val startTime: Long
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