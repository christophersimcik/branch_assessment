package com.simcik.branchassessment.todo

import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.CycleInterpolator
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.simcik.branchassessment.R
import com.simcik.branchassessment.activities.SharedViewModel
import com.simcik.branchassessment.custom.ui.CustomItemDecorator
import com.simcik.branchassessment.custom.ui.DeleteItemTouchHelper
import com.simcik.branchassessment.database.entities.Todo
import com.simcik.branchassessment.databinding.TodoFragmentBinding

import com.simcik.branchassessment.dialog.CreationDialog

class TodoFragment : Fragment(), CreationDialog.DialogInterface,
    TodoAdapter.LongPressCallback {

    companion object {
        
        const val CLICK_ANIMATION_DURATION = 350L
        const val CLICK_ANIMATION_FACTOR = 1.10f
        const val CLICK_CYCLE_DURATION = 1f
        
        const val ADD_BUTTON_DURATION = 350L
        const val ADD_BUTTON_MAX = 500f
        
        const val TRANSLATION_Y = "translationY"
    }

    private val viewModel: TodoViewModel by viewModels()
    private val todoAdapter: TodoAdapter by lazy {
        TodoAdapter(context, viewModel).apply {
            hasStableIds()
            listener = this@TodoFragment
        }
    }


    private lateinit var todoListRecyclerView: RecyclerView
    private lateinit var addTodoButton: AppCompatImageView
    private lateinit var binding: TodoFragmentBinding
    private val observer = getObserver()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TodoFragmentBinding.inflate(inflater, container, false)
        getListId()
        getListName()
        
        // bind after getting arguments id and name 
        bind()
        setUpRecyclerView()
        observeData()
        swapTitle()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val v: CreationDialog? =
            parentFragmentManager.findFragmentByTag(CreationDialog.TODO_DIALOG) as CreationDialog?
        v?.setListener(this)
    }

    //swaps title in activity to reflect fragment operation
    private fun swapTitle() {
        activity?.let {
            ViewModelProvider(it).get(SharedViewModel::class.java)
                .swapTitle(viewModel.nameOfList)
        }
    }

    private fun bind() {
        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner
        if (this::binding.isInitialized) {
            addTodoButton =
                binding.todoAddButton.apply {
                    setOnClickListener {
                        onClickAnimation(it)
                        showTodoCreationDialog()
                    }
                }
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


    private fun getListId() {
        val id = arguments?.getInt(TodoViewModel.ID) ?: 0
        viewModel.setIdOfList(id)
    }

    private fun getListName() {
        val name = arguments?.getString(TodoViewModel.NAME) ?: ""
        viewModel.nameOfList = name
    }

    private fun setUpRecyclerView() {
        if (this::binding.isInitialized) {
            todoListRecyclerView = binding.todoListRecyclerView
            ItemTouchHelper(DeleteItemTouchHelper()).apply {
                attachToRecyclerView(
                    todoListRecyclerView
                )
            }
            todoListRecyclerView.adapter = todoAdapter
            todoListRecyclerView.layoutManager = LinearLayoutManager(context)
            todoListRecyclerView.addOnScrollListener(getScrollListener())
            val bottom = todoListRecyclerView.context.resources.getInteger(R.integer.todo_item_decor_bottom)
            todoListRecyclerView.addItemDecoration(CustomItemDecorator(bottom))
        }
    }

    private fun observeData() {
        viewModel.list?.observe(this.viewLifecycleOwner, observer)
    }

    override fun dismiss(dialog: Dialog?) {
        dialog?.dismiss()
    }

    private fun getScrollListener(): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                toggleButton(newState)
                super.onScrollStateChanged(recyclerView, newState)
            }
        }
    }

    private fun getObserver(): Observer<List<Todo>> {
        return Observer<List<Todo>> { data ->
            val tmp = ArrayList<Todo>()
            data.forEach {
                tmp.add(Todo(it.todoId, it.listId, it.description, it.isCompleted, it.color))
            }
            todoAdapter.submitList(tmp.sortedBy { it.isCompleted })
        }
    }

    override fun submit(dialog: Dialog?, description: String) {
        dialog?.dismiss()
        if (description.isBlank()) Toast.makeText(
            context,
            R.string.no_description_provided,
            Toast.LENGTH_SHORT
        ).show()
        viewModel.add(description)
    }

    override fun cancel(dialog: Dialog?) {
        dialog?.dismiss()
    }

    override fun showEditDialog() {
        showTodoCreationDialog()

    }

    private fun toggleButton(state: Int) {
        if (state > 0) {
            ObjectAnimator.ofFloat(
                addTodoButton,
                TRANSLATION_Y,
                addTodoButton.translationY, ADD_BUTTON_MAX,
            )
                .setDuration(ADD_BUTTON_DURATION)
                .start()
        } else {
            ObjectAnimator.ofFloat(
                addTodoButton,
                TRANSLATION_Y,
                addTodoButton.translationY, 0f,
            )
                .setDuration(ADD_BUTTON_DURATION)
                .start()
        }
    }

    private fun onClickAnimation(view: View) {
        view.animate()
            .scaleX(CLICK_ANIMATION_FACTOR)
            .scaleY(CLICK_ANIMATION_FACTOR)
            .setDuration(CLICK_ANIMATION_DURATION)
            .setInterpolator(CycleInterpolator(CLICK_CYCLE_DURATION))
            .start()
    }

}