package com.example.smartlinkconfig
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartlinkconfig.screens.ConfigurationScreen
import com.example.smartlinkconfig.screens.LoginScreen

@Composable
fun NavGraph() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable ("login") {
            LoginScreen (onLoginSuccess = {
                navController.navigate("config") {
                    // Prevents user from going back to login screen
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("config") {
            // We will create this screen next
            ConfigurationScreen()
        }
    }
}
