package com.example.kulmkapp.ui.fridge

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.FridgeDao
import com.example.kulmkapp.logic.room.FridgeItemEntity
import com.example.kulmkapp.logic.room.LocalRoomDb


class FridgeDialogFragment(val fridgeAdapter: FridgeAdapter) : DialogFragment() {

    private val TAG = "MyFridgeDialogFragment"
    private lateinit var dao: FridgeDao

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            dao = LocalRoomDb.getInstance(requireContext()).getFridgeDao()

            val builder = AlertDialog.Builder(it)
            builder.setTitle("Add product to fridge")

            val inflater = requireActivity().layoutInflater;
            val addItemView = inflater.inflate(R.layout.add_item_fridge, null)

            val dateChangeButton = addItemView.findViewById<Button>(R.id.dateChangeButton)
            dateChangeButton.setOnClickListener {
                showDatePickerDialog(addItemView)
            }

            builder.setView(addItemView)

                .setPositiveButton(R.string.add,
                    DialogInterface.OnClickListener { dialog, id ->
                        val itemName =
                            addItemView.findViewById<TextView>(R.id.itemName).text
                        val itemTypeId: Int = 0//addItemView.findViewById<12345>(...)
                        val amount = addItemView.findViewById<EditText>(R.id.itemAmount).text
                        val dateString = addItemView.findViewById<TextView>(R.id.dateText).text
                        //val dateString = dao.getValueByKey("addToFridgeDate").value

                        // kontrolli kas kõik väljad on täidetud, kui pole siis alert et täida koik
                        if (itemName.isEmpty() || amount.isEmpty() || dateString.isEmpty()) {
                            showAlertDialog()
                        }
                        else { // lisa asjad fridgesse
                            dao.insertFridgeOrShoppingListItem(
                                FridgeItemEntity(
                                    0,
                                    itemName.toString(),
                                    itemTypeId,
                                    amount.toString().toFloat(),
                                    1,
                                    dateString.toString()
                                )
                            )

                            fridgeAdapter.data = dao.getAllFridgeItems()
                            fridgeAdapter.notifyDataSetChanged()
                        }
                    })
                .setNegativeButton(R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun showDatePickerDialog(addItemView: View) {
        val newFragment = DatePickerFragment(addItemView)
        val supportFragmentManager: FragmentManager = parentFragmentManager
        newFragment.show(supportFragmentManager, "datePicker")

        Log.i(TAG, newFragment.dateString)
    }

    fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setTitle("Error")
        alertDialog.setMessage("Not all fields were filled.")
        //alertDialog.setIcon(R.drawable.welcome)

        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "OK"
        ) { dialog, which ->
            // tee midagi kui vajutab erroril ok
        }

        alertDialog.show()
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

}