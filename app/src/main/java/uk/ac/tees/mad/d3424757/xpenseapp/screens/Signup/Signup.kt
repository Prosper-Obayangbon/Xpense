package uk.ac.tees.mad.d3424757.xpenseapp.screens.Signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.d3424757.xpenseapp.components.GoogleSignUpButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XInputField
import uk.ac.tees.mad.d3424757.xpenseapp.components.XTextLink
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme

@Composable
fun Signup(navController: NavController) {
    // Name Field
    var name by remember { mutableStateOf("") }
    // Email Field
    var email by remember { mutableStateOf("") }
    // password Field
    var password by remember { mutableStateOf("") }

    Surface(modifier =Modifier
        .fillMaxSize(),
        color = Color.White
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(vertical = 32.dp)
            )

            // Name Field
            XInputField(
                value = name,
                onValueChange = {name = it},
                label = "Name",
            )
            XInputField(
                value = email,
                onValueChange = {email = it},
                label = "Email",
                keyboardType = KeyboardType.Email,
            )
            XInputField(
                value = password,
                onValueChange = {password = it},
                label = "Password",
                keyboardType = KeyboardType.Password,
                isPassword = true
            )

            //Sign-Up Button
            XButton(
                text = "Sign Up"
            ) { }

            // Google Sign-Up Button
            GoogleSignUpButton {  }

            // Login TextButton
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already Have Account?",
                    color = Color.Black, // Set color to black
                    fontSize = 14.sp
                )

                XTextLink(text = "Log In", onClick = {} )
            }



        }
    }
    }


//@Preview(showBackground = true)
//@Composable
//fun SignUpPreview(){
//    XpenseAppTheme{
//        Signup()
//    }
//
//}