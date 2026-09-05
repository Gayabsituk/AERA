package com.example.activity.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Outlined.Home)
    object Activity : Screen("activity", "Activity", Icons.Outlined.Eco)
    object Analytics : Screen("analytics", "Analytics", Icons.Outlined.ShowChart)
    object Goals : Screen("goals", "Goals", Icons.Outlined.TrackChanges)
    object Settings : Screen("settings", "Settings", Icons.Outlined.Settings)
}

@Composable
fun AppNavigation(viewModel: ActivityViewModel) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            
            // Show bottom bar only on main screens
            val showBottomBar = currentDestination?.route in listOf(
                Screen.Home.route, Screen.Activity.route, Screen.Analytics.route, 
                Screen.Goals.route, Screen.Settings.route
            )

            if (showBottomBar) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    val screens = listOf(Screen.Home, Screen.Activity, Screen.Analytics, Screen.Goals, Screen.Settings)
                    screens.forEach { screen ->
                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = null, tint = if (selected) Color(0xFF2D5D45) else Color.Gray) },
                            label = { Text(screen.label, color = if (selected) Color(0xFF2D5D45) else Color.Gray) },
                            selected = selected,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = Color(0xFFE8F5E9)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Activity.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { PlaceholderScreen("Home") }
            composable(Screen.Activity.route) {
                ActivityListScreen(viewModel, onAddActivity = {
                    navController.navigate("log_activity")
                })
            }
            composable("log_activity") {
                LogActivityScreen(viewModel, onBack = {
                    navController.popBackStack()
                })
            }
            composable(Screen.Analytics.route) { PlaceholderScreen("Analytics") }
            composable(Screen.Goals.route) { PlaceholderScreen("Goals") }
            composable(Screen.Settings.route) { PlaceholderScreen("Settings") }
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFFFDFBF7)) {
        Box(contentAlignment = Alignment.Center) {
            Text("$name Screen (Under Development)")
        }
    }
}
