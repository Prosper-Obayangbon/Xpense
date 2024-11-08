package uk.ac.tees.mad.d3424757.xpenseapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import uk.ac.tees.mad.d3424757.xpenseapp.R
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
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)?= null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    var showPassword by remember { mutableStateOf(false) } // State for password visibility


    // Outlined text field for input
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = keyboardType,
        ),
        singleLine = true,
        visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None, // Handle password visibility
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        if (showPassword){
                            painterResource(id = R.drawable.visibility)
                        } else painterResource(id = R.drawable.visibility_off),
                        contentDescription = if (showPassword) "Hide password" else "Show password"
                    )
                }
            }
        },

        modifier = Modifier
            .fillMaxWidth() // Full width input field
            .padding(bottom = 16.dp) // Padding for the field
    )
}
//@Preview
//@Composable
//fun InputPreview(){
//        XpenseAppTheme{
//            XInputField(value = "heh" , onValueChange = {"hello"}, label = "hello")
//        }
//
//}