package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen

/**
 * A composable text link that triggers a callback when clicked.
 *
 * This component is designed to resemble a hyperlink, with customizable text, color, and click behavior.
 *
 * @param modifier Modifier for customizations like padding, alignment, etc.
 * @param text The text to be displayed for the link.
 * @param color The color of the text link. Defaults to `tealGreen`.
 * @param onClick A lambda function to be executed when the link is clicked.
 */
@Composable
fun XTextLink(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = tealGreen,
    onClick: () -> Unit
) {
    modifier.fillMaxWidth()
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text,
            color = color,
            fontSize = 14.sp
        )
    }
}
