package uk.ac.tees.mad.d3424757.xpenseapp.screens.Auth


import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.GoogleSignButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XInputField
import uk.ac.tees.mad.d3424757.xpenseapp.components.XTextLink
import uk.ac.tees.mad.d3424757.xpenseapp.components.XTitle
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.repository.UserProfileRepository
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.AuthViewModel

/**
 * Signup screen for the Xpense app.
 *
 * This composable provides a user interface for creating a new account. It includes fields for entering the user's name,
 * email, and password, as well as a button to submit the registration form. The screen also includes options for signing up
 * with Google and navigating to the login screen for existing users.
 *
 * The layout is organized in a column with centered elements and includes the following components:
 * - A title at the top of the screen.
 * - Name, email, and password input fields with appropriate icons and validation.
 * - An error message display for invalid input or registration issues.
 * - A "Sign Up" button that attempts registration and navigates to the home screen upon success.
 * - A Google Sign-Up button for registering with a Google account.
 * - A link for users to navigate to the login screen if they already have an account.
 *
 * @param navController Navigation controller to manage screen transitions.
 * @param viewModel AuthViewModel instance to manage user data and authentication logic.
 * @param context Application context, used for updating user registration status.
 */
@Composable
fun Signup(navController: NavController, viewModel: AuthViewModel, context: Context) {


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = mintCream
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            //Displace Title as logo at the top
            XTitle(color = tealGreen)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = tealGreen.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(24.dp)
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally){

                    /* -------------------Sign Up title------------------*/
                    Text(
                        text = "Sign Up",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(vertical = 24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )

                        /*-------------------- Name Field-----------------*/
                        XInputField(
                            value = viewModel.name,
                            onValueChange = { viewModel.updateName(it) },
                            label = "Name",
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Username Icon"
                                )                            }
                        )

                        /*------------------ Email Field-----------------*/
                        XInputField(
                            value = viewModel.email,
                            onValueChange = {viewModel.updateEmail(it) },
                            label = "Email",
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Email, // Use the default person icon
                                    contentDescription = "Email"
                                )
                            },
                            keyboardType = KeyboardType.Email,
                        )

                        /*------------------Password Field-----------------*/
                        XInputField(
                            value = viewModel.password,
                            onValueChange = { viewModel.updatePassword(it) },
                            label = "Password",
                            keyboardType = KeyboardType.Password,
                            leadingIcon = {
                                Icon(
                                    painterResource(id = R.drawable.password),
                                    contentDescription = "password"
                                )

                            },
                            isPassword = true
                        )

                        /*------------------Error Message-----------------*/
                        viewModel.error?.let { errorMessage ->
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .align(Alignment.Start)
                                    .padding(vertical = 8.dp)
                            )
                        }


                        /*------------------Sign-Up Button-----------------*/
                        XButton(
                            text = "Sign Up",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            viewModel.signUpUser { success ->
                                if (success) {
                                    viewModel.updateUserRegistrationStatus(context)
                                    navController.navigate("${XpenseScreens.SignUpLoadingScreen.route}/false") {
                                        popUpTo(XpenseScreens.Signup.route) { inclusive = true }

                                    }
                                }
                            }
                        }


                        /*------------------Google Sign-Up Button-----------------*/
                    GoogleSignButton(
                        text = "Sign In with Google",
                        onSignInResult = { idToken ->
                            viewModel.executeGoogleSignIn(idToken) { success ->
                                if (success) {
                                    navController.navigate("${XpenseScreens.SignUpLoadingScreen.route}/true") {
                                        popUpTo(XpenseScreens.Login.route) { inclusive = true }

                                    }
                                } else {
                                    // Show error message
                                }
                            }
                        },
                        context = context
                    )

                        Spacer(modifier = Modifier.height(16.dp))

                        /*------------------Login Link-----------------*/
                        Row(
                            modifier = Modifier.padding(top = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Already Have an Account?",
                                color = Color.Black,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            XTextLink(text = "Log In") {
                                navController.navigate(XpenseScreens.Login.name) // Navigate to login
                            }
                        }
                    }
                }



        }
    }
}


