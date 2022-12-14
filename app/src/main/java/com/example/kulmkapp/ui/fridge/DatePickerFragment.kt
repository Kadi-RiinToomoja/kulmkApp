package com.example.kulmkapp.ui.fridge

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.KeyValueEntity
import com.example.kulmkapp.logic.room.LocalRoomDb

class DatePickerFragment(val addItemView: View) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var dateString = "none"

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        dateString = "$day/$month/$year"
        Log.i("MyDatePickerFragment", dateString)

        addItemView.findViewById<TextView>(R.id.dateText).text = dateString

        val dao = LocalRoomDb.getInstance(requireActivity()).getFridgeDao()
        dao.addKeyValue(KeyValueEntity("addToFridgeDate", dateString))
    }
}