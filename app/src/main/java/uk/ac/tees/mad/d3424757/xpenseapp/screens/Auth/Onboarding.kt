package uk.ac.tees.mad.d3424757.xpenseapp.screens.Auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.XTextLink
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen

/**
 * Onboarding screen for the Xpense app.
 *
 * This composable displays an onboarding screen with a character image, tagline, description of the app's features,
 * and buttons for navigation to either the sign-up or login screens.
 * The screen provides the user with an introduction to the app and offers a clear path to start using it.
 *
 * The layout is structured using a Column with vertical arrangement and center alignment.
 * It includes the following components:
 * - An image of a 3D character to visually engage users.
 * - A tagline ("Spend Smarter\nSave More") to communicate the app's core message.
 * - A brief description of the app's features to give context on what it does.
 * - A "Get Started" button that navigates to the sign-up screen.
 * - A "Log In" text link for users who already have an account, navigating to the login screen.
 *
 * @param navController Navigation controller to manage screen transitions.
 */
@Composable
fun Onboarding(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(mintCream),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(40.dp))


        Image(
            painter = painterResource(id = R.drawable.man),
            contentDescription = "3D Character",
            modifier = Modifier
                .size(500.dp)
                .padding(top = 10.dp),
            contentScale = ContentScale.Fit
        )

        // Tagline Section
        Text(
            text = "Spend Smarter\nSave More",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = tealGreen,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        )

        // Onboarding Message
        Text(
            text = "Track your expenses, set budgets, and manage your finances efficiently with Xpense.",
            fontSize = 16.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .padding(top = 16.dp)
        )

        // Buttons Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 40.dp)
        ) {
            // Get Started Button
            Button(
                onClick = {
                    navController.navigate(XpenseScreens.Signup.name)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = tealGreen
                ),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text(
                    text = "Get Started",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }



        }
    }

}