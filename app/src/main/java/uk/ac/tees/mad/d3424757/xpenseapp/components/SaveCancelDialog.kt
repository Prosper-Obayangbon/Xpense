package uk.ac.tees.mad.d3424757.xpenseapp.components

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

/**
 * Composable function that displays a dialog asking the user if they want to save or cancel their changes.
 *
 * @param openDialog Boolean that controls whether the dialog is visible or not.
 * @param onSave Lambda function to handle the action when the "Save" button is clicked.
 * @param onCancel Lambda function to handle the action when the "Cancel" button is clicked.
 * @param onDismiss Lambda function to handle the action when the dialog is dismissed.
 */
@Composable
fun SaveCancelDialog(
    openDialog: Boolean,
    onSave: () -> Unit,  // Action to save data
    onCancel: () -> Unit, // Action to cancel
    onDismiss: () -> Unit // Action to dismiss the dialog
) {

    // Display the dialog when openDialog is true
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss() // Optional: Perform an action when dialog is dismissed (e.g., saving or cleanup)
            },
            title = {
                Text(text = "Save Changes?") // Title of the dialog
            },
            text = {
                Text("Do you want to save your changes or cancel?") // Message content of the dialog
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSave() // Trigger save action when the user clicks "Save"
                    }
                ) {
                    Text("Save") // Label for the save button
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onCancel() // Trigger cancel action when the user clicks "Cancel"
                    }
                ) {
                    Text("Cancel") // Label for the cancel button
                }
            }
        )
    }
}

/**
 * Preview for the SaveCancelDialog composable function.
 * This allows you to see how the dialog would look in the UI preview.
 */
@Preview
@Composable
fun PreviewSaveCancelDialog() {
    // Display the dialog in the preview with openDialog set to true.
    SaveCancelDialog(
        openDialog = true,
        onSave = { /* Handle save action */ },
        onCancel = { /* Handle cancel action */ },
        onDismiss = { /* Handle dismiss action */ }
    )
}
