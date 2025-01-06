package uk.ac.tees.mad.d3424757.xpenseapp.navigation

// Enum class defining all the screens (routes) in the app
enum class XpenseScreens(val route: String) {
    // Basic screens
    SplashScreen("splash"),
    Onboarding("onboarding"),
    Signup("signup"),
    Login("login"),
    Home("home"),
    SignUpLoadingScreen("loading/{userId}"),
    TermsAndConditions("legal"),

    // Transaction-related Screens
    AddScreen("add"),
    AddTransaction("addTransaction"),
    TransactionScreen("transaction"),
    StatsScreen("stats"),

    // Screens with dynamic parameters
    // Transaction details screen takes a transaction ID as a parameter
    TransactionDetailsScreen("transaction/{transactionId}"),

    // Budget related screens, some with parameters
    Budget("budget"),
    AddBudget("addBudget/{isEdit}/{budgetId}"), // Add or edit a budget
    BudgetDetailScreen("budget/{budgetId}"), // View a specific budget's details

    // Profile-related screens
    Profile("profile"),
    ProfileInfoScreen("profileInfo"),
    ChangePasswordScreen("ChangePassword")
}
