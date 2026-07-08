package com.prashi.vox.navigation

sealed class Screen(val title: String) {

    object Home : Screen("Home")

    object Chat : Screen("AI Chat")

    object Travel : Screen("AI Travel")

    object Notes : Screen("Notes")

    object Weather : Screen("Weather")

    object News : Screen("News")

    object Settings : Screen("Settings")
}