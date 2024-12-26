package uk.ac.tees.mad.d3424757.xpenseapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing the user profile.
 * This entity will be stored in the "user_profile" table in the database.
 *
 * @param id Unique identifier for the user profile (auto-generated).
 * @param name The name of the user.
 * @param email The email address of the user.
 * @param profilePicture URI of the profile picture (can be null).
 */
@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey(autoGenerate = true) val id: Long? = 0,          // Unique ID for the user profile (auto-generated)
    val name: String,                                            // User's name
    val email: String,                                           // User's email address
    val profilePicture: String?                                  // URI for the profile picture (optional)
)
