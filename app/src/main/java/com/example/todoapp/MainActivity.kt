package com.example.todoapp

import android.os.Bundle
import android.widget.Space
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainPage()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage() {
    val myContext = LocalContext.current

    val todoName = remember {
        mutableStateOf("")
    }

    val itemList = readData(myContext)

    val focusManager = LocalFocusManager.current

    val deleteDialogStatus = remember {
        mutableStateOf(false)
    }

    val clickedItemIndex = remember {
        mutableStateOf(0)
    }

    val updateDialogStatus = remember {
        mutableStateOf(false)
    }

    val clickedItem = remember {
        mutableStateOf("")
    }

    val textDialogStatus = remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = todoName.value,
                onValueChange = {
                    todoName.value = it
                },
                label = {Text(text = "Enter TODO")},
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Green,
                    unfocusedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary,
//                    textColor = Color.White,
                    cursorColor = Color.White
                ),
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .border(1.dp, Color.Black, RoundedCornerShape(5.dp))
                    .weight(7F)
                    .height(60.dp),
                textStyle = TextStyle(textAlign = TextAlign.Center)
            )

            Spacer(modifier = Modifier.width(20.dp))

            Button(
                onClick = {
                    if (todoName.value.isNotEmpty()) {
                        itemList.add(todoName.value)
                        writeData(itemList, myContext)
                        todoName.value = ""
                        focusManager.clearFocus()
                    } else {
                        Toast.makeText(myContext, "Please enter a TODO", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .weight(3F)
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(id = R.color.green),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(5.dp),
                border = BorderStroke(1.dp, Color.Black)
            ) {
                Text(text = "Add", fontSize = 20.sp)
            }
        }

        LazyColumn {
            items(
                count = itemList.size,
                itemContent = { index ->
                    val item = itemList[index]

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 1.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item,
                                color = Color.White,
                                fontSize = 18.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .width(300.dp)
                                    .clickable {
                                        clickedItem.value = item
                                        textDialogStatus.value = true
                                    }
                            )

                            Row() {
                                IconButton(
                                    onClick = {
                                        updateDialogStatus.value = true
                                        clickedItemIndex.value = index
                                        clickedItem.value = item
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Edit,
                                        contentDescription = "edit",
                                        tint = Color.White)
                                }

                                IconButton(
                                    onClick = {
                                        deleteDialogStatus.value = true
                                        clickedItemIndex.value = index
                                    }
                                ) {
                                    Icon(Icons.Filled.Delete,
                                        contentDescription = "delete",
                                        tint = Color.White)
                                }
                            }
                        }
                    }
                }
            )
        }

        if (deleteDialogStatus.value) {
            AlertDialog(
                onDismissRequest = { deleteDialogStatus.value = false },
                title = { Text(text = "Delete") },
                text = { Text(text = "Do you want to delete this item from the list?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            itemList.removeAt(clickedItemIndex.value)
                            writeData(itemList, myContext)
                            deleteDialogStatus.value = false
                            Toast.makeText(myContext, "Item was removed from the list.", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = "YES")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            deleteDialogStatus.value = false
                        }
                    ) {
                        Text(text = "NO")
                    }
                }
            )
        }

        if (updateDialogStatus.value) {
            AlertDialog(
                onDismissRequest = { updateDialogStatus.value = false },
                title = { Text(text = "Update") },
                text = {
                    TextField(
                        value = clickedItem.value,
                        onValueChange = {
                            clickedItem.value = it
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            itemList[clickedItemIndex.value] = clickedItem.value
                            writeData(itemList, myContext)
                            updateDialogStatus.value = false
                            Toast.makeText(myContext, "Item was updated.", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = "YES")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            updateDialogStatus.value = false
                        }
                    ) {
                        Text(text = "NO")
                    }
                }
            )
        }

        if (textDialogStatus.value) {
            AlertDialog(
                onDismissRequest = { textDialogStatus.value = false },
                title = { Text(text = "TODO item") },
                text = {
                    Text(text = clickedItem.value)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            textDialogStatus.value = false
                        }
                    ) {
                        Text(text = "OK")
                    }
                }
            )
        }
    }
}

