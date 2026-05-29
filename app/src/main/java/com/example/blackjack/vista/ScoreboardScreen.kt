package com.example.blackjack.vista

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.blackjack.R
import com.example.blackjack.data.AppDatabase
import com.example.blackjack.model.Card
import com.example.blackjack.model.Rank
import com.example.blackjack.model.Suit
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ScoreboardScreen(navController: NavController, viewModel: BlackjackViewModel) {
    val daydream = FontFamily(Font(R.font.daydream))
    val proggy = FontFamily(Font(R.font.proggycleancenerdfontmonoregular))
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val results by db.gameDao().getAllResults().collectAsState(initial = emptyList())



    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(Strings.scoreboardTitle, style = MaterialTheme.typography.headlineSmall, fontFamily = daydream)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(results) { result ->
                val isPlayerWin = result.winnerName == "Player" //no se usa el nombre como tal
                val isTie = result.playerScore == result.dealerScore
                val cardColor = when {
                    isTie -> Color(0xFFF0E130)
                    isPlayerWin -> Color(0xFF4CAF50)
                    else -> Color(0xFFE53935)
                }
                val textColor = when {
                    isTie -> Color.Black
                    else -> Color.White
                }

                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = cardColor
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            parseHand(result.winningCards).forEach { card ->
                                val cardRes = if (viewModel.highContrast) card?.highContrastDrawableRes ?: R.drawable.devblank else card?.drawableRes ?: R.drawable.devblank
                                Image(
                                    painter = painterResource(id = cardRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(width = 60.dp, height = 90.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = formatDate(result.date),
                                style = MaterialTheme.typography.bodyMedium,
                                color = textColor,
                                fontFamily = proggy

                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = "${result.playerScore} // ${result.dealerScore}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = textColor,
                                fontFamily = proggy
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = formatTime(result.date),
                                style = MaterialTheme.typography.bodyMedium,
                                color = textColor,
                                fontFamily = proggy
                            )
                        }
                    }
                }
            }
        }

        val daydream = FontFamily(Font(R.font.daydream))

        TextButton(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = { viewModel.playButtonClick(); viewModel.popBackSafe(navController) }) {
            Text(Strings.gameBackToMenu, fontFamily = daydream, color = Color.White)
        }
    }
}


private fun parseHand(cardsString: String): List<Card?> {
    return cardsString.split(", ").map { cardStr ->
        val parts = cardStr.trim().split(" of ")
        if (parts.size == 2) {
            try {
                Card(Rank.valueOf(parts[0]), Suit.valueOf(parts[1]))
            } catch (_: IllegalArgumentException) {
                null
            }
        } else null
    }
}

private fun formatDate(timestamp: Long): String {
    val date = Date(timestamp)
    return SimpleDateFormat("dd/MM", Locale.getDefault()).format(date)
}

private fun formatTime(timestamp: Long): String {
    val date = Date(timestamp)
    return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
}
