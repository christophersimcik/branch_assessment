package com.simcik.branchassessment.todo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simcik.branchassessment.database.entities.Todo
import com.simcik.branchassessment.databinding.TodoItemNewBinding


class TodoAdapter(private val context: Context?, private val viewModel: TodoViewModel) :
    ListAdapter<Todo, TodoAdapter.ViewHolder>(TodoDiffCallback()) {

    private lateinit var recyclerView: RecyclerView
    lateinit var listener: LongPressCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TodoItemNewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(getItem(position))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    inner class ViewHolder(private val binding: TodoItemNewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var text = binding.todoDescriptionText

        fun setData(todo: Todo) {
            binding.todo = todo
        }

        init {
            text.setOnClickListener { toggleCompleted() }
        }

        fun delete() {
            binding.todo?.let { it ->
                viewModel.delete(it)
            }
        }

        private fun toggleCompleted() {
            binding.todo?.let { it ->
                viewModel.update(
                    Todo(
                        it.todoId,
                        it.listId,
                        it.description,
                        !it.isCompleted,
                        it.color
                    )
                )
            }
        }

    }

    class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
        override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem.todoId == newItem.todoId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
            return oldItem == newItem
        }

    }

    interface LongPressCallback {
        fun showEditDialog()
    }

}