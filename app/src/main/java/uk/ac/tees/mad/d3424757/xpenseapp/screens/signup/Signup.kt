package uk.ac.tees.mad.d3424757.xpenseapp.screens.signup


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
import androidx.navigation.NavController
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.GoogleSignButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XInputField
import uk.ac.tees.mad.d3424757.xpenseapp.components.XTextLink
import uk.ac.tees.mad.d3424757.xpenseapp.components.XTitle
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.AuthViewModel

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
                        color = MaterialTheme.colorScheme.surfaceVariant,
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
                                    navController.navigate(XpenseScreens.Home.route) {
                                        popUpTo(XpenseScreens.Signup.route) { inclusive = true }

                                    }
                                }
                            }
                        }


                        /*------------------Google Sign-Up Button-----------------*/
                        GoogleSignButton(text = "Sign Up with Google", context = context)

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


