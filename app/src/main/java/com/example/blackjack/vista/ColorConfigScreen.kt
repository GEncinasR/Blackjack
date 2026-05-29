package com.example.blackjack.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.blackjack.R


@Composable
fun ColorConfigScreen(navController: NavController, viewModel: BlackjackViewModel) {
    val currentScheme = viewModel.colorScheme
    val proggy = FontFamily(Font(R.font.proggycleancenerdfontmonoregular))
    val daydream = FontFamily(Font(R.font.daydream))

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(Strings.colorTitle,style = MaterialTheme.typography.headlineSmall,fontFamily = daydream)
        Spacer(modifier = Modifier.height(16.dp))

        Text(Strings.colorSchemeLabel, style = MaterialTheme.typography.titleMedium,fontFamily = proggy)
        Spacer(modifier = Modifier.height(8.dp))

        Strings.colorSchemes.forEach { name ->
            val palette = paletteForScheme(name)
            val isSelected = name == currentScheme
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.colorScheme = name }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(palette.primary)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(name, modifier = Modifier.weight(1f),fontFamily = daydream)
                RadioButton(
                    selected = isSelected,
                    onClick = { viewModel.colorScheme = name }
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))



        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                viewModel.playButtonClick()
                viewModel.popBackSafe(navController)
            }
        ) {
            Text(Strings.gameBackToMenu, fontFamily = daydream, color = Color.White)
        }
    }
}
