package com.simcik.branch_assessment.entities

import com.simcik.branchassessment.database.entities.ListWithTodos
import com.simcik.branchassessment.database.entities.Todo
import com.simcik.branchassessment.database.entities.TodoList
import org.junit.Assert
import org.junit.Test

class ListWithTodosTest {

    @Test
    fun testGetPercentage(){
        val todoList= TodoList(1, System.currentTimeMillis(), "test")
        val list = arrayListOf(
            Todo(1,1,"test",false, 0),
            Todo(2,1,"test",true, 0),
            Todo(3,1,"test",false, 0),
            Todo(4,1,"test",true, 0)
        )

        val listWithTodos = ListWithTodos(todoList, list)
        val expected = 50
        Assert.assertEquals(listWithTodos.getPercentageComplete(), expected)
    }

}