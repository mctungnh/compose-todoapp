package com.example.todoapp.repository

import com.example.todoapp.data.TaskDao
import com.example.todoapp.data.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun insert(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    fun getAllTasks(): Flow<List<TaskEntity>> {
        return taskDao.getAllTasks()
    }

    suspend fun getTaskById(taskId: Int): TaskEntity? {
        return taskDao.getTaskById(taskId)
    }
}