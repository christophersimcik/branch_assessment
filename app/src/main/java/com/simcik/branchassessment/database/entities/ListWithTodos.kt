package com.simcik.branchassessment.database.entities

import androidx.room.Embedded
import androidx.room.Relation

data class ListWithTodos(
    @Embedded val todoList: TodoList,
    @Relation(
        parentColumn = "todoListId",
        entityColumn = "listId"
    )
    val todos: List<Todo>
){
    fun getPercentageComplete(): Int{
        var count = 0f
        todos.forEach {todo ->
            if(todo.isCompleted) count ++
        }
        return if(todos.isNotEmpty()) (count / todos.size * 100).toInt() else 0
    }
    
    fun getListSize(): Int{
        return todos.size
    }
}