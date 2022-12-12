package com.example.kulmkapp.ui.fridge

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.kulmkapp.R
import kotlinx.coroutines.NonCancellable.start

class FridgeDialogFragment : DialogFragment() {
    fun showDatePickerDialog() {
        val newFragment = DatePickerFragment()
        val supportFragmentManager: FragmentManager = parentFragmentManager
        newFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Add product to fridge")
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.add_item, null))
                // Add action buttons
                .setPositiveButton(R.string.select_date,
                    DialogInterface.OnClickListener { dialog, id ->
                        // lisa asjad fridgesse
                        showDatePickerDialog()

                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}