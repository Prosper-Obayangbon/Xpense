package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen


/**
 * A composable text link that triggers a callback when clicked.
 *
 * @param text The text displayed for the link.
 * @param onClick A lambda function to be executed when the link is clicked.
 */
@Composable
fun XTextLink(
    text: String,
    onClick: () -> Unit
) {
    // Text button styled as a link
    TextButton(onClick = onClick) {
        Text(
            text = text,
            color = tealGreen, // Color of the text link
            fontSize = 14.sp // Font size of the text link
        )
    }
}
