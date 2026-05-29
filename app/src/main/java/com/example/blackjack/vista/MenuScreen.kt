package com.example.blackjack.vista

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.blackjack.R

@Composable
fun MenuScreen(navController: NavController, viewModel: BlackjackViewModel) {
    if (viewModel.isFirstRun) {
        AlertDialog(
            onDismissRequest = { viewModel.isFirstRun = false },
            confirmButton = {
                TextButton(onClick = { viewModel.isFirstRun = false }) {
                    Text("OK", color = Color.Black)
                }
            },
            title = { Text("Hola amigos", color = Color.Black, fontFamily = proggy) },
            text = {
                Text("Muchas gracias por instalar mi juego, espero lo disfruten." +
                        "\nLos invito a revisar los ajutes y activar la música.",
                    color = Color.Black,fontFamily = proggy,fontSize = 20.sp)
            }
        )
    }

    if (viewModel.legacyMode) {
        LegacyMenu(navController, viewModel)
    } else {
        NewMenu(navController, viewModel)
    }
}

@Composable
private fun LegacyMenu(navController: NavController, viewModel: BlackjackViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = Strings.appTitle, fontSize = 32.sp, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.button),
            contentDescription = Strings.menuCdPlay,
            modifier = Modifier.clickable { viewModel.playButtonClick(); viewModel.resetGame(); viewModel.navigateSafe(navController, "game") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.scores),
                contentDescription = Strings.scoreboardTitle,
            modifier = Modifier.clickable { viewModel.playButtonClick(); viewModel.navigateSafe(navController, "scoreboard") }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.instruct),
                contentDescription = Strings.instructionsTitle,
                modifier = Modifier.clickable { viewModel.playButtonClick(); navController.navigate("instructions") }
            )
            Image(
                painter = painterResource(id = R.drawable.config),
                contentDescription = Strings.configTitle,
                modifier = Modifier.clickable { viewModel.playButtonClick(); navController.navigate("config") }
            )
        }
    }
}

@Composable
private fun NewMenu(navController: NavController, viewModel: BlackjackViewModel) {
    val buttonColor = Color(0xFFEB4141)
    val borderColor = Color(0xFF8B3A3A)
    val daydream = FontFamily(Font(R.font.daydream))

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = Strings.appTitle,
            fontSize = 32.sp,
            fontFamily = daydream,
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .width(180.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(buttonColor)
                .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                .clickable { viewModel.playButtonClick(); viewModel.resetGame(); navController.navigate("game") },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = Strings.menuJugar,
                fontFamily = daydream,
                fontSize = 24.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .width(180.dp)
                .height(56.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(buttonColor)
                .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                .clickable { viewModel.playButtonClick(); navController.navigate("scoreboard") },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = Strings.menuScores,
                fontFamily = daydream,
                fontSize = 24.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(buttonColor)
                    .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                    .clickable { viewModel.playButtonClick(); viewModel.navigateSafe(navController, "instructions") },
                contentAlignment = Alignment.Center
            ) {
                Text(
                text = Strings.menuHelp,
                fontFamily = daydream,
                    fontSize = 28.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }

            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(buttonColor)
                    .border(2.dp, borderColor, RoundedCornerShape(8.dp))
                    .clickable { viewModel.playButtonClick(); viewModel.navigateSafe(navController, "config") },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gear),
                    contentDescription = Strings.configTitle
                )
            }
        }
    }
}
