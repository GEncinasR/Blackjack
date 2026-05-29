package com.example.blackjack.vista

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.blackjack.R





@Composable
fun ConfigScreen(navController: NavController, viewModel: BlackjackViewModel) {
    var textValue by remember { mutableStateOf(viewModel.targetValue.toString()) }
    val daydream = FontFamily(Font(R.font.daydream))
    val proggy = FontFamily(Font(R.font.proggycleancenerdfontmonoregular))
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(Strings.configTitle, style = MaterialTheme.typography.headlineSmall,fontFamily = daydream)
        Spacer(modifier = Modifier.height(16.dp))

        Text(Strings.configTargetScore,fontFamily = proggy)
        Spacer(modifier = Modifier.height(4.dp))

        OutlinedTextField(
            value = textValue,
            onValueChange = { textValue = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(Strings.configHighContrast,fontFamily = proggy)
            Switch(
                checked = viewModel.highContrast,
                onCheckedChange = { viewModel.highContrast = it }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        /* legacy escondido, nomás se mostró en clase
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(Strings.configLegacyMode,fontFamily = proggy)
            Switch(
                checked = viewModel.legacyMode,
                onCheckedChange = { viewModel.legacyMode = it }
            )
        }
        */

        Spacer(modifier = Modifier.height(24.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(200.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    viewModel.playButtonClick()
                    viewModel.navigateSafe(navController, "sound_config")
                },
            contentAlignment = Alignment.Center
        ) {
            Text(Strings.configSound, fontFamily = daydream, fontSize = 20.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .width(200.dp)
                .height(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.primary)
                .clickable {
                    viewModel.playButtonClick()
                    viewModel.navigateSafe(navController, "color_config")
                },
            contentAlignment = Alignment.Center
        ) {
            Text(Strings.configColors, fontFamily = daydream, fontSize = 20.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = {
                val newValue = textValue.toIntOrNull()
                if (newValue != null && newValue in 11..50) {
                    viewModel.targetValue = newValue
                }
                viewModel.playButtonClick()
                viewModel.popBackSafe(navController)
            }
        ) {
            Text(Strings.gameBackToMenu, fontFamily = daydream, color = Color.White)
        }
    }
}
