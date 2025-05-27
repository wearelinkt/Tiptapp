package iq.tiptapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import iq.tiptapp.ui.HomeScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navController: NavHostController = rememberNavController()

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = HOME_ROUTE,
        ) {
            composable(route = HOME_ROUTE) {
                HomeScreen()
            }
        }
    }
}


private const val HOME_ROUTE = "home"
