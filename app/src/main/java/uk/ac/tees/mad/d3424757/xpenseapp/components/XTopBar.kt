import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme

/**
 * TopBar composable displays a customizable top bar with a centered title and a back button.
 *
 * @param modifier Modifier to be applied to the Row.
 * @param text Title text to be displayed in the top bar.
 * @param textColor Color of the title text.
 * @param backgroundColor Background color for the top bar; can be transparent.
 * @param backClick Lambda function to be executed when the back button is clicked.
 */
@Composable
fun XTopBar(
    modifier: Modifier = Modifier,
    text: String,
    textColor: Color = Color.White,
    backClick: () -> Unit

) {
    Row(
        modifier = modifier
            .background(Color.Transparent) // Applies the transparent or any color background
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Back button icon
        IconButton(onClick = backClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = textColor
            )
        }

        // Centered title text
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )

        // Optional right side spacer to balance layout
        Spacer(modifier = Modifier.width(48.dp))
    }
}

@Preview
@Composable
fun TPreview(){
    XpenseAppTheme{
        XTopBar(text ="Add") { }
    }
}
