package uk.ac.tees.mad.d3424757.xpenseapp.components

import android.widget.Toast
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SaveCancelDialog(
    openDialog : Boolean,
    onSave: () -> Unit,  // Action to save data
    onCancel: () -> Unit, // Action to cancel
    onDismiss: () -> Unit // Action to dismiss the dialog

) {

    // Show the dialog
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss() // Optional: Perform an action when dialog is dismissed
            },
            title = {
                Text(text = "Save Changes?")
            },
            text = {
                Text("Do you want to save your changes or cancel?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSave() // Trigger save action
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        onCancel() // Trigger cancel action
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

