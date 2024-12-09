package uk.ac.tees.mad.d3424757.xpenseapp.data.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetData(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val amount: Double,
    val date: String,
    val alertEnabled: Boolean,
    val alertThreshold: Int
)
