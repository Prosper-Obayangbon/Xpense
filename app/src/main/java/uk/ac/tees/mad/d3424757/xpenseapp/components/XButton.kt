package uk.ac.tees.mad.d3424757.xpenseapp.components

import android.app.Activity
import android.content.Context
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.utils.Constants

/**
 * A custom button composable that takes text and a click handler.
 *
 * This composable creates a button with a customizable text label and provides a click
 * handler to execute custom actions when the button is pressed.
 *
 * @param modifier Modifier to be applied to the button.
 * @param text The text to display on the button.
 * @param handleClick A lambda function to handle the button click event.
 */
@Composable
fun XButton(
    modifier: Modifier = Modifier,
    text: String,
    handleClick: () -> Unit
) {
    Button(
        onClick = handleClick,
        colors = ButtonDefaults.buttonColors(containerColor = tealGreen),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * A composable button for signing up with Google.
 *
 * This button configures the Google Sign-In flow, triggering the authentication process
 * when clicked and starting the appropriate sign-in intent.
 *
 * @param text The text to display on the button.
 * @param context The context used to start the sign-in intent (Activity context).
 * @param modifier Modifier to be applied to the button.
 */
@Composable
fun GoogleSignButton(
    text: String,
    context: Context,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleSignInClient = GoogleSignIn.getClient(context, gso)
            val signInIntent = googleSignInClient.signInIntent
            (context as Activity).startActivityForResult(signInIntent, Constants.RC_SIGN_IN)
        },
        colors = ButtonDefaults.buttonColors(mintCream),
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_google_icon),
            contentDescription = "Google",
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            color = Color.Black
        )
    }
}

/**
 * Preview composable to display the custom button in the UI.
 *
 * This is used for previewing the XButton composable with sample text to visualize
 * how it looks within the app.
 */
@Preview
@Composable
fun ButtonPreview() {
    XpenseAppTheme {
        XButton(text = "heh") { }
    }
}
