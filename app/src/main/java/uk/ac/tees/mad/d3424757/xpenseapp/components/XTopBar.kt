package uk.ac.tees.mad.d3424757.xpenseapp.components

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme

/**
 * A customizable top app bar with a back button and title.
 *
 * @param text The title text displayed on the app bar.
 * @param color The color of the text and icon is white).
 * @param backClick A lambda function to be executed when the back button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.White,
    backClick: () -> Unit,

) {
    TopAppBar(

        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = backClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "Back",
                        tint = color
                    )
                }

                Text(
                    text = text,
                    color = color,
                    fontSize = 20.sp, // Font size of the title
                    fontWeight = FontWeight.Bold, // Weight of the title text
                    modifier = modifier.padding(horizontal = 120.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent // Set to your desired background color
        )
    )
}

@Preview
@Composable
fun TPreview(){
    XpenseAppTheme{
        TopBar(text = "heh") { }
    }
}