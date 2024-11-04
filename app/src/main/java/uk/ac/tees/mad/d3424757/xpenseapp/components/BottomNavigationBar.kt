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
import coil.compose.AsyncImagePainter.State.Empty.painter
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen


// Composable function to render the Bottom Navigation Bar
@Composable
fun BottomNavigationBar() {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Main container for the navigation bar
        NavigationBar(
            containerColor = Color.White,
            contentColor = Color.Gray
        ) {
            // Home navigation item
            NavigationBarItem(
                icon = { Icons.Default.Home },
                label = {Text("Home")},
                selected = true,
                colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = tealGreen,
                        selectedTextColor = tealGreen,
                        indicatorColor = Color.Transparent
                ),
                onClick = {},

            )

            // Transaction navigation item
            NavigationBarItem(
                icon = { painterResource(id = R.drawable.transaction) },
                label = {Text("Transaction")},
                selected = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = tealGreen,
                    selectedTextColor = tealGreen,
                    indicatorColor = Color.Transparent
                ),
                onClick = {},
            )

            // Budget navigation item
            NavigationBarItem(
                icon = { painterResource(id = R.drawable.chart) },
                label = {Text("Budget")},
                selected = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = tealGreen,
                    selectedTextColor = tealGreen,
                    indicatorColor = Color.Transparent
                ),
                onClick = {},
            )

            // Profile navigation item
            NavigationBarItem(
                icon = { Icons.Default.Person },
                label = {Text("Profile")},
                selected = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = tealGreen,
                    selectedTextColor = tealGreen,
                    indicatorColor = Color.Transparent
                ),
                onClick = {}
            )
        }

        // Floating action button positioned at the center of the bar
        FloatingActionButton(
            onClick = {},
            shape = CircleShape,
            containerColor = tealGreen,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = -30.dp) // Adjust position to float above the bar
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Preview
@Composable
fun NavBarPreview(){
    XpenseAppTheme{
        BottomNavigationBar()
    }
}