package com.example.zuki.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_results")
data class GameResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val playerId: Long,
    val score: Int,
    val hits: Int,
    val misses: Int,
    val accuracy: Int,
    val difficultyMultiplier: Float,
    val playedAt: Long = System.currentTimeMillis()
)