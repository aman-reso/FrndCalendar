package com.frnd.frndcalendar.presentation.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.frnd.frndcalendar.databinding.CreateTaskBtmSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTaskBtmSheet : BottomSheetDialogFragment() {
    private var binding: CreateTaskBtmSheetBinding? = null
    private var createTaskBtmSheetCallback: CreateTaskBtmSheetCallback? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CreateTaskBtmSheetCallback) {
            createTaskBtmSheetCallback = context
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CreateTaskBtmSheetBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        binding?.btnCreateTask?.setOnClickListener {
            val title = binding?.edtTitle?.text?.toString()
            val description = binding?.edtDescription?.text?.toString()
            if (!title.isNullOrEmpty() && !description.isNullOrEmpty()) {
                createTaskBtmSheetCallback?.createTask(title, description)
                dialog?.dismiss()
            } else {
                Toast.makeText(requireContext(), "Please check title or Description", Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        const val TAG = "btm_sheet"
        fun newInstance(): CreateTaskBtmSheet {
            val args = Bundle()

            val fragment = CreateTaskBtmSheet()
            fragment.arguments = args
            return fragment
        }
    }

    interface CreateTaskBtmSheetCallback {
        fun createTask(title: String, description: String)
    }
}