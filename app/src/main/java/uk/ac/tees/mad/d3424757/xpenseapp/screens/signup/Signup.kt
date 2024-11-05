package uk.ac.tees.mad.d3424757.xpenseapp.screens.signup


import android.view.Display
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import uk.ac.tees.mad.d3424757.xpenseapp.components.GoogleSignButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XInputField
import uk.ac.tees.mad.d3424757.xpenseapp.components.XTextLink
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.SignUpViewModel

@Composable
fun Signup(navController: NavController) {
    val signUpViewModel: SignUpViewModel = viewModel()

    // Access the mutable states directly
    val name = signUpViewModel.name
    val email = signUpViewModel.email
   val password = signUpViewModel.password
    val error = signUpViewModel.error


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = mintCream
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            // Name Field
            XInputField(
                value = name,
                onValueChange = { signUpViewModel.updateName(it) },
                label = "Name",
            )

            // Email Field
            XInputField(
                value = email,
                onValueChange = {signUpViewModel.updateEmail(it) },
                label = "Email",
                keyboardType = KeyboardType.Email,
            )

            // Password Field
            XInputField(
                value = password,
                onValueChange = { signUpViewModel.updatePassword(it) },
                label = "Password",
                keyboardType = KeyboardType.Password,
                isPassword = true
            )

            // Display error message if it exists and is not empty
                if (!error.isNullOrEmpty()) {
                    Text(
                        text = error,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

            // Sign-Up Button
            XButton(
                text = "Sign Up"
            ) {
                signUpViewModel.signUpUser { success ->
                    if (success) {
                        navController.navigate(XpenseScreens.Home.name)
                    }
                }
            }


            // Google Sign-Up Button
            GoogleSignButton(text = "Sign Up") { /* Handle Google Sign-Up */ }

            Spacer(modifier = Modifier.height(5.dp))

            // Login TextButton
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already Have an Account?",
                    color = Color.Black,
                    fontSize = 14.sp
                )

                XTextLink(text = "Log In") {
                    navController.navigate(XpenseScreens.Login.name) // Navigate to login
                }
            }
        }
    }
}


