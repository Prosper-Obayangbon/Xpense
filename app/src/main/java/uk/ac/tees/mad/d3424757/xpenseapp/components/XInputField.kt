package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme


/**
 * A composable input field for text input with customizable properties.
 *
 * @param value The current text value of the input field.
 * @param onValueChange A lambda function that is triggered when the input value changes.
 * @param label The label displayed above the input field.
 * @param keyboardType The type of keyboard to display (default is [KeyboardType.Text]).
 * @param isPassword Indicates whether the input field is for a password (default is false).
 */
@Composable
fun XInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    // Outlined text field for input
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType // Set keyboard type
        ),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None, // Handle password visibility
        modifier = Modifier
            .fillMaxWidth() // Full width input field
            .padding(16.dp) // Padding for the field
    )
}
@Preview
@Composable
fun InputPreview(){
        XpenseAppTheme{
            XInputField(value = "heh" , onValueChange = {"hello"}, label = "hello")
        }

}