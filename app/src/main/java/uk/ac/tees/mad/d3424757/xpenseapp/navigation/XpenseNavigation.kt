package uk.ac.tees.mad.d3424757.xpenseapp.navigation

import AddScreen
import HomeViewModel
import ProfileScreen
import SignUpLoadingScreen
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.repository.TransactionRepository
import uk.ac.tees.mad.d3424757.xpenseapp.screens.transaction.AddTransaction
import uk.ac.tees.mad.d3424757.xpenseapp.screens.budget.AddBudget
import uk.ac.tees.mad.d3424757.xpenseapp.screens.budget.BudgetDetailScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.budget.BudgetScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.profile.ChangePasswordScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.Auth.Signup
import uk.ac.tees.mad.d3424757.xpenseapp.screens.home.Home
import uk.ac.tees.mad.d3424757.xpenseapp.screens.Auth.Login
import uk.ac.tees.mad.d3424757.xpenseapp.screens.Auth.Onboarding
import uk.ac.tees.mad.d3424757.xpenseapp.screens.profile.ProfileInfoScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.Auth.SplashScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.TransactionScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.transaction.StatsScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.transaction.TransactionDetailsScreen
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.BudgetViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.AuthViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.StatsViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.TransactionViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.UserProfileVM

/**
 * The main navigation host for the Xpense app.
 * This function initializes the navigation controller and sets up navigation graphs for different parts of the app.
 */
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun XpenseNavigation(modifier: Modifier, context: Context) {
    val navController = rememberNavController()

    // Determine the initial screen dynamically based on user state (e.g., logged in or first time)
    val startDestination = if (/* check user login state */ true) {
        XpenseScreens.SplashScreen.route
    } else {
        XpenseScreens.Onboarding.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination // Starting screen of the app
    ) {
        // Set up navigation graphs for each module
        authNavGraph(navController, context)
        mainNavGraph(modifier, navController, context)
        reportNavGraph(modifier, navController, context)
        budgetNavGraph(modifier, navController, context)

        // Profile navigation is only available for specific API levels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            profileNavGraph(modifier, navController, context)
        }
    }
}

// ----------- AUTHENTICATION NAVIGATION GRAPH -----------

/**
 * Defines navigation for authentication-related screens such as login, signup, onboarding, and splash screens.
 */
private fun NavGraphBuilder.authNavGraph(navController: NavController, context: Context) {
    // Splash screen
    composable(XpenseScreens.SplashScreen.route) {
        SplashScreen(navController = navController, context = context)
    }
    // Onboarding screen for first-time users
    composable(XpenseScreens.Onboarding.route) {
        Onboarding(navController = navController)
    }
    // Signup screen
    composable(XpenseScreens.Signup.route) {
        val authViewModel = AuthViewModel(context)
        Signup(navController = navController, viewModel = authViewModel, context = context)
    }
    // Login screen
    composable(XpenseScreens.Login.route) {
        val authViewModel = AuthViewModel(context)
        Login(navController = navController, viewModel = authViewModel, context = context)
    }
    composable(XpenseScreens.SignUpLoadingScreen.route + "/{isLogin}") { backStackEntry ->
        val isLogin = backStackEntry.arguments?.getBoolean("isLogin") ?: false
        SignUpLoadingScreen(
            navController = navController,
            context = context,
            isLogin = isLogin
        )
    }
}

// ----------- MAIN NAVIGATION GRAPH -----------

