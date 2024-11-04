package uk.ac.tees.mad.d3424757.xpenseapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.d3424757.xpenseapp.screens.Signup.Signup
import uk.ac.tees.mad.d3424757.xpenseapp.screens.onboarding.Onboarding
import uk.ac.tees.mad.d3424757.xpenseapp.screens.splash.SplashScreen

@Composable
fun XpenseNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = XpenseScreens.SplashScreen.name
    ) {
        composable(XpenseScreens.SplashScreen.name){
            SplashScreen(navController = navController)
        }
        composable(XpenseScreens.Onboarding.name){
            Onboarding(navController = navController)
        }
        composable(XpenseScreens.Signup.name){
            Signup(navController = navController)
        }
    }
}