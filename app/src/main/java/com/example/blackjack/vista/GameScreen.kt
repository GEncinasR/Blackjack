package com.example.blackjack.vista

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.blackjack.R
import com.example.blackjack.model.Card


@Composable
fun GameScreen(navController: NavController, viewModel: BlackjackViewModel) {
    val daydream = FontFamily(Font(R.font.daydream))

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(Strings.gameDealerLabel, fontFamily = daydream, style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Text("${Strings.gameScorePrefix}${if (viewModel.isGameOver) viewModel.calculateScore(viewModel.dealerHand) else Strings.gameUnknownScore}", fontFamily = daydream, color = Color.White)
        HandView(viewModel.dealerHand, isPlayer = false, highContrast = viewModel.highContrast)

        Spacer(modifier = Modifier.weight(1f))

        if (viewModel.isGameOver) {
            Text(viewModel.gameStatusMessage, fontFamily = daydream, fontSize = 16.sp, color = Color.Yellow)
            Box(
                modifier = Modifier
                    .padding(0.dp, 16.dp, 0.dp, 0.dp)
                    .width(320.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF4CAF50))
                    .clickable { viewModel.playButtonClick(); viewModel.resetGame() },
                contentAlignment = Alignment.Center
            ) {
                Text(Strings.gamePlayAgain, fontFamily = daydream, fontSize = 20.sp, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(Strings.gamePlayerLabel, fontFamily = daydream, style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Text("${Strings.gameScorePrefix}${if (viewModel.dealingPhase < 3) Strings.gameUnknownScore else viewModel.calculateScore(viewModel.playerHand)}", fontFamily = daydream, color = Color.White)
        HandView(viewModel.playerHand, isPlayer = true, hasHit = viewModel.hasHit, highContrast = viewModel.highContrast)

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (viewModel.isGameOver || viewModel.isDealing) Color(0xFF2E7D32) else Color(0xFF4CAF50))
                    .clickable(enabled = !viewModel.isGameOver && !viewModel.isDealing) { viewModel.hit() },
                contentAlignment = Alignment.Center
            ) {
                Text(Strings.gameHit, fontFamily = daydream, fontSize = 20.sp, color = Color.White)
            }

            Box(
                modifier = Modifier
                    .width(140.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (viewModel.isGameOver || viewModel.isDealing) Color(0xFF2E7D32) else Color(0xFF4CAF50))
                    .clickable(enabled = !viewModel.isGameOver && !viewModel.isDealing) { viewModel.playButtonClick(); viewModel.stand() },
                contentAlignment = Alignment.Center
            ) {
                Text(Strings.gameStand, fontFamily = daydream, fontSize = 12.sp, color = Color.White)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        TextButton(onClick = { viewModel.playButtonClick(); viewModel.popBackSafe(navController) }) {
            Text(Strings.gameBackToMenu, fontFamily = daydream, color = Color.White)
        }
    }
}


@Composable
fun HandView(hand: List<Card>, isPlayer: Boolean = false, hasHit: Boolean = false, highContrast: Boolean = false) {
    val cardWidth = 80.dp
    val overlap = if (isPlayer && hasHit || hand.size > 3) 60.dp else 0.dp
    val totalWidth = if (hand.isEmpty()) 0.dp
    else cardWidth + (cardWidth - overlap) * (hand.size - 1)

    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(totalWidth)
            .height(120.dp)
    ) {
        hand.forEachIndexed { index, card ->
            CardView(
                card = card,
                isPlayer = isPlayer,
                highContrast = highContrast,
                modifier = Modifier.offset(x = (cardWidth - overlap) * index)
            )
        }
    }
}


@Composable
fun CardView(card: Card, isPlayer: Boolean = false, highContrast: Boolean = false, modifier: Modifier = Modifier) {
    val imageRes = if (card.isFaceUp) {
        if (highContrast) card.highContrastDrawableRes else card.drawableRes
    } else {
        if (isPlayer) R.drawable.devplayer else R.drawable.devcroupier
    }
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = modifier.size(width = 80.dp, height = 120.dp)
    )
}
