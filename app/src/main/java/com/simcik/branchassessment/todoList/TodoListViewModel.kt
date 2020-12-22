package com.simcik.branchassessment.todoList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.simcik.branchassessment.custom.helpers.TimeHelper
import com.simcik.branchassessment.database.entities.TodoList
import com.simcik.branchassessment.dialog.CreationDialog
import com.simcik.branchassessment.repository.Repository
import kotlinx.coroutines.launch

class TodoListViewModel(application: Application) : AndroidViewModel(application) {

    val type = CreationDialog.Type.LIST

    private val repository = Repository(application)

    val todoList = repository.getLists()

    fun getTime(time: Long): String {
        return TimeHelper.getTimeAsString(time)
    }

    fun add(description: String) {
        viewModelScope.launch {
            if (description.isNotBlank()) {
                repository.addList(createList(description.trim()))
            }
        }
    }

    fun delete(list: TodoList) {
        viewModelScope.launch {
            repository.deleteList(list)
        }
    }

    private fun createList(name: String): TodoList {
        return TodoList(0, TimeHelper.getTime(), name)
    }
}

