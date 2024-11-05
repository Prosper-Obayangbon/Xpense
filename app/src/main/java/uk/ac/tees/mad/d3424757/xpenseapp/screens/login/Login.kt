package uk.ac.tees.mad.d3424757.xpenseapp.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.d3424757.xpenseapp.components.GoogleSignButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.TopBar
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XInputField
import uk.ac.tees.mad.d3424757.xpenseapp.components.XTextLink
import uk.ac.tees.mad.d3424757.xpenseapp.navigation.XpenseScreens
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream

@Composable
fun Login(modifier: Modifier = Modifier, navController: NavController){

    Surface(modifier.fillMaxSize(),
        color = mintCream
    )
    {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign In",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            Spacer(modifier.height(50.dp))

            XInputField(
                value = "Email",
                onValueChange = {},
                label = "Email"
            )
            Spacer(modifier.height(8.dp))

            XInputField(
                value = "Password",
                onValueChange = {},
                label = "Password",
                keyboardType = KeyboardType.Password,
                isPassword = true

            )
            Spacer(modifier.height(8.dp))

            XButton(
                modifier = Modifier.padding(16.dp),
                text= "Login"
            ) { }

            Spacer(modifier.height(8.dp))

            XTextLink(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Forget Password?",
                onClick = {}
                )
            XTextLink(
                modifier.align(Alignment.Start),
                text = "Use FingerPrint",
                color = Color.Blue,
                onClick = {}
            )

            Spacer(Modifier.height(8.dp))

            // Google Sign-Up Button
            GoogleSignButton(text = "Sign In") { /* Handle Google Sign-Up */ }

            Spacer(modifier.height(5.dp))

            // Login TextButton
            Row(
                modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Already Have an Account?",
                    color = Color.Black,
                    fontSize = 14.sp
                )

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