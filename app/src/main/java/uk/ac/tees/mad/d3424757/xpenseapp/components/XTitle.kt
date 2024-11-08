package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.d3424757.xpenseapp.R

@Composable
fun XTitle(modifier: Modifier = Modifier, color: Color = Color.White) {
    Row(
        modifier.padding(1.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = "X",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.lavishly)),
                fontSize = 96.sp,
                color = color
            )
        )
        Text(
            text = "PENSE",
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.iowan)),
                fontSize = 32.sp,
                color = color
            )
        )
    }
}