package com.example.blackjack.vista

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.blackjack.R


@Composable
fun SoundConfigScreen(navController: NavController, viewModel: BlackjackViewModel) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(Strings.soundTitle,style = MaterialTheme.typography.headlineSmall,fontFamily = daydream)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(Strings.configCardSounds)
            Switch(
                checked = viewModel.cardSounds,
                onCheckedChange = { viewModel.cardSounds = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(Strings.configSfxVolume)
        Slider(
            value = viewModel.sfxVolume,
            onValueChange = { viewModel.sfxVolume = it },
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(Strings.configMusic)
            Switch(
                checked = viewModel.music,
                onCheckedChange = { viewModel.music = it }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(Strings.configMusicVolume)
        Slider(
            value = viewModel.musicVolume,
            onValueChange = { viewModel.musicVolume = it },
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        val daydream = FontFamily(Font(R.font.daydream))

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
