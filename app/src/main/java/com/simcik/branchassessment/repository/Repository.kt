package com.simcik.branchassessment.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.simcik.branchassessment.database.TodoDatabase
import com.simcik.branchassessment.database.entities.ListWithTodos
import com.simcik.branchassessment.database.entities.Todo
import com.simcik.branchassessment.database.entities.TodoList

class Repository(context: Context) {
    private val todoDatabase = TodoDatabase.getInstance(context)
    private val todoListDao = todoDatabase?.todoListDao()
    private val todoDao = todoDatabase?.todoDao()

    fun getLists(): LiveData<List<ListWithTodos>>? {
        return todoListDao?.getListOfTodos()
    }

    suspend fun addList(todoList: TodoList) {
        todoListDao?.addList(todoList)
    }

    suspend fun deleteList(todoList: TodoList) {
        todoListDao?.deleteList(todoList)
    }

    fun getTodoList(id: Int): LiveData<List<Todo>>?{
        return todoDao?.getListOfTodos(id)
    }

    suspend fun addTodo(todo: Todo) {
        todoDao?.addTodo(todo)
    }

    suspend fun deleteTodo(todo: Todo) {
        todoDao?.deleteTodo(todo)
    }

    suspend fun updateTodo(todo: Todo) {
        todoDao?.update(todo)
    }
}