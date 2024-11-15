package uk.ac.tees.mad.d3424757.xpenseapp.navigation

import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.HomeViewModel
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.d3424757.xpenseapp.screens.add.AddScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.addTransaction.AddExpense
import uk.ac.tees.mad.d3424757.xpenseapp.screens.signup.Signup
import uk.ac.tees.mad.d3424757.xpenseapp.screens.home.Home
import uk.ac.tees.mad.d3424757.xpenseapp.screens.login.Login
import uk.ac.tees.mad.d3424757.xpenseapp.screens.onboarding.Onboarding
import uk.ac.tees.mad.d3424757.xpenseapp.screens.splash.SplashScreen
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.SignViewModel

@Composable
fun XpenseNavigation(modifier: Modifier, context: Context) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = XpenseScreens.SplashScreen.route
    ) {
        authNavGraph(navController, context)
        mainNavGraph(modifier, navController, context)
    }
}

// Separate auth graph for auth-related screens
private fun NavGraphBuilder.authNavGraph(navController: NavController, context : Context) {
    composable(XpenseScreens.SplashScreen.route) {
        SplashScreen(navController = navController, context = context)
    }
    composable(XpenseScreens.Onboarding.route) {
        Onboarding(navController = navController)
    }
    composable(XpenseScreens.Signup.route) {
        Signup(navController = navController, viewModel = SignViewModel(), context = context)
    }
    composable(XpenseScreens.Login.route) {
        Login(navController = navController, viewModel = SignViewModel())
    }
}

// Separate main graph for main app screens
private fun NavGraphBuilder.mainNavGraph(modifier: Modifier, navController: NavController, context: Context) {
    composable(XpenseScreens.Home.route) {
        val hVM = HomeViewModel(context)
        Home(modifier = modifier, viewModel = hVM, navController = navController)
    }

    composable(XpenseScreens.AddScreen.route) {
        AddScreen(modifier = modifier, viewModel = HomeViewModel(context), navController = navController)
    }

    composable(XpenseScreens.AddTransaction.route) {
        AddExpense(modifier = modifier)
    }
}
