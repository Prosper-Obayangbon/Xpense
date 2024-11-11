package uk.ac.tees.mad.d3424757.xpenseapp.data.preferences

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

// Data class for transaction details
data class TransactionData(
    val name: String,
    val description: String,
    val amount: String,
    val time: String,
    val image: Int,
    val iconColor: Color
)