package com.prashi.vox.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Note
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.padding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoxDrawer(
    navController: NavHostController,
    content: @Composable () -> Unit
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet {

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "VOX",
                    style = MaterialTheme.typography.headlineMedium
                )

                HorizontalDivider()

                NavigationDrawerItem(
                    label = { Text("Home") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        navController.navigate("home")
                    },
                    icon = { Icon(Icons.Default.Home, null) }
                )

                NavigationDrawerItem(
                    label = { Text("AI Chat") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("chat")
                    },
                    icon = { Icon(Icons.Default.Chat, null) }
                )

                NavigationDrawerItem(
                    label = { Text("AI Travel") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("travel")
                    },
                    icon = { Icon(Icons.Default.Flight, null) }
                )

                NavigationDrawerItem(
                    label = { Text("Notes") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("notes")
                    },
                    icon = { Icon(Icons.Default.Note, null) }
                )

                NavigationDrawerItem(
                    label = { Text("Weather") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("weather")
                    },
                    icon = { Icon(Icons.Default.WbSunny, null) }
                )

                NavigationDrawerItem(
                    label = { Text("News") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("news")
                    },
                    icon = { Icon(Icons.Default.Newspaper, null) }
                )

                NavigationDrawerItem(
                    label = { Text("Settings") },
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        navController.navigate("settings")
                    },
                    icon = { Icon(Icons.Default.Settings, null) }
                )
            }
        }
    ) {

        Scaffold(

            topBar = {

                TopAppBar(

                    title = {
                        Text("VOX")
                    },

                    navigationIcon = {

                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {

                            Icon(Icons.Default.Menu, null)

                        }

                    }

                )

            }

        ) { padding ->

            Surface(
                modifier = Modifier.padding(padding)
            ) {
                content()
            }

        }

    }

}