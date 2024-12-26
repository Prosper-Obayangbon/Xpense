package uk.ac.tees.mad.d3424757.xpenseapp.screens.Auth

import android.content.Context
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import uk.ac.tees.mad.d3424757.xpenseapp.components.XTitle
import uk.ac.tees.mad.d3424757.xpenseapp.data.preferences.UserPreferences
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.ANIMATION_DURATION
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants.SPLASH_DELAY


/**
 * A composable function that displays a splash screen with an animated logo.
 * The splash screen will animate the logo, and after a delay, navigate the user to either
 * the login screen or onboarding screen based on their registration status.
 *
 * @param navController The NavController to handle navigation between screens.
 * @param context The context used to access shared preferences for checking registration status.
 */
@Composable
fun SplashScreen(navController: NavController, context: Context) {

    // The surface fills the entire screen and has a background color of tealGreen
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = tealGreen
    ) {
        // Column to center the content horizontally and vertically
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Remember the instance of UserPreferences to check the user's registration status
            val userPreferences = remember { UserPreferences(context) }

            // Animatable state for applying the scaling animation to the logo
            val scale = remember { Animatable(0f) }

            // Launch effect for animating the logo and transitioning to the next screen after a delay
            LaunchedEffect(Unit) {
                // Animate the logo's scale to give a zoom effect
                scale.animateTo(
                    targetValue = 0.9f,
                    animationSpec = tween(
                        durationMillis = ANIMATION_DURATION,
                        easing = {
                            // OvershootInterpolator gives a bounce effect to the scaling
                            OvershootInterpolator(8f).getInterpolation(it)
                        }
                    )
                )
                // Wait for the splash screen animation to complete
                delay(SPLASH_DELAY)

                // Check if the user is already registered
                if (userPreferences.isUserRegistered()) {
                    // If user is registered, navigate to Login Screen
                    navController.navigate(XpenseScreens.Login.route) {
                        // Pop the splash screen off the back stack to prevent back navigation
                        popUpTo(XpenseScreens.SplashScreen.route) { inclusive = true }
                    }
                } else {
                    // If user is not registered, navigate to the Onboarding Screen
                    navController.navigate(XpenseScreens.Onboarding.route) {
                        // Pop the splash screen off the back stack to prevent back navigation
                        popUpTo(XpenseScreens.SplashScreen.route) { inclusive = true }
                    }
                }
            }

            // Surface containing the logo and text, with padding and a circular shape
            Surface(
                modifier = Modifier
                    .padding(15.dp)
                    .size(330.dp)
                    .scale(scale.value),
                shape = CircleShape,
                color = tealGreen,
                border = BorderStroke(width = 2.dp, color = Color.LightGray) // Light gray border
            ) {
                // Column to center the logo and tagline
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Custom title composable that renders the logo
                    XTitle()

                    // Text below the logo with a tagline, in italic and light gray color
                    Text(
                        text = "Track, Save, Grow...", // Tagline text
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        color = Color.LightGray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
