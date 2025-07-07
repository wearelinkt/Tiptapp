package iq.tiptapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import iq.tiptapp.expected.createDataStore
import iq.tiptapp.ui.splash.SplashScreen
import iq.tiptapp.ui.verification.PhoneNumberInputScreen
import iq.tiptapp.ui.verification.SmsCodeScreen
import iq.tiptapp.ui.verification.VerificationViewModel
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        ActivityHolder.activity = this

        setContent {
            LoginScreen(prefs = remember {
                createDataStore(applicationContext)
            })
        }
    }
}

@Composable
fun LoginScreen(prefs: DataStore<Preferences>) {
    val navController: NavHostController = rememberNavController()
    val viewModel: VerificationViewModel = koinViewModel<VerificationViewModel>()

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = HOME_ROUTE,
        ) {
            composable(route = SPLASH_ROUTE) {
                SplashScreen(prefs) {
                    if (it) {
                        navController.navigate(HOME_ROUTE) {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(INPUT_PHONE_NUMBER_ROUTE) {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                }
            }
            composable(route = INPUT_PHONE_NUMBER_ROUTE) {
                PhoneNumberInputScreen(viewModel) {
                    navController.navigate(SMS_VERIFICATION_ROUTE)
                }
            }
            composable(
                route = SMS_VERIFICATION_ROUTE
            ) {
                SmsCodeScreen(
                    prefs = prefs,
                    viewModel = viewModel,
                    onBackClick = dropUnlessResumed {
                        navController.navigateUp()
                    },
                    navigate = {
                        navController.navigate(HOME_ROUTE) {
                            popUpTo(0)
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(route = HOME_ROUTE) {
                TiptappApp()
            }
        }
    }
}

private const val SPLASH_ROUTE = "splash"
private const val INPUT_PHONE_NUMBER_ROUTE = "input_phone_number"
private const val SMS_VERIFICATION_ROUTE = "verification"
private const val HOME_ROUTE = "home"