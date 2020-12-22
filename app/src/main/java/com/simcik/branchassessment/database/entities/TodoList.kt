package com.simcik.branchassessment.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoList(
    @PrimaryKey(autoGenerate = true)
    val todoListId: Int,
    val timestamp: Long,
    var name: String
)