//package com.example.todoapp
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.animation.animateContentSize
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.gestures.detectHorizontalDragGestures
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.runtime.saveable.rememberSaveable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.pointer.pointerInput
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import kotlinx.coroutines.launch
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//import android.app.DatePickerDialog
//import android.content.Context
//import java.util.Calendar
//import android.app.TimePickerDialog
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            ToDoApp()
//        }
//    }
//}
//
//@Composable
//fun ToDoApp() {
//    val navController = rememberNavController()
//    var darkMode by rememberSaveable { mutableStateOf(false) }
//    var tasks by rememberSaveable { mutableStateOf(listOf(
//        Task("Buy Groceries", "Milk, Eggs, Bread", "2025-03-25", "14:30:00", false),
//        Task("Meeting", "Project discussion", "2025-03-26", "10:00:00", false)
//    )) }
//    MaterialTheme(colorScheme = if (darkMode) darkColorScheme() else lightColorScheme()) {
//        Scaffold(
//            bottomBar = { BottomNavigationBar(navController) }
//        ) { paddingValues ->
//            NavHost(
//                navController = navController,
//                startDestination = "home",
//                modifier = Modifier.padding(paddingValues)
//            ) {
//                composable("home") { HomeScreen(navController, tasks, onUpdateTasks = { tasks = it }) }
//                composable("statistics") { StatisticsScreen(tasks) }
//                composable("settings") { SettingsScreen(darkMode, onDarkModeToggle = { darkMode = it }) }
//                composable("add") { AddEditTaskScreen(navController, onUpdateTasks = { tasks = it }, tasks) }
//            }
//        }
//    }
//}
//
//@Composable
//fun BottomNavigationBar(navController: NavHostController) {
//    NavigationBar {
//        listOf("home" to "Home", "statistics" to "Statistics", "settings" to "Settings").forEach { (route, label) ->
//            NavigationBarItem(
//                selected = false,
//                onClick = { navController.navigate(route) },
//                label = { Text(label) },
//                icon = {}
//            )
//        }
//    }
//}
//
//@Composable
//fun TaskSection(
//    title: String,
//    tasks: List<Task>,
//    expanded: Boolean,
//    onToggle: () -> Unit,
//    navController: NavHostController,
//    onUpdateTasks: (List<Task>) -> Unit,
//    allTasks: List<Task>
//) {
//    var isExpanded by rememberSaveable { mutableStateOf(expanded) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .animateContentSize()
//    ) {
//        Text(
//            text = "$title ${if (isExpanded) "▼" else "▲"}",
//            fontSize = 14.sp,
//            fontWeight = FontWeight.Bold,
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Gray.copy(alpha = 0.2f))
//                .clickable { isExpanded = !isExpanded }
//                .padding(8.dp)
//        )
//        if (isExpanded) {
//            if (tasks.isEmpty()) {
//                Text(
//                    "No tasks",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                    fontSize = 12.sp
//                )
//            } else {
//                LazyColumn(modifier = Modifier.fillMaxWidth()) {
//                    items(tasks) { task ->
//                        TaskCard(
//                            task,
//                            onEdit = { navController.navigate("add") },
//                            onDelete = { onUpdateTasks(allTasks - task) },
//                            onDone = {
//                                val updatedTasks = allTasks.map {
//                                    if (it.title == task.title && it.description == task.description)
//                                        it.copy(isDone = !it.isDone)
//                                    else it
//                                }
//                                onUpdateTasks(updatedTasks)
//                            }
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun HomeScreen(navController: NavHostController, tasks: List<Task>, onUpdateTasks: (List<Task>) -> Unit) {
//    val doneTasks = tasks.filter { it.isDone }
//    val toDoTasks = tasks.filter { !it.isDone }
//
//    Box(modifier = Modifier.fillMaxSize()) {
//        Column(modifier = Modifier.fillMaxSize()) {
//            TaskSection("To Do", toDoTasks, expanded = true, onToggle = {}, navController, onUpdateTasks, tasks)
//            TaskSection("Done", doneTasks, expanded = true, onToggle = {}, navController, onUpdateTasks, tasks)
//        }
//        FloatingActionButton(
//            onClick = { navController.navigate("add") },
//            modifier = Modifier
//                .align(Alignment.BottomEnd)
//                .padding(16.dp)
//        ) {
//            Text("+")
//        }
//    }
//}
//
//@Composable
//fun TaskCard(task: Task, onEdit: () -> Unit, onDelete: () -> Unit, onDone: () -> Unit) {
//    var expanded by remember { mutableStateOf(false) }
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp)
//            .pointerInput(Unit) {
//                detectHorizontalDragGestures { _, dragAmount ->
//                    if (dragAmount < -100) onDone()
//                    if (dragAmount > 100) onDelete()
//                }
//            }
//            .background(if (task.isDone) Color.Gray else Color.White),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//    ) {
//        Row(
//            modifier = Modifier.padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(modifier = Modifier.weight(1f)) {
//                Text(task.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
//                Text(task.description, maxLines = 1, overflow = TextOverflow.Ellipsis)
//                Text("Date: ${task.date}")
//                Text("Time: ${task.time}")
//            }
//            Checkbox(checked = task.isDone, onCheckedChange = { onDone() })
//            IconButton(onClick = { expanded = true }) { Text("⋮") }
//            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
//                DropdownMenuItem(text = { Text("Edit") }, onClick = { onEdit(); expanded = false })
//                DropdownMenuItem(text = { Text("Delete") }, onClick = { onDelete(); expanded = false })
//            }
//        }
//    }
//}
//
//@Composable
//fun StatisticsScreen(tasks: List<Task>) {
//    val doneCount = tasks.count { it.isDone }
//    val undoneCount = tasks.size - doneCount
//    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
//        Text("Done: $doneCount", fontSize = 20.sp)
//        Text("Undone: $undoneCount", fontSize = 20.sp)
//    }
//}
//
//@Composable
//fun SettingsScreen(darkMode: Boolean, onDarkModeToggle: (Boolean) -> Unit) {
//    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            Text("Dark Mode")
//            Switch(checked = darkMode, onCheckedChange = { onDarkModeToggle(it) })
//        }
//    }
//}
//
//@Composable
//fun AddEditTaskScreen(
//    navController: NavHostController,
//    onUpdateTasks: (List<Task>) -> Unit,
//    tasks: List<Task>,
//    task: Task? = null
//) {
//    val context = LocalContext.current
//    var title by rememberSaveable { mutableStateOf(task?.title ?: "") }
//    var description by rememberSaveable { mutableStateOf(task?.description ?: "") }
//    var dateTime by rememberSaveable { mutableStateOf(task?.let { "${it.date} ${it.time}" } ?: "2025-03-25 14:30:00") }
//
//    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//        TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
//        TextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
//        Spacer(modifier = Modifier.height(16.dp))
//        Text(text = "Date & Time: $dateTime", modifier = Modifier.clickable {
//            val calendar = Calendar.getInstance()
//            val datePicker = DatePickerDialog(
//                context,
//                { _, year, month, dayOfMonth ->
//                    val timePicker = TimePickerDialog(
//                        context,
//                        { _, hour, minute ->
//                            dateTime = "$year-${month + 1}-$dayOfMonth ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:00"
//                        },
//                        calendar.get(Calendar.HOUR_OF_DAY),
//                        calendar.get(Calendar.MINUTE),
//                        true
//                    )
//                    timePicker.show()
//                },
//                calendar.get(Calendar.YEAR),
//                calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)
//            )
//            datePicker.show()
//        })
//        Spacer(modifier = Modifier.weight(1f))
//        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
//            Text(text = "Cancel", modifier = Modifier.clickable { navController.popBackStack() }.padding(8.dp), color = Color.Blue)
//            Button(onClick = {
//                val newTask = Task(title, description, dateTime.split(" ")[0], dateTime.split(" ")[1], false)
//                onUpdateTasks(tasks + newTask)
//                navController.popBackStack()
//            }) { Text("Save") }
//        }
//    }
//}
//
//data class Task(val title: String, val description: String, val date: String, val time: String, var isDone: Boolean)
