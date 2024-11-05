package uk.ac.tees.mad.d3424757.xpenseapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.d3424757.xpenseapp.screens.signup.Signup
import uk.ac.tees.mad.d3424757.xpenseapp.screens.home.Home
import uk.ac.tees.mad.d3424757.xpenseapp.screens.login.Login
import uk.ac.tees.mad.d3424757.xpenseapp.screens.onboarding.Onboarding
import uk.ac.tees.mad.d3424757.xpenseapp.screens.splash.SplashScreen

@Composable
fun XpenseNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = XpenseScreens.SplashScreen.route
    ) {
        authNavGraph(navController)
        mainNavGraph(navController)
    }
}

// Separate auth graph for auth-related screens
private fun NavGraphBuilder.authNavGraph(navController: NavController) {
    composable(XpenseScreens.SplashScreen.route) {
        SplashScreen(navController = navController)
    }
    composable(XpenseScreens.Onboarding.route) {
        Onboarding(navController = navController)
    }
    composable(XpenseScreens.Signup.route) {
        Signup(navController = navController)
    }
    composable(XpenseScreens.Login.route) {
        Login(navController = navController)
    }
}

// Separate main graph for main app screens
private fun NavGraphBuilder.mainNavGraph(navController: NavController) {
    composable(XpenseScreens.Home.route) {
        Home(navController = navController)
    }
}
