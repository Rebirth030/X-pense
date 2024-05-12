package com.example.xpense_app.view.profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.xpense_app.controller.services.UserService
import com.example.xpense_app.model.User
import com.example.xpense_app.model.UserRole
import kotlin.math.floor

@Composable
@ExperimentalMaterial3Api
fun Profile(currentUser: MutableState<User>) {
    var role by remember {
        mutableStateOf(currentUser.value.role)
    }
    var forcedEndAfter by remember {
        mutableStateOf(currentUser.value.forcedEndAfter)
    }
    var forcedEndAfterOn by remember {
        mutableStateOf(currentUser.value.forcedEndAfterOn)
    }
    var forcedBreakAfter by remember {
        mutableStateOf(currentUser.value.forcedBreakAfter)
    }
    var forcedBreakAfterOn by remember {
        mutableStateOf(currentUser.value.forcedBreakAfterOn)
    }
    var notificationsOn by remember {
        mutableStateOf(currentUser.value.notification)
    }
    var weeklyWorkingHours by remember {
        mutableStateOf(currentUser.value.weeklyWorkingHours)
    }
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Spacer(modifier = Modifier.height(70.dp)) // header
        DropDown(
            currentUser,
            onSelectedProfile = {
                role = it.name
                when (it) {
                    UserRole.EMPLOYEE -> {
                        weeklyWorkingHours = 40
                        forcedBreakAfter = 4.0
                        forcedBreakAfterOn = true
                        forcedEndAfter = 8.0
                        forcedEndAfterOn = true
                        notificationsOn = false
                    }

                    UserRole.FREELANCER -> {
                        weeklyWorkingHours = 41
                        forcedBreakAfter = 5.0
                        forcedBreakAfterOn = false
                        forcedEndAfter = 10.0
                        forcedEndAfterOn = false
                        notificationsOn = false
                    }

                    UserRole.WORK_STUDENT -> {
                        Log.d("CHANGE ROLE", "Helo")
                        weeklyWorkingHours = 20
                        forcedBreakAfter = 3.0
                        forcedBreakAfterOn = false
                        forcedEndAfter = 6.0
                        forcedEndAfterOn = false
                        notificationsOn = false
                    }

                    UserRole.COSTUMED -> {
                        weeklyWorkingHours = 0
                        forcedBreakAfter = 0.0
                        forcedBreakAfterOn = false
                        forcedEndAfter = 0.0
                        forcedEndAfterOn = false
                        notificationsOn = false
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(30.dp))
        HourMinuteInput(
            "Forced break after",
            role != UserRole.COSTUMED.name,
            floor(forcedBreakAfter!!).toInt(),
            ((forcedBreakAfter!! - floor(forcedBreakAfter!!)) * 60).toInt(),
            forcedBreakAfterOn!!,
            onInputChange = {
                forcedEndAfter = it
            },
            onSwitchToggle = {
                forcedBreakAfterOn = it
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        HourMinuteInput(
            "Forced end after",
            role != UserRole.COSTUMED.name,
            floor(forcedEndAfter!!).toInt(),
            ((forcedEndAfter!! - floor(forcedEndAfter!!)) * 60).toInt(),
            forcedEndAfterOn!!,
            onInputChange = {
                forcedEndAfter = it
            },
            onSwitchToggle = {
                forcedEndAfterOn = it
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        HourInput(
            "Weekly\nworking hours",
            role != UserRole.COSTUMED.name,
            weeklyWorkingHours!!,
            onInputChange = {
                weeklyWorkingHours = it
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        NotificationInput(
            notificationsOn!!,
            onSwitchToggle = { notificationsOn = it }
        )
        Spacer(modifier = Modifier.weight(1f))
        SaveButton(onSaveUser = {
            currentUser.value.role = role
            currentUser.value.weeklyWorkingHours = weeklyWorkingHours
            currentUser.value.forcedBreakAfter = forcedBreakAfter
            currentUser.value.forcedBreakAfterOn = forcedBreakAfterOn
            currentUser.value.forcedEndAfter = forcedEndAfter
            currentUser.value.forcedEndAfterOn = forcedEndAfterOn
            currentUser.value.notification = false // default of every profile
            UserService.updateUser(
                currentUser.value,
                currentUser.value.token,
                onSuccess = {
                    currentUser.value = it
                },
                onError = {
                    it.printStackTrace()
                }
            )
        })
    }
}

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
        mutableStateOf(currentUser.value.role)
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
                        text = { Text(text = option.name) },
                        onClick = {
                            selectedOption = option.name
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
                String.format("%02d", inputHour),
                minVal = 1,
                maxVal = 10,
                onInputSelected = {
                    onInputChange(it.toDouble() + (inputMinute / 60))
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
                String.format("%02d", inputMinute),
                minVal = 1,
                maxVal = 60,
                onInputSelected = {
                    onInputChange(inputHour + (it.toDouble() / 60))
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
            String.format("%02d", inputValue),
            minVal = 1,
            maxVal = 100,
            onInputSelected = {
                onInputChange(it.toInt())
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
    minVal: Int,
    maxVal: Int,
    onInputSelected: (String) -> Unit
) {
    TextField(
        modifier = Modifier.width(100.dp),
        readOnly = readOnly,
        value = inputValue,
        onValueChange = {
            if (it.isEmpty() || (it.toIntOrNull() in minVal..maxVal)) {
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