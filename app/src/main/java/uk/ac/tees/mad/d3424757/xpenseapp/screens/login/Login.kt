package uk.ac.tees.mad.d3424757.xpenseapp.screens.login

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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.SignViewModel

@Composable
fun Login(modifier: Modifier = Modifier, viewModel: SignViewModel, navController: NavController){

    Surface(modifier.fillMaxSize(),
        color = mintCream
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
                .padding(24.dp)
        ) {
            XTitle(color = tealGreen)

            Spacer(modifier = Modifier.height(40.dp))

            // Sign-In form content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(24.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    /*--------------------Sing in title-------------------*/
                    Text(
                        text = "Sign In",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    /*--------------------Email Field-------------------*/

                    XInputField(
                        value = viewModel.email,
                        onValueChange = { viewModel.updateEmail(it) },
                        label = "Email",
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Email"
                            )
                        },
                        keyboardType = KeyboardType.Email,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    )

                    /*--------------------Password Field-------------------*/
                    XInputField(
                        value = viewModel.password,
                        onValueChange = { viewModel.updatePassword(it) },
                        label = "Password",
                        leadingIcon = {
                            Icon(
                                painterResource(id = R.drawable.password),
                                contentDescription = "password"
                            )
                        },
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)

                    )

                    // Error Message Display
                    viewModel.error?.let { errorMessage ->
                        if (errorMessage.isNotEmpty()) {
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                    }

                    /*--------------------Sign-In Button------------------*/
                    XButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        text = "Login"
                    ) {
                        viewModel.executeSignIn { success ->
                            if (success) {
                                navController.navigate(XpenseScreens.Home.route) {
                                    popUpTo(XpenseScreens.Login.route) { inclusive = true }

                                }

                            }
                        }
                    }


                    /*--------------------Google Sign-In Button-------------------*/
                    GoogleSignButton(
                        text = "Sign In with Google",
                        context = LocalContext.current,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // forgot Password & Fingerprint Links
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                XTextLink(
                    text = "Forget Password?",
                    onClick = {},
                    modifier = Modifier.align(Alignment.CenterVertically),
                )
                XTextLink(
                    text = "Use FingerPrint",
                    color = Color.Blue,
                    onClick = {},
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Spacer(Modifier.height(16.dp))


            /*-------------------Sign-Up Link------------------*/
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't Have an Account?",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(4.dp))

                XTextLink(text = "Sign up") {
                    navController.navigate(XpenseScreens.Signup.name) // Navigate to login
                }

            }

        }
    }
}
//@Preview(showBackground = true)
//@Composable
//fun SignUpPreview(){
//    XpenseAppTheme{
//        Login()
//    }
//
//}