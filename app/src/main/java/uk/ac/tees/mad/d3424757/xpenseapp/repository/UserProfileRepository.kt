package uk.ac.tees.mad.d3424757.xpenseapp.repository

import uk.ac.tees.mad.d3424757.xpenseapp.data.dao.UserProfileDao
import uk.ac.tees.mad.d3424757.xpenseapp.data.database.XpenseDatabase
import uk.ac.tees.mad.d3424757.xpenseapp.data.model.UserProfile

/**
 * Repository for handling user profile operations.
 *
 * @param dao The database instance providing access to the UserProfileDao.
 */
class UserProfileRepository(private val dao: XpenseDatabase) {

    /**
     * Fetches the user profile from the database by user ID.
     *
     * @param userId The ID of the user whose profile needs to be fetched.
     * @return The user profile associated with the given ID, or null if not found.
     */
    suspend fun getUserProfile(userId: Int): UserProfile? {
        return dao.userProfileDao().getUserProfile(userId)
    }

    /**
     * Inserts or updates the user profile in the database.
     * This method uses the insert strategy for both insert and update.
     *
     * @param userProfile The user profile object to be inserted or updated.
     */
    suspend fun saveUserProfile(userProfile: UserProfile) {
        dao.userProfileDao().insertUserProfile(userProfile)
    }

    /**
     * Updates the user's profile picture in the database.
     *
     * @param userId The ID of the user whose profile picture needs to be updated.
     * @param uri The URI of the new profile picture.
     */
    suspend fun updateProfilePicture(userId: Int, uri: String) {
        dao.userProfileDao().updateProfilePicture(userId, uri)
    }

    /**
     * Updates the user profile details in the database.
     *
     * @param userProfile The user profile object with updated details.
     */
    suspend fun updateUserProfile(userProfile: UserProfile) {
        // If needed, you can differentiate between insert and update operations here,
        // for now, it's handled by the `insertUserProfile` method.
        dao.userProfileDao().insertUserProfile(userProfile)
    }
}
