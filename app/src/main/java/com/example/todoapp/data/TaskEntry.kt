package com.example.todoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val timestamp: Long, // Store date/time as epoch millis
    val isCompleted: Boolean = false
)
