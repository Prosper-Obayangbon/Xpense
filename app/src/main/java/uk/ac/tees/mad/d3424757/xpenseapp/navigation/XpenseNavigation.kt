package uk.ac.tees.mad.d3424757.xpenseapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.d3424757.xpenseapp.screens.SplashScreen

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
    }
}