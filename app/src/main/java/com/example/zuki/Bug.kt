package com.example.zuki

import kotlin.random.Random
import androidx.annotation.DrawableRes
enum class BugType(
    @DrawableRes val iconRes: Int,
    @DrawableRes val splashRes: Int,
    val points: Int,
    val speedMul: Float,
    val baseSize: Float,
    val chance: Float
) {
    NORMAL(R.drawable.tarakan, splashRes = R.drawable.smert, points = 10, speedMul = 1.0f, baseSize = 0.18f, chance    = 0.50f),
    FAST  (R.drawable.tarakan_fast,  splashRes = R.drawable.smert_fast,  points = 30, speedMul = 2.8f, baseSize = 0.20f, chance    = 0.35f),
    RARE (iconRes = R.drawable.tarakan_rare, splashRes = R.drawable.smert_rare,points    = 100, speedMul  = 1.0f, baseSize  = 0.22f,chance    = 0.15f  )
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

        val type = pickType()

        val angle = Random.nextFloat() * 2f * Math.PI.toFloat()

        val baseSpeed = (Random.nextFloat() * 0.15f + 0.10f) * speedFactor * type.speedMul

        val vx = kotlin.math.cos(angle) * baseSpeed
        val vy = kotlin.math.sin(angle) * baseSpeed

        val size = type.baseSize * 1.2f


        val halfSize = size / 2f
        val minPos   = halfSize
        val maxPos   = 1f - halfSize

        return Bug(
            id   = nextId++,
            type = type,
            x    = Random.nextFloat() * (maxPos - minPos) + minPos,
            y    = Random.nextFloat() * (maxPos - minPos) + minPos,
            vx   = vx,
            vy   = vy,
            size = size
        )

    }
    private fun pickType(): BugType {
        val r = Random.nextFloat()
        var acc = 0f
        for (t in BugType.values()) {
            acc += t.chance
            if (r < acc) return t
        }
        return BugType.NORMAL
    }
}