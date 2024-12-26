package uk.ac.tees.mad.d3424757.xpenseapp.screens.Auth

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
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

/**
 * Login screen for the Xpense app.
 *
 * This composable provides a user interface for signing into the app with email and password.
 * It includes a sign-in form, with fields for entering an email and password, a button to submit the credentials,
 * and an option to sign in using Google. The screen also includes links for password recovery and fingerprint authentication,
 * as well as a link to the sign-up screen for new users.
 *
 * The layout is organized in a column with centered elements and includes the following components:
 * - A title at the top of the screen.
 * - Email and password input fields with appropriate icons and validation.
 * - An error message display for failed sign-in attempts.
 * - A "Login" button that attempts sign-in and navigates to the home screen upon success.
 * - A Google Sign-In button for signing in with Google.
 * - Links for forgotten password, fingerprint login, and signing up for new users.
 *
 * @param modifier Modifier for the UI elements.
 * @param viewModel AuthViewModel instance to manage the sign-in logic and handle user data.
 * @param navController Navigation controller to manage screen transitions.
 */

@Composable
fun Login(modifier: Modifier = Modifier, viewModel: AuthViewModel, navController: NavController, context : Context){

    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                viewModel.executeGoogleSignIn(idToken) { success ->
                    if (success) {
                        navController.navigate("${XpenseScreens.SignUpLoadingScreen.route}/true") {
                            popUpTo(XpenseScreens.Login.route) { inclusive = true }
                        }
                    } else {
                        // Show error in UI
                    }
                }
            }
        } catch (e: ApiException) {
            //viewModel.setError("Google Sign-In failed: ${e.localizedMessage}")
        }
    }


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
                        color = tealGreen.copy(alpha = 0.2f),
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
                                navController.navigate("${XpenseScreens.SignUpLoadingScreen.route}/true") {
                                    popUpTo(XpenseScreens.Login.route) { inclusive = true }

                                }

                            }
                        }
                    }


                    /*--------------------Google Sign-In Button-------------------*/
                    // Google Sign-In Button
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