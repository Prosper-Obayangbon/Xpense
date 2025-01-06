package uk.ac.tees.mad.d3424757.xpenseapp.screens.legal

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.mintCream
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.tealGreen
import uk.ac.tees.mad.d3424757.xpenseapp.viewmodel.LegalViewModel

/**
 * Terms and Conditions page for the Xpense app.
 *
 * Displays the terms and conditions for using the app. Users must read and agree
 * to proceed with registration or app usage.
 *
 * @param navController Navigation controller to manage screen transitions.
 */
@Composable
fun TermsAndConditionsScreen(
    modifier: Modifier,
    navController: NavController,
    legalViewModel: LegalViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = mintCream
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column {
                /* ---------- Title ---------- */
                Text(
                    text = "Terms and Conditions",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = tealGreen
                )

                /* ---------- Terms Content ---------- */
                Text(
                    text = "Welcome to Xpense. Please read the terms and conditions carefully before using this application.\n\n" +
                            "1. **Acceptance of Terms**:\nBy using Xpense, you agree to these terms. If you disagree with any part, you may not use the app.\n\n" +
                            "2. **User Responsibilities**:\nYou are responsible for maintaining the confidentiality of your login details. You must not misuse the app or use it for unlawful purposes.\n\n" +
                            "3. **Privacy Policy**:\nWe are committed to protecting your privacy. Refer to our Privacy Policy for details.\n\n" +
                            "4. **Liability**:\nXpense is not liable for any financial loss or damages resulting from the use of this app.\n\n" +
                            "5. **Modifications**:\nWe reserve the right to update these terms at any time. Continued use of the app implies acceptance of the updated terms.\n\n" +
                            "6. **Governing Law**:\nThese terms are governed by the laws of the United Kingdom.\n\n" +
                            "If you have any questions about these terms, contact us at D3424757@tees.live.ac.uk.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            /* ---------- Agree Button ---------- */
            XButton(
                handleClick = {
                    legalViewModel.hasAgreedToTerms.value = true
                    navController.popBackStack()
                              },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
                text = "I Agree"
            )
        }
    }
}
