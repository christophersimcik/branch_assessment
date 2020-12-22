package com.simcik.branchassessment.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*

import com.simcik.branchassessment.database.entities.Todo

@Dao
interface TodoDao{

    @Query("SELECT * FROM Todo WHERE listId IN (:listId) ")
    fun getListOfTodos(listId: Int): LiveData<List<Todo>>

    @Update
    suspend fun update(todo: Todo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodo(todo: Todo)

    @Delete
    suspend fun deleteTodo(todo: Todo)
}