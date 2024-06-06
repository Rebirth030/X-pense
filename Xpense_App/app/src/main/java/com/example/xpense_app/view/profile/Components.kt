package com.example.xpense_app.view.profile

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xpense_app.R
import com.example.xpense_app.model.User
import com.example.xpense_app.model.UserRole


/**
 * Displays a button for saving user data.
 *
 * This composable function is responsible for rendering a button that allows the user to save their profile data.
 *
 * @param onSaveUser Callback function triggered when the save button is clicked. It passes a boolean value indicating
 *                    whether the button is clicked.
 */
@Composable
fun SaveButton(onSaveUser: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = {
                onSaveUser(true)
            },
            enabled = true,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Text(stringResource(R.string.save))
        }
    }
}

/**
 * Displays a drop-down menu for selecting the user's profile role.
 *
 * This composable function is responsible for rendering a drop-down menu that allows the user to select
 * their profile role from the provided options.
 *
 * @param currentUser A [MutableState] containing the [User] object for the currently logged-in user.
 * @param onSelectedProfile Callback function triggered when a profile role is selected from the drop-down menu.
 *                          It passes the selected [UserRole] to the caller.
 */
@Composable
@ExperimentalMaterial3Api
fun DropDown(
    currentUser: MutableState<User>,
    onSelectedProfile: (UserRole) -> Unit
) {
    val context = LocalContext.current
    var isExpended by remember {
        mutableStateOf(false)
    }
    val userRole =
        requireNotNull(UserRole.entries.find { role -> role.name == currentUser.value.role }) {
            Toast.makeText(
                context,
                stringResource(R.string.user_role_not_found),
                Toast.LENGTH_LONG
            ).show()
            "Custom"
        }
    var selectedOption by remember {
        mutableStateOf(
            userRole.description
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExposedDropdownMenuBox(
            expanded = isExpended,
            onExpandedChange = { isExpended = !isExpended }
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpended) }
            )
            ExposedDropdownMenu(expanded = isExpended, onDismissRequest = { isExpended = false }) {
                UserRole.entries.forEachIndexed { _, option ->
                    DropdownMenuItem(
                        text = { Text(text = option.description) },
                        onClick = {
                            selectedOption = option.description
                            isExpended = false
                            onSelectedProfile(option)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}


/**
 * Displays input fields for entering hours and minutes.
 *
 * This composable function is responsible for rendering input fields that allow the user to enter hours and minutes.
 *
 * @param label The label for the input fields.
 * @param readOnly A boolean indicating whether the input fields are read-only.
 * @param inputHour The value of the hour input field.
 * @param inputMinute The value of the minute input field.
 * @param inputOn A boolean indicating whether the input is enabled.
 * @param onInputChange Callback function triggered when the input value is changed. It passes the new value as a double.
 * @param onSwitchToggle Callback function triggered when the switch toggle is changed. It passes a boolean indicating
 *                       whether the switch is toggled.
 */
@Composable
fun HourMinuteInput(
    label: String,
    readOnly: Boolean,
    inputHour: Int,
    inputMinute: Int,
    inputOn: Boolean,
    onInputChange: (Double) -> Unit,
    onSwitchToggle: (Boolean) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        Text(
            text = label,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        )
        Row(modifier = Modifier.padding(top = 10.dp)) {
            IntegerInput(
                stringResource(R.string.hours),
                readOnly,
                inputHour.toString(),
                maxVal = 10,
                onInputSelected = {
                    if (it.isNotBlank()) {
                        onInputChange(it.toDouble() + (inputMinute / 60))
                    }
                }
            )
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = ":",
                style = TextStyle(fontSize = 40.sp)
            )
            IntegerInput(
                stringResource(R.string.minutes),
                readOnly,
                inputMinute.toString(),
                maxVal = 60,
                onInputSelected = {
                    if (it.isNotBlank()) {
                        onInputChange(inputHour + (it.toDouble() / 60))
                    }
                }
            )
            Spacer(modifier = Modifier.weight(1f))
            SwitchButton(
                inputOn,
                onSwitchToggle = { onSwitchToggle(it) }
            )
        }
    }
}

/**
 * Displays an input field for entering hours.
 *
 * This composable function is responsible for rendering an input field that allows the user to enter hours.
 *
 * @param label The label for the input field.
 * @param readOnly A boolean indicating whether the input field is read-only.
 * @param inputValue The value of the hour input field.
 * @param onInputChange Callback function triggered when the input value is changed. It passes the new value as an integer.
 */
@Composable
fun HourInput(
    label: String,
    readOnly: Boolean,
    inputValue: Int,
    onInputChange: (Int) -> Unit
) {
    Row(modifier = Modifier.padding(10.dp))
    {
        Text(
            text = "$label:",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        )
        Spacer(modifier = Modifier.weight(1f))
        IntegerInput(
            stringResource(R.string.hours),
            readOnly,
            inputValue.toString(),
            maxVal = 100,
            onInputSelected = {
                if (it.isNotBlank()) {
                    onInputChange(it.toInt())
                }
            },
        )
    }
}

/**
 * Displays a toggle switch for enabling/disabling notifications.
 *
 * This composable function is responsible for rendering a toggle switch that allows the user to enable or disable notifications.
 *
 * @param notificationsOn A boolean indicating whether notifications are currently enabled.
 * @param onSwitchToggle Callback function triggered when the toggle switch is changed. It passes a boolean indicating
 *                       whether notifications are toggled.
 */
@Composable
fun NotificationInput(
    notificationsOn: Boolean,
    onSwitchToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.notifications),
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        )
        Spacer(modifier = Modifier.weight(1f))
        SwitchButton(
            notificationsOn,
            onSwitchToggle = { onSwitchToggle(it) }
        )
    }
}

