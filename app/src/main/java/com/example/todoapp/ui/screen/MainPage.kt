package com.example.todoapp.ui.screen

import android.widget.Toast
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.todoapp.R
import com.example.todoapp.data.TaskEntity
import com.example.todoapp.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navController: NavHostController, taskViewModel: TaskViewModel = viewModel()) {
    val myContext = LocalContext.current

    val todoName = remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val deleteDialogStatus = remember { mutableStateOf(false) }
    val clickedItemIndex = remember { mutableIntStateOf(0) }
    val updateDialogStatus = remember { mutableStateOf(false) }
    val clickedItem = remember { mutableStateOf("") }
    val textDialogStatus = remember { mutableStateOf(false) }

    val itemList = taskViewModel.allTasks.observeAsState(listOf()).value

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = todoName.value,
                onValueChange = { todoName.value = it },
                label = { Text(text = "Enter TODO") },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color.Green,
                    unfocusedLabelColor = Color.White,
                    containerColor = MaterialTheme.colorScheme.primary,
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
                        val newTask = TaskEntity(title = todoName.value, description = "", isCompleted = false, timestamp = System.currentTimeMillis())
                        taskViewModel.addTask(newTask)
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
                                text = item.title,
                                color = Color.White,
                                fontSize = 18.sp,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .width(300.dp)
                                    .clickable {
                                        clickedItem.value = item.title
                                        textDialogStatus.value = true
                                    }
                            )

                            Row {
                                IconButton(
                                    onClick = {
                                        updateDialogStatus.value = true
                                        clickedItemIndex.value = index
                                        clickedItem.value = item.title
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Edit,
                                        contentDescription = "edit",
                                        tint = Color.White
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        deleteDialogStatus.value = true
                                        clickedItemIndex.value = index
                                    }
                                ) {
                                    Icon(
                                        Icons.Filled.Delete,
                                        contentDescription = "delete",
                                        tint = Color.White
                                    )
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
                            taskViewModel.deleteTask(itemList[clickedItemIndex.value])
                            deleteDialogStatus.value = false
                            Toast.makeText(myContext, "Item was removed from the list.", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = "YES")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { deleteDialogStatus.value = false }
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
                        onValueChange = { clickedItem.value = it }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val updatedTask = itemList[clickedItemIndex.value].copy(title = clickedItem.value)
                            taskViewModel.updateTask(updatedTask)
                            updateDialogStatus.value = false
                            Toast.makeText(myContext, "Item was updated.", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Text(text = "YES")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { updateDialogStatus.value = false }
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
                text = { Text(text = clickedItem.value) },
                confirmButton = {
                    TextButton(
                        onClick = { textDialogStatus.value = false }
                    ) {
                        Text(text = "OK")
                    }
                }
            )
        }
    }
}