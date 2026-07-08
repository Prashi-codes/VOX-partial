package com.prashi.vox.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.prashi.vox.navigation.Screen

data class DrawerItem(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val screen: Screen
)

val drawerItems = listOf(

    DrawerItem(
        "Home",
        Icons.Default.Home,
        Screen.Home
    ),

    DrawerItem(
        "AI Chat",
        Icons.Default.Chat,
        Screen.Chat
    ),

    DrawerItem(
        "AI Travel",
        Icons.Default.Flight,
        Screen.Travel
    ),

    DrawerItem(
        "Notes",
        Icons.Default.Edit,
        Screen.Notes
    ),

    DrawerItem(
        "Weather",
        Icons.Default.Cloud,
        Screen.Weather
    ),

    DrawerItem(
        "News",
        Icons.Default.Article,
        Screen.News
    ),

    DrawerItem(
        "Settings",
        Icons.Default.Settings,
        Screen.Settings
    )
)