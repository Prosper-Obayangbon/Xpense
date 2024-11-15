package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon

import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter.State.Empty.painter
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen


/**
 * BottomNavigationBar composable displays the main navigation bar with a centered floating action button.
 *
 * @param onFabClick Lambda function to handle FloatingActionButton clicks.
 */
@Composable
fun BottomNavigationBar(navController: NavController)
{
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Primary Navigation Bar container
        NavigationBar(
            containerColor = Color.White,
            contentColor = Color.Gray
        ) {
            // Home navigation item
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Home") },
                label = { Text("Home") },
                selected = true,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = tealGreen,
                    selectedTextColor = tealGreen,
                    indicatorColor = Color.Transparent
                ),
                onClick = { /* TODO: Handle Home click */ }
            )

            // Transaction navigation item
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.transaction),
                        contentDescription = "Transaction"
                    )
                },
                label = { Text("Transaction") },
                selected = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = tealGreen,
                    selectedTextColor = tealGreen,
                    indicatorColor = Color.Transparent
                ),
                onClick = { /* TODO: Handle Transaction click */ }
            )

            // Budget navigation item
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_pie_chart_24),
                        contentDescription = "Budget"
                    )
                },
                label = { Text("Budget") },
                selected = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = tealGreen,
                    selectedTextColor = tealGreen,
                    indicatorColor = Color.Transparent
                ),
                onClick = { /* TODO: Handle Budget click */ }
            )

            // Profile navigation item
            NavigationBarItem(
                icon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Profile") },
                label = { Text("Profile") },
                selected = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = tealGreen,
                    selectedTextColor = tealGreen,
                    indicatorColor = Color.Transparent
                ),
                onClick = { /* TODO: Handle Profile click */ }
            )
        }

        // Floating Action Button centered above the navigation bar
        FloatingActionButton(
            onClick =  {navController.navigate(XpenseScreens.AddScreen.route) },
            shape = CircleShape,
            containerColor = tealGreen,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = -30.dp) // Adjusts position to float above the bar
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Preview
@Composable
fun NavBarPreview(){
    XpenseAppTheme{
       // BottomNavigationBar()
    }
}