package com.example.zuki

import kotlin.random.Random

data class Bug(
    val id: Long,
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val size: Float
)

object BugFactory {
    private var nextId = 1L

    fun spawn(speedFactor: Float): Bug {
        // Заходим с любого края экрана
        val fromLeft = Random.nextBoolean()
        val fromTop  = Random.nextBoolean()
        val x = if (fromLeft) -0.05f else 1.05f
        val y = if (fromTop)  -0.05f else 1.05f

        val dirX = if (fromLeft)  1f else -1f
        val dirY = if (fromTop)   1f else -1f


        val baseSpeed = Random.nextFloat() * 0.15f + 0.10f
        val speed = baseSpeed * speedFactor


        val angle = Random.nextFloat() * 0.6f - 0.3f

        return Bug(
            id = nextId++,
            x = x, y = y,
            vx = dirX * speed * kotlin.math.cos(angle),
            vy = dirY * speed * kotlin.math.sin(angle),
            size = 0.18f + Random.nextFloat() * 0.07f   // 0.10..0.15
        )
    }
}