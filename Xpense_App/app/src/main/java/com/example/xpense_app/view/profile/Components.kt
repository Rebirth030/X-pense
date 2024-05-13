package com.example.xpense_app.view.profile

import android.util.Log
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.decapitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xpense_app.model.User
import com.example.xpense_app.model.UserRole


@Composable
fun SaveButton(onSaveUser: (Boolean) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = {
                onSaveUser(true)
            },
            enabled = true,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }
    }
}

@Composable
@ExperimentalMaterial3Api
fun DropDown(
    currentUser: MutableState<User>,
    onSelectedProfile: (UserRole) -> Unit
) {
    var isExpended by remember {
        mutableStateOf(false)
    }
    var selectedOption by remember {
        mutableStateOf(
            UserRole.entries.find { role -> role.name == currentUser.value.role }!!.description
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
                "Hours",
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
                "Minutes",
                readOnly,
                inputMinute.toString(),
                maxVal = 60,
                onInputSelected = {
                    if(it.isNotBlank()) {
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
            "Hours",
            readOnly,
            inputValue.toString(),
            maxVal = 100,
            onInputSelected = {
                if(it.isNotBlank()) {
                    onInputChange(it.toInt())
                }
            },
        )
    }
}


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
            text = "Notifications:",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        )
        Spacer(modifier = Modifier.weight(1f))
        SwitchButton(
            notificationsOn,
            onSwitchToggle = { onSwitchToggle(it) }
        )
    }
}

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
            readOnly = readOnly,
            value = inputValue,
            onValueChange = {
                onInputSelected(it)
            },
            label = { Text(label) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
        )
    }
}

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
            Log.d("MUTABLE INPUT", it)
            if (it.isBlank()||(it.toIntOrNull() in minVal..maxVal)) {
                input = it
                onInputSelected(it)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}

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