package com.simcik.branchassessment.todo

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.simcik.branchassessment.database.entities.Todo
import com.simcik.branchassessment.dialog.CreationDialog
import com.simcik.branchassessment.repository.Repository
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val ID = "LIST_ID"
        const val NAME = "NAME"
    }

    private val repository = Repository(application)
    private var idOfList= 0
    var nameOfList = ""
    val type = CreationDialog.Type.TODO
    val list : LiveData<List<Todo>>? by lazy{ getList(idOfList) }
    var todo = Todo(0, 0, "", false, 0)

    private fun getList(id: Int): LiveData<List<Todo>>? {
        return repository.getTodoList(id)
    }

    fun update(todo: Todo) {
        viewModelScope.launch {
            repository.updateTodo(todo)
        }
    }

    fun add(description: String) {
        viewModelScope.launch {
            if (description.isNotBlank()) {
                repository.addTodo(createTodo(description.trim()))
            }
        }
    }

    fun delete(todo: Todo) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }

    fun setIdOfList(idOfList: Int) {
        this.idOfList = idOfList
    }

    private fun createTodo(description: String): Todo {
        return Todo(0, idOfList, description, false, Color.BLACK)
    }
}