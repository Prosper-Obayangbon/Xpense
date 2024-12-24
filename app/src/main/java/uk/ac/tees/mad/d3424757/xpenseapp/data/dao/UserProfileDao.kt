package uk.ac.tees.mad.d3424757.xpenseapp.data.dao

import androidx.room.*
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.UserProfile

@Dao
interface UserProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserProfile(userProfile: UserProfile)

    @Query("SELECT * FROM user_profile WHERE id = :userId")
    suspend fun getUserProfile(userId: Int): UserProfile?

    @Query("UPDATE user_profile SET profilePicture = :uri WHERE id = :userId")
    suspend fun updateProfilePicture(userId: Int, uri: String)
}
