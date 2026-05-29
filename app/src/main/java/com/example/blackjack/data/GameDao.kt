package com.example.blackjack.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Insert
    suspend fun insertResult(result: GameResult)

    @Query("SELECT * FROM game_results ORDER BY date DESC")
    fun getAllResults(): Flow<List<GameResult>>
}
