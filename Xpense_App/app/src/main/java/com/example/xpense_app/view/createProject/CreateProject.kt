package com.example.xpense_app.view.createProject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.input.key.Key.Companion.U
import androidx.compose.ui.unit.dp
import com.example.xpense_app.controller.services.ProjectService
import com.example.xpense_app.model.Project
import com.example.xpense_app.model.User
import com.example.xpense_app.view.manualBooking.*
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

/**
 * Composable function for the screen to create a new project.
 */
@Composable
fun CreateProjectScreen(currentUser: MutableState<User>) {
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
                label = { Text("Project Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = projectDescription,
                onValueChange = { projectDescription = it },
                label = { Text("Project Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            DatePickerTextField(date = date, showDatePicker = showDatePicker)
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    create(name = projectName, description = projectDescription, releaseDate = date, currentUser)
                    showDialog = true
                 }) {
                Text("Create Project")
            }
            if (showDialog) {
                Success(
                    projectName,
                    projectDescription,
                    date,
                    onClose = { showDialog = false
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
 * Function to create a new project.
 * @param name The name of the project.
 * @param description The description of the project.
 * @param releaseDate The release date of the project.
 */
fun create(
    name: String,
    description: String,
    releaseDate: Date,
    currentUser: MutableState<User>
){
    val date =
        ZonedDateTime.ofInstant(releaseDate.toInstant(), ZoneId.systemDefault()).format(DateTimeFormatter.ISO_ZONED_DATE_TIME)
    val project = Project(null, name, description, date, null, null, 1L, 302L)
    ProjectService.createProject(project, currentUser.value.token, onSuccess = {}, onError = {})
}


/**
 * Composable function to display the success dialog after creating a project.
 * @param name The name of the project.
 * @param description The description of the project.
 * @param releaseDate The release date of the project.
 * @param onClose Callback function to close the dialog.
 */
@Composable
fun Success(name: String, description: String, releaseDate: Date, onClose:() -> Unit){
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(text = "Project Details") },
        text = {
            Column {
                Text("Name: ${name}")
                Text("Description: ${description}")
                Text("Release Date: ${releaseDate}")
            }
        },
        confirmButton = {
            Button(
                onClick = onClose
            ) {
                Text("Close")
            }
        })

}