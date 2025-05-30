package iq.tiptapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        Napier.base(DebugAntilog())
        //Firebase.auth.useEmulator("10.0.2.2", 9099)

        ActivityHolder.activity = this

        setContent {
            LoginScreen()
        }
    }
}

@Composable
fun LoginScreen() {
    val navController: NavHostController = rememberNavController()
    val phoneAuthService = AndroidAuthService(ActivityHolder.activity)
    val context = LocalContext.current

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = INPUT_PHONE_NUMBER_ROUTE,
        ) {
            composable(route = INPUT_PHONE_NUMBER_ROUTE) {
                PhoneNumberInputScreen(phoneAuthService) { verificationId ->
                    navController.navigate("${SMS_VERIFICATION_ROUTE}/${verificationId}")
                }
            }
            composable(
                route = "$SMS_VERIFICATION_ROUTE/{${SMS_VERIFICATION_ID}}",
                arguments = listOf(
                    navArgument(SMS_VERIFICATION_ID) { type = NavType.StringType })
            ) {
                val verificationId = it.arguments?.getString(SMS_VERIFICATION_ID)!!
                SmsCodeScreen(
                    dropUnlessResumed {
                        navController.navigateUp()
                    },
                    onCodeComplete = { smsCode, setLoading ->
                        phoneAuthService.verifyCode(verificationId, smsCode, onSuccess = {
                            setLoading(false)
                            navController.navigate(HOME_ROUTE)
                        }, onError = { error ->
                            setLoading(false)
                            Toast.makeText(context, error.message, Toast.LENGTH_LONG).show()
                            Napier.e("Error sending code: ${error.message}")
                        })
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
private const val SMS_VERIFICATION_ID = "verification_id"
private const val HOME_ROUTE = "home"