package uk.ac.tees.mad.d3424757.xpenseapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Long? = 0,
    val name: String,
    val email: String,
    val profilePicture: String? // Store the URI of the profile picture
)
