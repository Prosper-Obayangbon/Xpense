package uk.ac.tees.mad.d3424757.xpenseapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class LegalViewModel : ViewModel() {
    val hasAgreedToTerms = mutableStateOf(false)
}
