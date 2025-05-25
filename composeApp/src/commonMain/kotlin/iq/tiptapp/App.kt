package iq.tiptapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import iq.tiptapp.MainDestinations.DETAIL_ROUTE
import iq.tiptapp.MainDestinations.DISNEY_KEY
import iq.tiptapp.MainDestinations.HOME_ROUTE
import iq.tiptapp.ui.DetailScreen
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
            composable(
                route = "$DETAIL_ROUTE/{${DISNEY_KEY}}",
                arguments = listOf(
                    navArgument(DISNEY_KEY) { type = NavType.StringType })
            ) {
                DetailScreen(
                    dropUnlessResumed {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val DETAIL_ROUTE = "detail"
    const val DISNEY_KEY = "disney"
}