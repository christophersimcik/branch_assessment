package com.simcik.branchassessment.database

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.simcik.branchassessment.database.dao.TodoDao
import com.simcik.branchassessment.database.dao.TodoListDao
import com.simcik.branchassessment.database.entities.Todo
import com.simcik.branchassessment.database.entities.TodoList

@Database(

    entities = [Todo::class, TodoList::class], version = 1

)

abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
    abstract fun todoListDao(): TodoListDao

    companion object {
        private const val DATABASE_NAME = "TODO_DB"
        private var instance: TodoDatabase? = null
        fun getInstance(context: Context): TodoDatabase? {
            if (instance == null) {
                synchronized(TodoDatabase::class) {
                    instance = androidx.room.Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        DATABASE_NAME
                    )
                        .build()
                }
            }
            return instance
        }
    }
}