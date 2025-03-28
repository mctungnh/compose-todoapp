package com.example.todoapp.viewmodel

import android.app.Application
import androidx.lifecycle.*
import androidx.lifecycle.asLiveData
import com.example.todoapp.data.TaskDao
import com.example.todoapp.data.TaskDatabase
import com.example.todoapp.data.TaskEntity
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val taskDao: TaskDao = TaskDatabase.getDatabase(application).taskDao()
    val allTasks: LiveData<List<TaskEntity>> = taskDao.getAllTasks().asLiveData()

    fun addTask(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.insertTask(task)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.updateTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskDao.deleteTask(task)
        }
    }
}
