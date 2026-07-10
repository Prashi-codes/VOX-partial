package com.prashi.vox.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.prashi.vox.screens.ChatScreen
import com.prashi.vox.screens.HomeScreen
import com.prashi.vox.screens.NewsScreen
import com.prashi.vox.screens.NotesScreen
import com.prashi.vox.screens.SettingsScreen
import com.prashi.vox.screens.WeatherScreen
import com.prashi.vox.travel.TravelScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    listening: Boolean,
    conversation: String
) {

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(
                listening = listening,
                conversation = conversation
            )
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