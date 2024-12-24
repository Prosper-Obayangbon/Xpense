package uk.ac.tees.mad.d3424757.xpenseapp.navigation

import AddScreen
import ProfileScreen
import TransactionScreen
import android.annotation.SuppressLint
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
import uk.ac.tees.mad.d3424757.xpenseapp.repository.UserProfileRepository
import uk.ac.tees.mad.d3424757.xpenseapp.screens.addTransaction.AddTransaction
import uk.ac.tees.mad.d3424757.xpenseapp.screens.addBudget.AddBudget
import uk.ac.tees.mad.d3424757.xpenseapp.screens.budget.BudgetDetailScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.budget.BudgetScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.changePassword.ChangePasswordScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.signup.Signup
import uk.ac.tees.mad.d3424757.xpenseapp.screens.home.Home
import uk.ac.tees.mad.d3424757.xpenseapp.screens.login.Login
import uk.ac.tees.mad.d3424757.xpenseapp.screens.onboarding.Onboarding
import uk.ac.tees.mad.d3424757.xpenseapp.screens.profileInfo.ProfileInfoScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.splash.SplashScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.stats.StatsScreen
import uk.ac.tees.mad.d3424757.xpenseapp.screens.transaction.TransactionDetailsScreen
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.BudgetViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.AuthViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.StatsViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.TransactionViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.HomeViewModel
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.UserProfileVM
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun XpenseNavigation(modifier: Modifier, context: Context) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = XpenseScreens.SplashScreen.route
    ) {
        authNavGraph(navController, context)
        mainNavGraph(modifier, navController, context)
        reportNavGraph(modifier, navController, context)
        budgetNavGraph(modifier, navController, context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            profileNavGraph(modifier, navController, context)
        }
    }
}

// Separate auth graph for auth-related screens
private fun NavGraphBuilder.authNavGraph(navController: NavController, context: Context) {
    composable(XpenseScreens.SplashScreen.route) {
        SplashScreen(navController = navController, context = context)
    }
    composable(XpenseScreens.Onboarding.route) {
        Onboarding(navController = navController)
    }
    composable(XpenseScreens.Signup.route) {
        Signup(navController = navController, viewModel = AuthViewModel(context), context = context)
    }
    composable(XpenseScreens.Login.route) {
        Login(navController = navController, viewModel = AuthViewModel(context))
    }
}

// Separate main graph for main app screens
private fun NavGraphBuilder.mainNavGraph(modifier: Modifier, navController: NavController, context: Context) {
    composable(XpenseScreens.Home.route) {
        val hVM = HomeViewModel(context)
        Home(modifier = modifier, viewModel = hVM, navController = navController)
    }

    composable(XpenseScreens.AddScreen.route) {
        AddScreen(modifier = modifier, viewModel = TransactionViewModel(context), navController = navController)
    }

    composable(
        route = "addTransaction?isIncome={isIncome}",
        arguments = listOf(navArgument("isIncome") { type = NavType.BoolType })
    ) { backStackEntry ->
        val isIncome = backStackEntry.arguments?.getBoolean("isIncome") ?: false
        AddTransaction(navController = navController, context = context, viewModel = TransactionViewModel(context), isIncome = isIncome)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun NavGraphBuilder.reportNavGraph(modifier: Modifier, navController: NavController, context: Context) {
    // Transaction Screen
    composable(XpenseScreens.TransactionScreen.route) {
        TransactionScreen(modifier = modifier, viewModel = TransactionViewModel(context), navController = navController)
    }

    // Stats Screen
    val dao = XpenseDatabase.getDatabase(context)
    val repository = TransactionRepository(dao)
    composable(XpenseScreens.StatsScreen.route) {
        StatsScreen(viewModel = StatsViewModel(repository), modifier = modifier, navController = navController, context = context)
    }

    // Transaction Details Screen
    composable(
        XpenseScreens.TransactionDetailsScreen.route ,
        arguments = listOf(navArgument("transactionId") { type = NavType.StringType })
    ) { backStackEntry ->
        val transactionId = backStackEntry.arguments?.getString("transactionId")
        if (transactionId != null) {
            TransactionDetailsScreen(modifier = modifier, transactionId = transactionId, navController = navController, viewModel = TransactionViewModel(context))
        } else {
            // Handle the case where transactionId is null, show an error or fallback screen
        }
    }
}

// Separate budget graph for main app screens
private fun NavGraphBuilder.budgetNavGraph(modifier: Modifier, navController: NavController, context: Context) {
    composable(XpenseScreens.Budget.route) {
        BudgetScreen(
            modifier = modifier,
            navController = navController,
            budgetViewModel = BudgetViewModel(context)
        )
    }

    composable(
        route = "addBudget/{isEditing}/{budgetId}",
        arguments = listOf(
            navArgument("isEditing") { type = NavType.BoolType },
            navArgument("budgetId") { type = NavType.IntType }
        )
    ) {
        val isEditing = it.arguments?.getBoolean("isEditing") ?: false
        val budgetId = it.arguments?.getInt("budgetId") ?: -1
        AddBudget(modifier= modifier, context = context, isEdit = isEditing, budgetId = budgetId, navController = navController)
    }


    composable(
        XpenseScreens.BudgetDetailScreen.route ,
        arguments = listOf(navArgument("budgetId") { type = NavType.StringType })
    ) { backStackEntry ->
        val budgetId = backStackEntry.arguments?.getString("budgetId")
        if (budgetId != null) {
            BudgetDetailScreen(modifier = modifier, budgetId = budgetId, navController = navController, budgetViewModel = BudgetViewModel(context))
        } else {
            // Handle the case where budgetId is null, show an error or fallback screen
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun NavGraphBuilder.profileNavGraph(modifier: Modifier, navController: NavController, context: Context) {
    val viewModel = UserProfileVM(
        context = context,
        showToast = { message -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show() }
    )


    composable(XpenseScreens.Profile.route) {
        ProfileScreen(navController = navController, modifier = modifier, viewModel = viewModel, userId = 0)
    }
    composable(XpenseScreens.ProfileInfoScreen.route) {
        ProfileInfoScreen(navController = navController, modifier = modifier, viewModel = viewModel)
    }
    composable(XpenseScreens.ChangePasswordScreen.route) {
        ChangePasswordScreen(navController = navController, modifier = modifier,viewModel = viewModel)
    }
}
