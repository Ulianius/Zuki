package com.example.zuki.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AppDao {
    @Insert
    suspend fun insertPlayer(player: PlayerEntity): Long

    @Query("SELECT * FROM players ORDER BY createdAt DESC LIMIT 1")
    suspend fun getLastPlayer(): PlayerEntity?

    @Insert
    suspend fun insertResult(result: GameResultEntity): Long

    @Query("SELECT * FROM game_results ORDER BY playedAt DESC")
    suspend fun getAllResults(): List<GameResultEntity>

    @Query("SELECT * FROM players ORDER BY createdAt DESC")
    suspend fun getAllPlayers(): List<PlayerEntity>

    @Query("DELETE FROM players WHERE id = :playerId")
    suspend fun deletePlayerById(playerId: Long)

    @Query("DELETE FROM game_results WHERE playerId = :playerId")
    suspend fun deleteResultsByPlayerId(playerId: Long)
}