package com.example.xpense_app.view.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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

@Composable
@ExperimentalMaterial3Api
fun Profile() {
    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        Spacer(modifier = Modifier.height(70.dp)) // header
        DropDown()
        Spacer(modifier = Modifier.height(30.dp))
        HourMinuteInput("Pause nach maximal")
        Spacer(modifier = Modifier.height(20.dp))
        HourMinuteInput("Feierabend nach maximal")
        Spacer(modifier = Modifier.height(20.dp))
        HourInput("Bevorzugte\nWochenstunden")
        Spacer(modifier = Modifier.height(20.dp))
        NotificationInput()
    }
}

@Composable
@ExperimentalMaterial3Api
fun DropDown() {
    val options = listOf("Employee", "Freelancer", "Costumed", "Import")
    var isExpended by remember {
        mutableStateOf(false)
    }
    var selectedOption by remember {
        mutableStateOf(options[2])
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
                options.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            selectedOption = options[index]
                            isExpended = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}

@Composable
fun HourMinuteInput(label: String) {
    Column(modifier = Modifier.padding(horizontal = 10.dp)) {
        Text(
            text = label,
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        )
        Row(modifier = Modifier.padding(top = 10.dp)) {
            IntegerInput("Stunden", minVal = 1, maxVal = 10)
            Text(
                modifier = Modifier.padding(horizontal = 10.dp),
                text = ":",
                style = TextStyle(fontSize = 40.sp)
            )
            IntegerInput("Minuten", minVal = 1, maxVal = 60)
            Spacer(modifier = Modifier.weight(1f))
            SwitchButton()
        }
    }
}


@Composable
fun HourInput(label: String) {
    Row(modifier = Modifier.padding(10.dp))
    {
        Text(
            text = "$label:",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        )
        Spacer(modifier = Modifier.weight(1f))
        IntegerInput(label = "Stunden", minVal = 1, maxVal = 100)
    }
}


@Composable
fun NotificationInput() {
    Row(
        modifier = Modifier.padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Benachrichtigungen:",
            style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        )
        Spacer(modifier = Modifier.weight(1f))
        SwitchButton()
    }
}

@Composable
fun IntegerInput(
    label: String,
    minVal: Int,
    maxVal: Int
) {
    var input by remember { mutableStateOf("") }
    TextField(
        modifier = Modifier.width(100.dp),
        value = input,
        onValueChange = {
            // Check if the input is empty or a valid integer within the range
            if (it.isEmpty() || (it.toIntOrNull() in minVal..maxVal)) {
                input = it
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun SwitchButton() {
    var checked by remember { mutableStateOf(true) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
        },
        thumbContent = if (checked) {
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