package com.simcik.branchassessment.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Size
import android.view.*
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.simcik.branchassessment.R
import com.simcik.branchassessment.databinding.TodoCreationDialogBinding

@Suppress("DEPRECATION")
class CreationDialog : DialogFragment() {

    companion object {
        const val TODO_DIALOG = "TODO_DIALOG"
        const val DIALOG_TYPE = "DIALOG_TYPE"
        const val DEFAULT_WIDTH = 500
        const val DEFAULT_HEIGHT = 1000
        const val PERCENT_WIDTH_FACTOR = 0.90f
        const val PERCENT_HEIGHT_FACTOR = 0.50f
        const val MIN_BUILD_LVL = 30
    }

    interface DialogInterface {
        fun dismiss(dialog: Dialog?)
        fun submit(dialog: Dialog?, description: String)
        fun cancel(dialog: Dialog?)
    }

    private lateinit var binding: TodoCreationDialogBinding
    private lateinit var button: View
    private lateinit var descriptionTextView: AppCompatEditText
    private lateinit var headerTextView: AppCompatTextView
    private lateinit var listener: DialogInterface

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = TodoCreationDialogBinding.inflate(inflater, container, false)
        bind()
        return binding.root
    }

    fun setListener(dialogInterface: DialogInterface) {
        listener = dialogInterface
    }

    override fun setTargetFragment(fragment: Fragment?, requestCode: Int) {
        super.setTargetFragment(fragment, requestCode)
        fragment?.let { if (fragment is DialogInterface) listener = fragment }
    }

    override fun onResume() {
        super.onResume()
        val window = dialog?.window
        window?.let { it ->
            val size = getScreenSize()
            val width = (size.width * PERCENT_WIDTH_FACTOR).toInt()
            val height = (size.height * PERCENT_HEIGHT_FACTOR).toInt()
            it.setLayout(width, height)
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    private fun bind() {
        descriptionTextView = binding.descriptionField.apply{ hint = getHintText()}
        headerTextView = binding.header.apply { text = getHeaderText() }
        button = binding.submitButton.apply {
            setOnClickListener { listener.submit(dialog, descriptionTextView.text.toString()) }
        }
    }


    override fun onCancel(dialog: android.content.DialogInterface) {
        if (this::listener.isInitialized) listener.cancel(this.dialog)
    }

    // retrieves enum to determine dialog header
    private fun getCreationType(): Type? {
        return arguments?.get(DIALOG_TYPE) as Type
    }

    // determines text for dialog header based on enum
    private fun getHeaderText(): String {
        val type = getCreationType()
        return type?.let {
            when (type) {
                Type.LIST -> context?.getString(R.string.list_header) ?: ""
                Type.TODO -> context?.getString(R.string.todo_header) ?: ""
            }
        } ?: ""
    }

    // determines hint text for dialog based on enum
    private fun getHintText(): String {
        val type = getCreationType()
        return type?.let {
            when (type) {
                Type.LIST -> context?.getString(R.string.name_hint) ?: ""
                Type.TODO -> context?.getString(R.string.description_hint) ?: ""
            }
        } ?: ""
    }


    // determines dialog window sz based on device screen sz
    private fun getScreenSize(): Size {
        var size: Size? = null
        context?.let {
            val windowManager = it.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            size = if (Build.VERSION.SDK_INT >= MIN_BUILD_LVL) {
                val windowMetrics = windowManager.currentWindowMetrics
                val insets =
                    windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
                val width = windowMetrics.bounds.width() - (insets.left + insets.right)
                val height = windowMetrics.bounds.height() - (insets.top + insets.bottom)
                Size(width, height)
            } else {
                val display = windowManager.defaultDisplay as Display
                val width = display.width
                val height = display.height
                Size(width, height)
            }
        }
        return size ?: Size(DEFAULT_WIDTH, DEFAULT_HEIGHT)
    }

    enum class Type {
        LIST, TODO
    }

}