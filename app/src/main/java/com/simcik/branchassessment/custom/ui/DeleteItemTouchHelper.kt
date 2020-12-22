package com.simcik.branchassessment.custom.ui

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.simcik.branchassessment.todo.TodoAdapter
import com.simcik.branchassessment.todoList.TodoListAdapter

class DeleteItemTouchHelper :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END ) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
       return  false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when(viewHolder){
            is TodoAdapter.ViewHolder -> viewHolder.delete()
            is TodoListAdapter.ViewHolder -> viewHolder.delete()
        }
    }

}