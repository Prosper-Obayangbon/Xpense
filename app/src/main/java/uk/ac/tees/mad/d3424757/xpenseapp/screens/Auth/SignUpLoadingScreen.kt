import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.repository.UserProfileRepository
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen

/**
 * Composable function to display a personalized loading screen after user sign up or login.
 *
 * This screen shows a loading animation with a welcome message, the user's name, and then navigates
 * to the home screen after a short delay.
 *
 * @param navController The NavController used to navigate to the home screen after the delay.
 * @param userName The name of the user to display in the welcome message.
 * @param isLogin Boolean to check if the user is logging in or signing up.
 */
@Composable
fun SignUpLoadingScreen(navController: NavController, isLogin: Boolean, context : Context) {

    var username by remember { mutableStateOf("") }

    // Use LaunchedEffect to trigger the data fetching asynchronously
    LaunchedEffect(0) {
        // Launch a coroutine to fetch the username from the Room database
        withContext(Dispatchers.IO) {
            val userProfile = UserProfileRepository(XpenseDatabase.getDatabase(context)).getUserProfile()
            // Update the state with the username
            username = userProfile?.firstName ?: "User"
        }
    }

    // State to control visibility of the welcome message and name
    var isMessageVisible by remember { mutableStateOf(false) }

    // Use LaunchedEffect to handle side effects, like navigating after a delay
    LaunchedEffect(Unit) {
        // Simulate loading with a delay
        delay(1500) // Wait for 1.5 seconds before showing the message
        isMessageVisible = true // Show the message after the delay
        delay(1000) // Delay for the message visibility before navigating
        navController.navigate(XpenseScreens.Home.route) // Navigate to the home screen after the delay
    }

    // Main layout for the loading screen
    Box(
        modifier = Modifier
            .fillMaxSize() // Make the box take up the whole screen
            .background(tealGreen), // Set a white background
        contentAlignment = Alignment.Center // Align the content in the center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Visibility for the Welcome Message and User's Name
            AnimatedVisibility(
                visible = isMessageVisible,
                enter = fadeIn(tween(durationMillis = 1000)) // Fade-in animation for the message
            ) {
                // Show "Welcome" for SignUp and "Welcome back" for Login
                val welcomeMessage = if (isLogin) {
                    "Welcome back, $username!"
                } else {
                    "Welcome, $username!"
                }

                Text(
                    text = welcomeMessage,
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp) // Add space below the welcome message
                )
            }

            // Display a circular progress indicator (spinner) while the app is processing
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp), // Set the size of the progress indicator
                color = MaterialTheme.colorScheme.primary // Set the color of the spinner
            )
        }
    }
}

/**
 * Composable function to preview the personalized loading screen.
 *
 * This preview simulates a user signing up and displays the loading screen with a welcome message.
 */
@Preview(showBackground = true)
@Composable
fun PreviewSignUpLoadingScreen() {
    val context = LocalContext.current
    val navController = rememberNavController()
    SignUpLoadingScreen(navController = navController, isLogin = false, context = context) // Preview SignUp
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginLoadingScreen() {
    val navController = rememberNavController()
    //SignUpLoadingScreen(navController = navController, isLogin = true, context = context) // Preview Login
}
