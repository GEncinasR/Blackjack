package com.example.blackjack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

// los nombres no se usan, luego lo implemento de modo versus
@Entity(tableName = "game_results")
data class GameResult(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val winnerName: String,
    val loserName: String,
    val date: Long,
    val winningCards: String,
    val playerScore: Int = 0,
    val dealerScore: Int = 0
)
