package uk.ac.tees.mad.d3424757.xpenseapp.screens.addTransaction

import XTopBar
import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.tees.mad.d3424757.xpenseapp.R
import uk.ac.tees.mad.d3424757.xpenseapp.components.XButton
import uk.ac.tees.mad.d3424757.xpenseapp.components.XHomeBackground
import uk.ac.tees.mad.d3424757.xpenseapp.components.XInputField
import uk.ac.tees.mad.d3424757.xpenseapp.ui.theme.XpenseAppTheme


@Composable
fun AddExpense(modifier: Modifier = Modifier, isIncome : Boolean = false) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }
    val categoryOptions = listOf("Shopping", "Subscription", "Food", "Transportation")
    var isCategoryDropdownExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf("") }
    var isMenuExpanded by remember { mutableStateOf(false) }
    var menuOptions = listOf("Save", "Cancel")

    XHomeBackground() {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            /*-----------------------Tob Bar------------------*/
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.back_arrow),
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
                Text(
                    text = if (isIncome) "Add Income" else "Add Expense",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White
                )
                IconButton(onClick = { isMenuExpanded = !isMenuExpanded }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false }
                    ) {
                        listOf("Save", "Cancel").forEach { option ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    if (option == "Save") {

                                    }
                                    isMenuExpanded = false
                                }
                            )

                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            // Form Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Category Field
                    Text("Category", style = MaterialTheme.typography.bodyMedium)
                    XInputField(
                        value = selectedCategory,
                        onValueChange = { },
                        trailingIcon = {
                            IconButton(onClick = { isCategoryDropdownExpanded = true }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                            }
                            DropdownMenu(
                                expanded = isCategoryDropdownExpanded,
                                onDismissRequest = { isCategoryDropdownExpanded = false }
                            ) {
                                //viewModel.categories.forEach { category ->
                                categoryOptions.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(text = category) },
                                        onClick = {
                                            //viewModel.updateCategory(category)
                                            isCategoryDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Amount Field
                    Text("Amount", style = MaterialTheme.typography.bodyMedium)
                    XInputField(
                        value = amount,
                        onValueChange = { },//viewModel.updateAmount(it) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_currency_pound_24),
                                contentDescription = "Pounds"
                            )
                        },
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Date Field
                    Text("Date", style = MaterialTheme.typography.bodyMedium)
                    XInputField(
                        value = selectedDate,
                        onValueChange = { },
                        trailingIcon = {
                            IconButton(onClick = {
                                val calendar = Calendar.getInstance()
                                DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        //viewModel.updateDate("$dayOfMonth/${month + 1}/$year")
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }) {
                                Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                            }
                        },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Description Field
                    Text("Description", style = MaterialTheme.typography.bodyMedium)
                    XInputField(
                        value = description,
                        onValueChange = {}, //viewModel.updateDescription(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        maxLine = 5,
                        keyboardType = KeyboardType.Text
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            XButton(
                text = "Save",
                handleClick = {}
            )
        }
    }
}
@Preview
@Composable
fun TPreview(){
    XpenseAppTheme{
        AddExpense()
    }
}