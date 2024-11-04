package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen

/**
* A composable button with customizable text and click handling.
*
* @param modifier Modifier to be applied to the button.
* @param text The text to display on the button.
* @param handleClick A lambda function to handle click events.
*/
@Composable
fun XButton(
    modifier: Modifier = Modifier,
    text: String,
    handleClick: () -> Unit
) {
    // Custom button
    Button(
        onClick = handleClick,
        colors = ButtonDefaults.buttonColors(containerColor = tealGreen),
        shape = RoundedCornerShape(16.dp), // Rounded corners
        modifier = modifier
            .fillMaxWidth() // Full width button
            .height(48.dp) // Fixed height
    ) {
        // Button text
        Text(
            text = text,
            color = Color.White, // White text
            fontSize = 16.sp, // Font size
            fontWeight = FontWeight.Medium // Font weight
        )
    }
}

/**
 * A composable button for signing up with Google.
 *
 * @param onClick A lambda function to be executed when the button is clicked.
 */
@Composable
fun GoogleSignUpButton(
    onClick: () -> Unit
) {
    // Button with Google Sign Up action
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Google icon
        Icon(
            painter = painterResource(id = R.drawable.ic_google_icon),
            contentDescription = "Google Sign Up",
            tint = Color.Unspecified, // Use Unspecified to keep the original icon color
            modifier = Modifier.size(24.dp) // Icon size defined as a constant
        )

        // Spacer between icon and text
        Spacer(modifier = Modifier.width(8.dp))

        // Button text
        Text(
            text = "Sign Up with Google",
            color = Color.Black // Text color
        )
    }
}


@Preview
@Composable
fun ButtonPreview(){
    XpenseAppTheme{
        XButton(text = "heh") { }
    }
}