package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
 * TransactionItem composable that displays an individual transaction's details in a row layout.
 *
 * @param modifier A modifier to customize the layout of the composable.
 * @param itemName The name of the item (e.g., "Shopping").
 * @param description A short description of the transaction (e.g., "For food").
 * @param amount The amount of the transaction (e.g., "-55" for expense or "45" for income).
 * @param time The time of the transaction (e.g., "10pm").
 * @param icon A resource ID for the icon representing the transaction.
 * @param iconColor The color of the background circle for the icon.
 */
@Composable
fun TransactionItem(
    modifier: Modifier = Modifier,
    itemName: String,
    description: String,
    amount: String,
    time: String,
    icon: Int,
    iconColor: Color
) {
    // Row layout to display the transaction item horizontally
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp, start = 16.dp, end = 16.dp)
            .size(70.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Box containing the icon, with a rounded background color
        Box(
            modifier = Modifier
                .size(50.dp)  // Set the size of the box
                .background(iconColor, shape = RoundedCornerShape(12.dp))
        ) {
            // Icon displayed in the box with a center alignment
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Spacer to add space between the icon and the text columns
        Spacer(modifier = Modifier.width(12.dp))

        // Column to display the item name and description
        Column(modifier = Modifier.weight(1f)) {
            // Item name displayed in bold font
            Text(text = itemName, fontWeight = FontWeight.Bold)
            // Description text in gray color with a smaller font size
            Text(text = description, color = Color.Gray, fontSize = 12.sp)
        }

        // Column for displaying the amount and time, aligned to the end
        Column(horizontalAlignment = Alignment.End) {
            // Amount text with dynamic color based on whether it's negative or positive
            Text(
                text = amount,
                color = if (amount.contains("-")) Color.Red else Color.Green,
                fontWeight = FontWeight.Bold
            )
            // Time displayed in gray color with a smaller font size
            Text(text = time, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

/**
 * Preview composable to visualize the TransactionItem with sample data.
 */
@Preview
@Composable
fun InputPreview() {
    XpenseAppTheme {
        // Preview the TransactionItem composable with sample values
        TransactionItem(
            itemName = "Shopping",
            description = "For food",
            amount = "-55",
            time = "10pm",
            icon = R.drawable.line_chart,
            iconColor = Color.Blue
        )
    }
}
