package com.simcik.branchassessment.todoList

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simcik.branchassessment.R
import com.simcik.branchassessment.database.entities.ListWithTodos
import com.simcik.branchassessment.databinding.TodoListItemBinding
import com.simcik.branchassessment.todo.TodoViewModel

class TodoListAdapter(
    private val viewModel: TodoListViewModel,
    private val context: Context?
) :
    ListAdapter<ListWithTodos, TodoListAdapter.ViewHolder>(ListDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            TodoListItemBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.viewModel = viewModel
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(getItem(position))
    }

    inner class ViewHolder(private val binding: TodoListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                getTodoList()
            }
        }

        fun setData(list: ListWithTodos) {
            binding.listWithTodos = list
        }

        fun delete() {
            binding.listWithTodos?.let { it ->
                viewModel.delete(it.todoList)
            }
        }

        private fun getTodoList() {
            val bundle =
                bundleOf(
                    Pair(TodoViewModel.ID, binding.listWithTodos?.todoList?.todoListId),
                    Pair(TodoViewModel.NAME, binding.listWithTodos?.todoList?.name)
                )
            Navigation.findNavController(itemView).navigate(R.id.todoListFragment, bundle)
        }

    }

    class ListDiffCallback : DiffUtil.ItemCallback<ListWithTodos>() {
        override fun areItemsTheSame(oldItem: ListWithTodos, newItem: ListWithTodos): Boolean {
            return oldItem.todoList.todoListId == newItem.todoList.todoListId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: ListWithTodos, newItem: ListWithTodos): Boolean {
            return oldItem == newItem
        }
    }

}