/**
 * Defines navigation for the main app screens, such as Home and Add Transaction.
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.mainNavGraph(modifier: Modifier, navController: NavController, context: Context) {
    // Home screen
    composable(XpenseScreens.Home.route) {
        val homeViewModel = HomeViewModel(context)
        Home(modifier = modifier, viewModel = homeViewModel, navController = navController)
    }
    // Add transaction screen
    composable(XpenseScreens.AddScreen.route) {
        val transactionViewModel = TransactionViewModel(context)
        AddScreen(modifier = modifier, viewModel = transactionViewModel, navController = navController)
    }
    // Add transaction with parameters (e.g., isIncome)
    composable(
        route = "addTransaction?isIncome={isIncome}",
        arguments = listOf(navArgument("isIncome") { type = NavType.BoolType })
    ) { backStackEntry ->
        val isIncome = backStackEntry.arguments?.getBoolean("isIncome") ?: false
        val transactionViewModel = TransactionViewModel(context)
        AddTransaction(navController = navController, context = context, viewModel = transactionViewModel, isIncome = isIncome)
    }
}

// ----------- REPORTS NAVIGATION GRAPH -----------

/**
 * Navigation graph for transaction reports and statistics screens.
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.reportNavGraph(modifier: Modifier, navController: NavController, context: Context) {
    // Transactions screen
    composable(XpenseScreens.TransactionScreen.route) {
        val transactionViewModel = TransactionViewModel(context)
        TransactionScreen(modifier = modifier, viewModel = transactionViewModel, navController = navController)
    }
    // Statistics screen
    composable(XpenseScreens.StatsScreen.route) {
        val statsViewModel = StatsViewModel(context)
        StatsScreen(viewModel = statsViewModel, modifier = modifier, navController = navController, context = context)
    }
    // Transaction details screen
    composable(
        XpenseScreens.TransactionDetailsScreen.route,
        arguments = listOf(navArgument("transactionId") { type = NavType.StringType })
    ) { backStackEntry ->
        val transactionId = backStackEntry.arguments?.getString("transactionId")
        val transactionViewModel = TransactionViewModel(context)
        TransactionDetailsScreen(
            modifier = modifier,
            transactionId = transactionId ?: "Error",
            navController = navController,
            viewModel = transactionViewModel
        )
    }
}

// ----------- BUDGET NAVIGATION GRAPH -----------

/**
 * Navigation graph for budget-related screens.
 */
private fun NavGraphBuilder.budgetNavGraph(modifier: Modifier, navController: NavController, context: Context) {
    // Budget screen
    composable(XpenseScreens.Budget.route) {
        val budgetViewModel = BudgetViewModel(context)
        BudgetScreen(modifier = modifier, navController = navController, budgetViewModel = budgetViewModel)
    }
    // Add/edit budget screen
    composable(
        route = XpenseScreens.AddBudget.route,
        arguments = listOf(
            navArgument("isEdit") { type = NavType.BoolType },
            navArgument("budgetId") { type = NavType.IntType }
        )
    ) { backStackEntry ->
        val isEditing = backStackEntry.arguments?.getBoolean("isEdit") ?: false
        val budgetId = backStackEntry.arguments?.getInt("budgetId") ?: -1
        val budgetViewModel = BudgetViewModel(context)
        AddBudget(modifier = modifier, viewModel = budgetViewModel, isEdit = isEditing, budgetId = budgetId, navController = navController)
    }
    // Budget detail screen
    composable(
        XpenseScreens.BudgetDetailScreen.route,
        arguments = listOf(navArgument("budgetId") { type = NavType.StringType })
    ) { backStackEntry ->
        val budgetId = backStackEntry.arguments?.getString("budgetId")
        val budgetViewModel = BudgetViewModel(context)
        BudgetDetailScreen(
            modifier = modifier,
            budgetId = budgetId ?: "Error",
            navController = navController,
            budgetViewModel = budgetViewModel
        )
    }
}

// ----------- PROFILE NAVIGATION GRAPH -----------

/**
 * Navigation graph for profile-related screens.
 * Only available on devices running Android 13+.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun NavGraphBuilder.profileNavGraph(modifier: Modifier, navController: NavController, context: Context) {
    val viewModel = UserProfileVM(
        context = context,
        showToast = { message -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show() }
    )
    // Profile main screen
    composable(XpenseScreens.Profile.route) {
        ProfileScreen(navController = navController, modifier = modifier, viewModel = viewModel)
    }
    // Profile information screen
    composable(XpenseScreens.ProfileInfoScreen.route) {
        ProfileInfoScreen(navController = navController, modifier = modifier, viewModel = viewModel)
    }
    // Change password screen
    composable(XpenseScreens.ChangePasswordScreen.route) {
        ChangePasswordScreen(navController = navController, modifier = modifier, viewModel = viewModel)
    }
}
