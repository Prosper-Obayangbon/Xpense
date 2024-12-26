package uk.ac.tees.mad.d3424757.xpenseapp.data.dao

import androidx.room.*
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.UserProfile

@Dao
interface UserProfileDao {

    /**
     * Insert a user profile into the database.
     * If a profile with the same ID already exists, it will be replaced.
     *
     * @param userProfile The UserProfile object to be inserted or replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfile)

    /**
     * Get a user profile by its ID.
     *
     * @param userId The ID of the user profile to retrieve.
     * @return The UserProfile object, or null if no profile exists with the given ID.
     */
    @Query("SELECT * FROM user_profile WHERE id = :userId")
    suspend fun getUserProfile(userId: Int): UserProfile?

    /**
     * Update the profile picture for a user.
     *
     * @param userId The ID of the user whose profile picture needs to be updated.
     * @param uri The URI of the new profile picture.
     */
    @Query("UPDATE user_profile SET profilePicture = :uri WHERE id = :userId")
    suspend fun updateProfilePicture(userId: Int, uri: String)
}
