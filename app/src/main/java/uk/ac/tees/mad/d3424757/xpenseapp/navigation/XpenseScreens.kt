package uk.ac.tees.mad.d3424757.xpenseapp.navigation

enum class XpenseScreens(val route: String) {
    SplashScreen("splash"),
    Onboarding("onboarding"),
    Signup("signup"),
    Login("login"),
    Home("home"),
    AddScreen("add"),
    AddTransaction("addTransaction"),
    TransactionScreen("transaction"),
    StatsScreen("stats"),
    TransactionDetailsScreen("transaction/{transactionId}"),
    Budget("budget")

}