/**
 * Displays an input field for entering integer values.
 *
 * This composable function is responsible for rendering an input field that allows the user to enter integer values.
 *
 * @param label The label for the input field.
 * @param readOnly A boolean indicating whether the input field is read-only.
 * @param inputValue The value of the integer input field.
 * @param maxVal The maximum allowed value for the input field.
 * @param onInputSelected Callback function triggered when the input value is selected. It passes the new value as a string.
 */
@Composable
fun IntegerInput(
    label: String,
    readOnly: Boolean,
    inputValue: String,
    maxVal: Int,
    onInputSelected: (String) -> Unit
) {
    if (!readOnly) {
        MutableInput(label, 0, maxVal, onInputSelected = { onInputSelected(it) })
    } else {
        TextField(
            modifier = Modifier.width(100.dp),
            readOnly = true,
            value = inputValue,
            onValueChange = {
                onInputSelected(it)
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }
}

/**
 * Displays an input field for entering mutable values within a specified range.
 *
 * This composable function is responsible for rendering an input field that allows the user to enter mutable values
 * within a specified range.
 *
 * @param label The label for the input field.
 * @param minVal The minimum allowed value for the input field.
 * @param maxVal The maximum allowed value for the input field.
 * @param onInputSelected Callback function triggered when the input value is selected. It passes the new value as a string.
 */
@Composable
fun MutableInput(
    label: String,
    minVal: Int,
    maxVal: Int,
    onInputSelected: (String) -> Unit
) {
    var input by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier.width(100.dp),
        value = input,
        onValueChange = {
            if (it.isBlank() || (it.toIntOrNull() in minVal..maxVal)) {
                input = it
                onInputSelected(it)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}

/**
 * Displays a switch button for toggling a boolean value.
 *
 * This composable function is responsible for rendering a switch button that allows the user to toggle a boolean value.
 *
 * @param inputOn A boolean indicating whether the switch button is currently on.
 * @param onSwitchToggle Callback function triggered when the switch button is toggled. It passes a boolean indicating
 *                       whether the switch button is toggled.
 */
@Composable
fun SwitchButton(
    inputOn: Boolean,
    onSwitchToggle: (Boolean) -> Unit
) {
    Switch(
        checked = inputOn,
        onCheckedChange = {
            onSwitchToggle(it)
        },
        thumbContent = if (inputOn) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            null
        }
    )
}

/**
 * Composable function to create a mutable hour and minute input field.
 *
 * @param label The label of the input field.
 * @param inputHour The current hour value.
 * @param inputMinute The current minute value.
 * @param inputOn The current state of the switch.
 * @param onInputChange The callback function to handle input changes.
 * @param onSwitchToggle The callback function to handle switch changes.
 */
@Composable
fun MutableHourMinuteInput(
    label: String,
    inputHour: Int,
    inputMinute: Int,
    inputOn: Boolean,
    onInputChange: (Double) -> Unit,
    onSwitchToggle: (Boolean) -> Unit
) {
    var hour by remember { mutableStateOf(inputHour.toString()) }
    var minute by remember { mutableStateOf(inputMinute.toString()) }
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        Text(
            text = label,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        )
        Row(modifier = Modifier.padding(top = 10.dp)) {
            TextField(
                modifier = Modifier.width(100.dp),
                value = hour,
                onValueChange = {
                    if (it.isBlank() || (it.toIntOrNull() in 0..10)) {
                        hour = it
                        if (it.isNotBlank()) {
                            onInputChange(it.toDouble() + (inputMinute / 60))
                        } else {
                            onInputChange(0.0 + (inputMinute / 60))
                        }
                    }
                },
                label = { Text(stringResource(R.string.hours)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = ":",
                style = TextStyle(fontSize = 40.sp)
            )
            TextField(
                modifier = Modifier.width(100.dp),
                value = minute,
                onValueChange = {
                    if (it.isBlank() || (it.toIntOrNull() in 0..60)) {
                        minute = it
                        if (it.isNotBlank()) {
                            onInputChange(inputHour + (it.toDouble() / 60))
                        } else {
                            onInputChange(inputHour + 0.0)
                        }
                    }
                },
                label = { Text(stringResource(R.string.minutes)) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.weight(1f))
            SwitchButton(
                inputOn,
                onSwitchToggle = { onSwitchToggle(it) }
            )
        }
    }
}

/**
 * Displays an input field for entering mutable hour values.
 *
 * This function is responsible for rendering an input field that allows the user to enter mutable hour values.
 *
 * @param inputHour The value of the hour input field.
 * @param onInputSelected Callback function triggered when the hour input value is selected. It passes the new value as an integer.
 */
@Composable
fun MutableHourInput(inputHour: Int, onInputSelected: (Int) -> Unit) {
    var hour by remember { mutableStateOf(inputHour.toString()) }
    TextField(
        modifier = Modifier.width(100.dp),
        value = hour,
        onValueChange = {
            if (it.isBlank() || (it.toIntOrNull() in 0..100)) {
                hour = it
                if (it.isNotBlank()) {
                    onInputSelected(it.toInt())
                } else {
                    onInputSelected(0)
                }
            }
        },
        label = { Text(stringResource(R.string.hours)) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}