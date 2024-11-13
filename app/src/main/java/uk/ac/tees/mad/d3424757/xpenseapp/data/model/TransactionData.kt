package uk.ac.tees.mad.d3424757.xpenseapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ac.tees.mad.d3424757.xpenseapp.utils.CategoryType
import uk.ac.tees.mad.d3424757.xpenseapp.utils.TransactionType

@Entity(tableName = "Transactions")
data class TransactionData(
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    val name: String,
    val description: String,
    val amount: Double,
    val time: Long,
    val date : Long,
    val type : TransactionType,
    val category : CategoryType
)