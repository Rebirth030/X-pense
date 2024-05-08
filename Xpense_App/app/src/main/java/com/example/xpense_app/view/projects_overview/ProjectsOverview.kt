package com.example.xpense_app.view.projects_overview

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.xpense_app.controller.services.ProjectService
import com.example.xpense_app.model.Project
import com.example.xpense_app.model.User
import com.example.xpense_app.navigation.NavigationItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Composable function for the Projects Overview screen.
 * Displays a list of all projects in the app.
 *
 * @param user The user currently logged in.
 * @param navGraph The navigation controller.
 */
@Composable
fun ProjectsOverview(user: User, navGraph: NavHostController) {
    val projects = remember { mutableStateOf(listOf<Project>()) }
    Surface {
        Scaffold (
            floatingActionButton = {
                IconButton(
                    onClick = {
                        navGraph.navigate(NavigationItem.CreateProject.route)
                    },
                    modifier = Modifier.background(color = if (isSystemInDarkTheme()) Color(51, 51, 51) else Color(211, 211, 211), RoundedCornerShape(50))
                ) {
                    Icon(Icons.TwoTone.Add, contentDescription = "Create Project")
                }
            }
        ) { padding ->
            Column (
                Modifier
                    .fillMaxWidth()
                    .padding(padding)) {
                GetProjects(user, projects)
                ProjectsList(projects)
            }
        }
    }
}

/**
 * Composable function for a scrollable list of projects.
 *
 * @param projects The list of projects to display.
 */
@Composable
fun ProjectsList(projects: MutableState<List<Project>>) {
    val scrollState = rememberScrollState()
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .verticalScroll(scrollState)
    ){
        projects.value.forEach { project ->
            ProjectCard(project)
        }
    }
}

/**
 * Composable function for a single project card.
 *
 * @param project The project to display.
 */
@Composable
fun ProjectCard(project: Project) {
    Card (modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)) {
        Column (modifier = Modifier.padding(20.dp)) {
            Text(text = "Project: ${project.name.orEmpty()}", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(bottom = 5.dp))
            Text(text = "Description: ${project.description.orEmpty()}")
            Text(text = "Release Date: ${project.releaseDate.orEmpty()}")
            Text(text = "Company Id: ${project.companyId.toString()}")
            Text(text = "Expected Expense: ${project.expectedExpense.toString()}")
            Text(text = "Current Expense: ${project.currentExpense.toString()}")
        }
    }
}

/**
 * Function to get all projects from the server.
 *
 * @param user The user currently logged in.
 * @param projects The list of projects to update.
 */
@Composable
fun GetProjects(user: User, projects: MutableState<List<Project>>) {
    val context = LocalContext.current
    LaunchedEffect(key1 = "getProjects") {
        withContext(Dispatchers.IO) {
            ProjectService.getProjects(
                token = user.token,
                onSuccess = { projectList ->
                    projects.value = projectList
                },
                onError = { e ->
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}
