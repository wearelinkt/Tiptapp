package iq.tiptapp.login

import androidx.compose.runtime.Composable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import iq.tiptapp.HOME_ROUTE
import iq.tiptapp.INPUT_PHONE_NUMBER_ROUTE
import iq.tiptapp.TiptappApp
import iq.tiptapp.ui.verification.PhoneNumberInputScreen
import iq.tiptapp.ui.verification.SmsCodeScreen
import iq.tiptapp.ui.verification.VerificationViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
actual fun Login(prefs: DataStore<Preferences>) {
    val navController: NavHostController = rememberNavController()
    val viewModel: VerificationViewModel = koinViewModel<VerificationViewModel>()
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

private const val SMS_VERIFICATION_ROUTE = "verification"