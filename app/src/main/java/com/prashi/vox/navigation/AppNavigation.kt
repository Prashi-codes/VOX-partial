package com.prashi.vox.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.prashi.vox.screens.*

@Composable
fun AppNavigation(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen()
        }

        composable("chat") {
            ChatScreen()
        }

        composable("travel") {
            TravelScreen()
        }

        composable("notes") {
            NotesScreen()
        }

        composable("weather") {
            WeatherScreen()
        }

        composable("news") {
            NewsScreen()
        }

        composable("settings") {
            SettingsScreen()
        }
    }
}