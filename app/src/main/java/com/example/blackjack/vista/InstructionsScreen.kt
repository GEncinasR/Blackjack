package com.example.blackjack.vista

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.blackjack.R
val proggy = FontFamily(Font(R.font.proggycleancenerdfontmonoregular))
val daydream = FontFamily(Font(R.font.daydream))

@Composable
fun InstructionsScreen(navController: NavController, viewModel: BlackjackViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(Strings.instructionsTitle, style = MaterialTheme.typography.headlineSmall,fontFamily = daydream)
        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Text(Strings.instructionsText,fontFamily = proggy,fontSize = 24.sp)
        }

        TextButton(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = { viewModel.playButtonClick(); viewModel.popBackSafe(navController) }) {
            Text(Strings.gameBackToMenu, fontFamily = daydream, color = Color.White)
        }
    }
}
