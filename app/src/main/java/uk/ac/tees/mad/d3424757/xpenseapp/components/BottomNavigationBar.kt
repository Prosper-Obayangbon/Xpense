package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen

/**
 * Data class to hold information about each navigation item in the bottom bar.
 *
 * @param route The navigation route associated with this item.
 * @param icon A composable function providing the icon for the item.
 * @param label The label text for the item.
 */
data class NavigationItemData(
    val route: String,
    val icon: @Composable () -> Unit,
    val label: String
)

/**
 * BottomNavigationBar composable creates a customizable bottom navigation bar
 * with a floating action button (FAB) in the center.
 *
 * @param modifier Modifier for customization of the navigation bar layout.
 * @param navController The [NavController] to manage navigation between screens.
 */
@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier, navController: NavController) {
    // Observe the current navigation route to highlight the selected item.
    val currentRoute by navController.currentBackStackEntryAsState()

    // Define the items to be displayed in the navigation bar.
    val items = listOf(
        NavigationItemData(
            route = XpenseScreens.Home.route,
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
            label = "Home"
        ),
        NavigationItemData(
            route = XpenseScreens.TransactionScreen.route,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.transaction),
                    contentDescription = "Transaction"
                )
            },
            label = "Transaction"
        ),
        NavigationItemData(
            route = XpenseScreens.Budget.route,
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_pie_chart_24),
                    contentDescription = "Budget"
                )
            },
            label = "Budget"
        ),
        NavigationItemData(
            route = XpenseScreens.ProfileScreen.route,
            icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Profile") },
            label = "Profile"
        )
    )

    // Main container for the navigation bar and FAB.
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp) // Fixed height for the navigation bar
    ) {
        // The NavigationBar that contains all the navigation items.
        NavigationBar(
            containerColor = mintCream, // Background color for the bar
            contentColor = Color.Gray   // Default content color
        ) {
            items.forEach { item ->
                NavigationBarItem(
                    icon = item.icon, // Icon for the item
                    label = { Text(item.label) }, // Label text for the item
                    selected = currentRoute?.destination?.route == item.route, // Highlight if selected
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = tealGreen,   // Icon color when selected
                        selectedTextColor = tealGreen,  // Text color when selected
                        indicatorColor = Color.Transparent // Removes the indicator background
                    ),
                    onClick = {
                        navController.navigate(item.route) {
                            launchSingleTop = true // Avoid duplicate destinations in the stack
                            restoreState = true    // Preserve scroll or other states
                            popUpTo(XpenseScreens.SplashScreen.route) { inclusive = true }
                        }
                    }
                )
            }
        }

        // Floating Action Button for adding items, positioned above the navigation bar.
        FloatingActionButton(
            onClick = {
                navController.navigate(XpenseScreens.AddScreen.route) {
                    popUpTo(XpenseScreens.SplashScreen.route) { inclusive = true }
                }
            },
            shape = CircleShape,  // Makes the FAB circular
            containerColor = tealGreen, // Background color of the FAB
            contentColor = Color.White, // Icon color in the FAB
            modifier = Modifier
                .align(Alignment.TopCenter) // Centers the FAB horizontally
                .offset(y = -30.dp)        // Moves the FAB above the navigation bar
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add") // Icon in FAB
        }
    }
}

/**
 * Preview of the BottomNavigationBar composable.
 * This preview uses a fake NavController to simulate the navigation bar's behavior.
 */
@Preview(showBackground = true, widthDp = 360, heightDp = 80)
@Composable
fun BottomNavigationBarPreview() {
    // Use a mock NavController for the preview.
    val fakeNavController = rememberNavController()
    XpenseAppTheme {
        BottomNavigationBar(navController = fakeNavController)
    }
}
