package com.loc.composebiometricauth

import android.content.Context
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import uk.ac.tees.mad.d3424757.xpenseapp.screens.Auth.BiometricAuthStatus

class BiometricAuthenticator(
    appContext: Context
) {

    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    private val biometricManager = BiometricManager.from(appContext.applicationContext)
    private lateinit var biometricPrompt: BiometricPrompt

    private fun isBiometricAuthAvailable(): BiometricAuthStatus {
        // Simplified check using only BIOMETRIC_STRONG
        val status = biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)
        Log.d("BiometricStatus", "Biometric support status: $status")

        return when (status) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                Log.d("BiometricStatus", "Biometric authentication is supported.")
                BiometricAuthStatus.READY
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("BiometricStatus", "No biometric hardware detected.")
                BiometricAuthStatus.NOT_AVAILABLE
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d("BiometricStatus", "Biometric hardware unavailable.")
                BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.d("BiometricStatus", "No biometrics enrolled.")
                BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED
            }
            else -> {
                Log.d("BiometricStatus", "Unexpected error.")
                BiometricAuthStatus.NOT_AVAILABLE
            }
        }
    }

    /**
     * @param onFailed called when the Fingerprint or faceId is presented but the verification fails.
     * @param onError called when the system cannot display the Fingerprint/faceId dialog.
     */
    fun promptBiometricAuth(
        title: String,
        subTitle: String,
        negativeButtonText: String,
        fragmentActivity: FragmentActivity,
        onSuccess: (result: BiometricPrompt.AuthenticationResult) -> Unit,
        onFailed: () -> Unit,
        onError: (errorCode: Int, errString: CharSequence) -> Unit
    ) {
        when (isBiometricAuthAvailable()) {
            BiometricAuthStatus.NOT_AVAILABLE -> {
                onError(BiometricAuthStatus.NOT_AVAILABLE.code, "Not available for this device")
                return
            }
            BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE -> {
                onError(BiometricAuthStatus.TEMPORARY_NOT_AVAILABLE.code, "Not available at this moment")
                return
            }
            BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED -> {
                onError(BiometricAuthStatus.AVAILABLE_BUT_NOT_ENROLLED.code, "You should add a fingerprint or face id first")
                return
            }
            else -> Unit
        }

        biometricPrompt =
            BiometricPrompt(
                fragmentActivity,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        onSuccess(result)
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        val errorMessage = when (errorCode) {
                            BiometricPrompt.ERROR_CANCELED -> "Authentication canceled by the user."
                            BiometricPrompt.ERROR_HW_NOT_PRESENT -> "Hardware not present."
                            BiometricPrompt.ERROR_HW_UNAVAILABLE -> "Hardware unavailable."
                            BiometricPrompt.ERROR_UNABLE_TO_PROCESS -> "Unable to process biometric data."
                            BiometricPrompt.ERROR_TIMEOUT -> "Authentication timed out."
                            BiometricPrompt.ERROR_NO_BIOMETRICS -> "No biometrics enrolled."
                            else -> "Authentication error: $errString"
                        }
                        Log.d("BiometricError", "Error Code: $errorCode, Error Message: $errorMessage")
                        onError(errorCode, errorMessage)
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        onFailed()
                    }
                }
            )
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subTitle)
            .setNegativeButtonText(negativeButtonText)
            .build()
        biometricPrompt.authenticate(promptInfo)
    }
}
