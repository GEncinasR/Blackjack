package com.example.blackjack.vista

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun BlackjackApp(viewModel: BlackjackViewModel) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    LaunchedEffect(currentRoute) {
        currentRoute?.let { viewModel.onScreenChanged(it) }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_STOP -> viewModel.pauseMusic()
                Lifecycle.Event.ON_START -> viewModel.resumeMusic()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    NavHost(navController = navController, startDestination = "menu") {
        composable("menu") { MenuScreen(navController, viewModel) }
        composable("game") { GameScreen(navController, viewModel) }
        composable("scoreboard") { ScoreboardScreen(navController, viewModel) }
        composable("instructions") { InstructionsScreen(navController, viewModel) }
        composable("config") { ConfigScreen(navController, viewModel) }
        composable("sound_config") { SoundConfigScreen(navController, viewModel) }
        composable("color_config") { ColorConfigScreen(navController, viewModel) }
    }
}
