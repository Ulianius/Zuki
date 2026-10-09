package com.example.zuki

data class GameSettings(
    val speed: Int = 5,
    val maxRoaches: Int = 10,
    val bonusIntervalSec: Int = 10,
    val roundDurationSec: Int = 60
) {
    val speedFactor: Float get() = speed / 5f
    val roundDurationMs: Long get() = roundDurationSec * 1000L
    val bonusIntervalMs: Long get() = bonusIntervalSec * 1000L
}