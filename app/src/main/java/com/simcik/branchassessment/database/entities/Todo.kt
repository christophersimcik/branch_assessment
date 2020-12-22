package com.simcik.branchassessment.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val todoId: Int,
    @ForeignKey
        (entity =ListWithTodos::class,
        parentColumns = ["toDoListId"],
        childColumns = ["listId"],
        onDelete = CASCADE
    )
    val listId: Int,
    var description: String,
    var isCompleted: Boolean,
    var color: Int
){
    override fun toString(): String {
        return "$listId|$description|$isCompleted|$color"
    }
}