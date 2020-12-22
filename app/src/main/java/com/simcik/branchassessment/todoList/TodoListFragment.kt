package com.simcik.branchassessment.todoList

import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.animation.CycleInterpolator
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.simcik.branchassessment.R
import com.simcik.branchassessment.activities.SharedViewModel
import com.simcik.branchassessment.custom.ui.CustomItemDecorator
import com.simcik.branchassessment.custom.ui.DeleteItemTouchHelper
import com.simcik.branchassessment.database.entities.ListWithTodos
import com.simcik.branchassessment.databinding.TodoListFragmentBinding
import com.simcik.branchassessment.dialog.CreationDialog
import com.simcik.branchassessment.todo.TodoFragment

class TodoListFragment : Fragment(), CreationDialog.DialogInterface {

    companion object {
        const val DURATION = 350L
    }

    private val viewModel: TodoListViewModel by viewModels()
    private val todoListAdapter: TodoListAdapter by lazy {
        TodoListAdapter(viewModel, context)
    }

    private lateinit var todoListRecyclerView: RecyclerView
    private lateinit var binding: TodoListFragmentBinding
    private lateinit var addListButton: AppCompatImageView
    private val observer: Observer<List<ListWithTodos>> = setUpObserver()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = TodoListFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        bind()
        setUpRecyclerView()
        observeData()
        swapTitle()
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Handler(Looper.getMainLooper()).postDelayed({
        }, DURATION)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun bind() {
        if (this::binding.isInitialized) {
            addListButton = binding.todoListAddButton.apply {
                setOnClickListener {
                    showTodoCreationDialog()
                    onClickAnimation(it)
                }
            }
        }
    }

    //swaps title in activity to reflect fragment operation
    private fun swapTitle() {
        activity?.let {
            ViewModelProvider(it).get(SharedViewModel::class.java)
                .swapTitle(it.getString(R.string.title))
        }
    }

    //establishes and displays a dialog fragment to input data for new item
    private fun showTodoCreationDialog() {
        val dialog = CreationDialog().apply {
            arguments = bundleOf(Pair(CreationDialog.DIALOG_TYPE, viewModel.type))
        }
        dialog.setTargetFragment(this, 0)
        dialog.show(parentFragmentManager, CreationDialog.TODO_DIALOG)
    }

    private fun observeData() {
        viewModel.todoList?.observe(this.viewLifecycleOwner, observer)
    }

    private fun setUpRecyclerView() {
        if (this::binding.isInitialized) {
            todoListRecyclerView = binding.todoListSelectionRecyclerView
            ItemTouchHelper(DeleteItemTouchHelper()).apply {
                attachToRecyclerView(
                    todoListRecyclerView
                )
            }
            todoListRecyclerView.adapter = todoListAdapter
            todoListRecyclerView.layoutManager = LinearLayoutManager(context)
            todoListRecyclerView.addOnScrollListener(getScrollListener())
            val bottom = todoListRecyclerView.context.resources.getInteger(R.integer.list_item_decor_bottom)
            todoListRecyclerView.addItemDecoration(CustomItemDecorator(bottom))

        }
    }

    private fun getScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                toggleButton(newState)
                super.onScrollStateChanged(recyclerView, newState)
            }
        }
    }

    override fun dismiss(dialog: Dialog?) {
        dialog?.dismiss()
    }

    override fun submit(dialog: Dialog?, description: String) {
        dialog?.dismiss()
        if (description.isBlank()) Toast.makeText(
            context,
            R.string.no_name_provided,
            Toast.LENGTH_SHORT
        ).show()
        viewModel.add(description)
    }

    override fun cancel(dialog: Dialog?) {
        dialog?.dismiss()
    }

    private fun setUpObserver(): Observer<List<ListWithTodos>> {
        return Observer<List<ListWithTodos>> { list ->
            todoListAdapter.submitList(list.sortedBy { it.todoList.timestamp })
        }
    }

    private fun toggleButton(state: Int) {
        if (state > 0) {
            ObjectAnimator.ofFloat(
                addListButton,
                TodoFragment.TRANSLATION_Y,
                addListButton.translationY, 500f,
            )
                .setDuration(350L)
                .start()
        } else {
            ObjectAnimator.ofFloat(
                addListButton,
                TodoFragment.TRANSLATION_Y,
                addListButton.translationY, 0f,
            )
                .setDuration(350L)
                .start()
        }
    }

    private fun onClickAnimation(view: View) {
        view.animate()
            .scaleX(1.10f)
            .scaleY(1.10f)
            .setDuration(350)
            .setInterpolator(CycleInterpolator(1f))
            .start()
    }


}