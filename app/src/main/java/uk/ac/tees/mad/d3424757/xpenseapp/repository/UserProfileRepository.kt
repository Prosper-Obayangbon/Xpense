package uk.ac.tees.mad.d3424757.xpenseapp.repository

import uk.ac.tees.mad.d3424757.xpenseapp.data.dao.UserProfileDao
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.UserProfile

class UserProfileRepository(private val dao: XpenseDatabase) {

    // Fetch user profile
    suspend fun getUserProfile(userId: Int): UserProfile? {
        return dao.userProfileDao().getUserProfile(userId)
    }

    // Insert or update user profile
    suspend fun saveUserProfile(userProfile: UserProfile) {
        dao.userProfileDao().insertUserProfile(userProfile)
    }

    // Update the profile picture
    suspend fun updateProfilePicture(userId: Int, uri: String) {
        dao.userProfileDao().updateProfilePicture(userId, uri)
    }
}
