package com.example.todoapp.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todoapp.ui.screen.MainPage
import com.example.todoapp.ui.screen.SettingsScreen
import com.example.todoapp.viewmodel.TaskViewModel

import androidx.compose.ui.platform.LocalContext
import com.example.todoapp.utils.PreferencesUtil

@Composable
fun AppNavigation(taskViewModel: TaskViewModel = viewModel()) {
    val context = LocalContext.current
    val navController = rememberNavController()
    var darkMode by rememberSaveable { mutableStateOf(PreferencesUtil.getDarkModeSetting(context)) }

    LaunchedEffect(darkMode) {
        PreferencesUtil.saveDarkModeSetting(context, darkMode)
    }

    MaterialTheme(colorScheme = if (darkMode) darkColorScheme() else lightColorScheme()) {
        Scaffold(
            bottomBar = { BottomNavigationBar(navController) }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(paddingValues)
            ) {
                composable("home") { MainPage(navController, taskViewModel) }
                composable("settings") { SettingsScreen(darkMode, onDarkModeToggle = { darkMode = it }) }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    NavigationBar {
        listOf("home" to "Home", "settings" to "Settings").forEach { (route, label) ->
            NavigationBarItem(
                selected = false,
                onClick = { navController.navigate(route) },
                label = { Text(label) },
                icon = {}
            )
        }
    }
}

//fun ToDoApp() {
//    val navController = rememberNavController()
//    var darkMode by rememberSaveable { mutableStateOf(false) }
//    var tasks by rememberSaveable { mutableStateOf(listOf(
//        Task("Buy Groceries", "Milk, Eggs, Bread", "2025-03-25", "14:30:00", false),
//        Task("Meeting", "Project discussion", "2025-03-26", "10:00:00", false)
//    )) }
//    MaterialTheme(colorScheme = if (darkMode) darkColorScheme() else lightColorScheme()) {
//        Scaffold(
//            bottomBar = { BottomNavigationBar(navController) }
//        ) { paddingValues ->
//            NavHost(
//                navController = navController,
//                startDestination = "home",
//                modifier = Modifier.padding(paddingValues)
//            ) {
//                composable("home") { HomeScreen(navController, tasks, onUpdateTasks = { tasks = it }) }
//                composable("statistics") { StatisticsScreen(tasks) }
//                composable("settings") { SettingsScreen(darkMode, onDarkModeToggle = { darkMode = it }) }
//                composable("add") { AddEditTaskScreen(navController, onUpdateTasks = { tasks = it }, tasks) }
//            }
//        }
//    }
//}