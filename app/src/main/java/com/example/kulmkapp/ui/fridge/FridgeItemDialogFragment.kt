package com.example.kulmkapp.ui.fridge

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.LocalRoomDb

class FridgeItemDialogFragment : DialogFragment() {

    private var TAG = "MyFridgeItemDialogFragment"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Details")

            val dao = LocalRoomDb.getInstance(requireActivity()).getFridgeDao()
            val inflater = requireActivity().layoutInflater;

            val addItemDetailsView: View = inflater.inflate(R.layout.add_item_fridge, null)
            val itemId = savedInstanceState?.getInt("itemId")

            itemId?.let {
                val item = dao.getSingleFridgeItemById(itemId)
                val itemName = item.customName
                val itemType = item.itemType
                val itemDate = item.expireDate
                addItemDetailsView.findViewById<TextView>(R.id.customName).text = itemName
                addItemDetailsView.findViewById<TextView>(R.id.dateText).text = itemDate.toString()
                // itemType see mis listis on selle spineeri asjaga
            }




            builder.setView(addItemDetailsView)
                // Add action buttons
                .setPositiveButton(R.string.select_date,
                    DialogInterface.OnClickListener { dialog, id ->
                        // lisa asjad fridgesse
                        //showDatePickerDialog()
                        showSearchDialog(this.requireContext())
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun showSearchDialog(context: Context) {
        val items = listOf("SSS", "rrra", "dsfoksdfn")
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, items)

        val searchSpinner = Spinner(context)
        searchSpinner.adapter = adapter

        val builder = androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle("Search")
            .setView(searchSpinner)
            .setPositiveButton("Search") { dialog, _ ->
                val selectedItem = searchSpinner.selectedItem as String
                // Perform search with the selected item
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }

        builder.show()
    }

    fun showDatePickerDialog(addItemView: View) {
        val newFragment = DatePickerFragment(addItemView)
        val supportFragmentManager: FragmentManager = parentFragmentManager
        newFragment.show(supportFragmentManager, "datePicker")

        Log.i(TAG, newFragment.dateString)
    }
}