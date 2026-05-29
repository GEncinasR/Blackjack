package com.example.blackjack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.blackjack.vista.BlackjackApp
import com.example.blackjack.vista.BlackjackViewModel
import com.example.blackjack.vista.paletteForScheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: BlackjackViewModel = viewModel()
            val scheme = viewModel.colorScheme
            val palette = paletteForScheme(scheme)

            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = palette.primary,
                    onPrimary = palette.onPrimary,
                    primaryContainer = palette.primaryContainer,
                    onPrimaryContainer = palette.onPrimaryContainer,
                    secondary = palette.secondary,
                    onSecondary = palette.onSecondary,
                    background = palette.background,
                    onBackground = palette.onBackground,
                    surface = palette.surface,
                    onSurface = palette.onSurface,
                    surfaceVariant = palette.surfaceVariant,
                    onSurfaceVariant = palette.onSurfaceVariant,
                )
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .systemBarsPadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BlackjackApp(viewModel)
                }
            }
        }
    }
}
