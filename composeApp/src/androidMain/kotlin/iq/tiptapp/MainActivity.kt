package iq.tiptapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
            LoginScreen()
        }
    }
}

@Composable
fun LoginScreen() {
    val navController: NavHostController = rememberNavController()
    val viewModel: VerificationViewModel = koinViewModel<VerificationViewModel>()

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = INPUT_PHONE_NUMBER_ROUTE,
        ) {
            composable(route = INPUT_PHONE_NUMBER_ROUTE) {
                PhoneNumberInputScreen(viewModel) {
                    navController.navigate(SMS_VERIFICATION_ROUTE)
                }
            }
            composable(
                route = SMS_VERIFICATION_ROUTE
            ) {
                SmsCodeScreen(
                    viewModel = viewModel,
                    onBackClick = dropUnlessResumed {
                        navController.navigateUp()
                    },
                    onCodeVerified = {
                        navController.navigate(HOME_ROUTE)
                    }
                )
            }
            composable(route = HOME_ROUTE) {
                App()
            }
        }
    }
}

private const val INPUT_PHONE_NUMBER_ROUTE = "input_phone_number"
private const val SMS_VERIFICATION_ROUTE = "verification"
private const val HOME_ROUTE = "home"