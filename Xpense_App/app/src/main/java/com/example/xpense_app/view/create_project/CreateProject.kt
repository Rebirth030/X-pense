@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.xpense_app.view.create_project

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.xpense_app.R
import com.example.xpense_app.controller.services.ProjectService
import com.example.xpense_app.model.Project
import com.example.xpense_app.model.User
import com.example.xpense_app.view.manual_booking.DateDialog
import com.example.xpense_app.view.manual_booking.DatePickerTextField
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

/**
 * Composable function for the screen to create a new project.
 * Allows the user to input project details and handles the creation process.
 *
 * @param currentUser The current user of the application.
 * @param context The context of the calling component.
 */
@Composable
fun CreateProjectScreen(currentUser: MutableState<User>, context: Context) {
    var projectName by remember { mutableStateOf("") }
    var projectDescription by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var date by remember {
        mutableStateOf(Date())
    }
    val showDatePicker = remember {
        mutableStateOf(false)
    }
    if (showDatePicker.value) {
        DateDialog(onDateSelected = { date = it }, onDismiss = { showDatePicker.value = false })
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = projectName,
                onValueChange = { projectName = it },
                label = { Text(stringResource(R.string.project_name)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = projectDescription,
                onValueChange = { projectDescription = it },
                label = { Text(stringResource(R.string.project_description)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.secondary
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            DatePickerTextField(date = date, showDatePicker = showDatePicker)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    createProject(
                        context,
                        name = projectName,
                        description = projectDescription,
                        releaseDate = date,
                        currentUser,
                        onSuccess = { showDialog = it })
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                )
            ) {
                Text(stringResource(R.string.create_project))
            }
            if (showDialog) {
                Success(
                    projectName,
                    projectDescription,
                    date,
                    onClose = {
                        showDialog = false
                        projectName = ""
                        projectDescription = ""
                        date = Date()
                    }
                )
            }
        }
    }
}

/**
 * Creates a new project based on provided details.
 * Validates the input data and initiates the creation process.
 *
 * @param context The context of the calling component.
 * @param name The name of the project.
 * @param description The description of the project.
 * @param releaseDate The release date of the project.
 * @param currentUser The current user of the application.
 * @param onSuccess Callback function to handle success or failure of project creation.
 */
fun createProject(
    context: Context,
    name: String,
    description: String,
    releaseDate: Date,
    currentUser: MutableState<User>,
    onSuccess: (Boolean) -> Unit
) {
    try {
        require(name.isNotBlank()) { context.getString(R.string.please_enter_a_project_name) }
        require(description.isNotBlank()) { context.getString(R.string.please_enter_a_project_description) }
        require(!releaseDate.before(Date())) { context.getString(R.string.release_date_cannot_be_in_the_past) }
    } catch (e: IllegalArgumentException) {
        Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        onSuccess(false)
        return
    }

    val formattedDate = ZonedDateTime.ofInstant(releaseDate.toInstant(), ZoneId.systemDefault())
        .format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    val project =
        Project(null, name, description, formattedDate, null, null, currentUser.value.id)
    ProjectService.createProject(
        project,
        currentUser.value.token,
        onSuccess = { onSuccess(true) },
        onError = { onSuccess(false) }
    )
}


/**
 * Composable function to display a success dialog after creating a project.
 * Shows project details in an alert dialog upon successful creation.
 *
 * @param name The name of the project.
 * @param description The description of the project.
 * @param releaseDate The release date of the project.
 * @param onClose Callback function to close the dialog.
 */
@Composable
fun Success(name: String, description: String, releaseDate: Date, onClose: () -> Unit) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(text = stringResource(R.string.project_successfully_created)) },
        text = {
            Column {
                Text(stringResource(R.string.name, name))
                Text(stringResource(R.string.description2, description))
                Text(stringResource(R.string.release_date, releaseDate))
            }
        },
        confirmButton = {
            Button(
                onClick = onClose
            ) {
                Text(stringResource(R.string.close))
            }
        })
}