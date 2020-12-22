package com.simcik.branchassessment.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.simcik.branchassessment.database.entities.TodoList
import com.simcik.branchassessment.database.entities.ListWithTodos

@Dao
interface TodoListDao{

    @Transaction
    @Query("SELECT * FROM TodoList")
    fun getListOfTodos(): LiveData<List<ListWithTodos>>

    @Update
    suspend fun update(todoList: TodoList)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun addList(todoList: TodoList)

    @Delete
    suspend fun deleteList(todoList: TodoList)
}