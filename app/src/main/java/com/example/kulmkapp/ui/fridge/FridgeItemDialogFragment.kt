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
import com.example.kulmkapp.logic.room.IngredientEntity
import com.example.kulmkapp.logic.room.LocalRoomDb

class FridgeItemDialogFragment(val fridgeItem: FridgeItemEntity, val fridgeAdapter: FridgeAdapter) :
    DialogFragment() {

    private var TAG = "MyFridgeItemDialogFragment"
    private lateinit var dao: FridgeDao
    private lateinit var ingredientsList: List<IngredientEntity>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            dao = LocalRoomDb.getInstance(requireActivity()).getFridgeDao()
            ingredientsList = dao.getAllIngredients()

            val builder = AlertDialog.Builder(it)
            builder.setTitle("Details")

            val inflater = requireActivity().layoutInflater;
            val itemDetailsView: View = inflater.inflate(R.layout.add_item_fridge, null)

            val dateChangeButton = itemDetailsView.findViewById<Button>(R.id.dateChangeButton)
            dateChangeButton.setOnClickListener {
                showDatePickerDialog(itemDetailsView)
            }

            itemDetailsView.findViewById<TextView>(R.id.customName).text = fridgeItem.customName
            itemDetailsView.findViewById<TextView>(R.id.foodTypeSpinner).text = fridgeItem.itemType
            itemDetailsView.findViewById<TextView>(R.id.itemAmount).text =
                fridgeItem.amount.toString()
            itemDetailsView.findViewById<TextView>(R.id.dateText).text = fridgeItem.expireDate

            builder.setView(itemDetailsView)
                .setPositiveButton(R.string.change, null)
                .setNegativeButton(R.string.cancel) { dialog, id ->
                    getDialog()?.cancel()
                }

            // kontrolli kas kõik väljad on täidetud, kui pole siis alert et täida koik
            var newDialog = builder.create()
            dialogPositiveButtonOnClick(newDialog, itemDetailsView)
            newDialog

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun dialogPositiveButtonOnClick(
        dialog: AlertDialog,
        addItemView: View
    ) {
        dialog.setOnShowListener {
            val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val itemName =
                    addItemView.findViewById<TextView>(R.id.customName).text
                val itemType: String =
                    addItemView.findViewById<TextView>(R.id.foodTypeSpinner).text.toString()
                var amount = addItemView.findViewById<EditText>(R.id.itemAmount).text.toString()
                val dateString = addItemView.findViewById<TextView>(R.id.dateText).text

                if (amount.isEmpty()) {
                    amount = "1"
                }

                // kontrolli kas kõik väljad on täidetud, kui pole siis alert et täida koik
                if (itemName.isEmpty() || dateString.isEmpty() || itemType.isEmpty()) {
                    val toast = Toast.makeText(
                        context,
                        getString(R.string.fields_not_filled),
                        Toast.LENGTH_LONG
                    )
                    toast.show()

                } else { // update fridge item
                    dao.updateFridgeItem(
                        fridgeItem.id,
                        itemName.toString(),
                        itemType,
                        amount.toFloat(),
                        dateString.toString()
                    )

                    fridgeAdapter.data = dao.getAllFridgeItems()
                    fridgeAdapter.notifyDataSetChanged()
                    //Dismiss once everything is OK.
                    dialog.dismiss()
                }
            }
        }
    }

    private fun showDatePickerDialog(addItemView: View) {
        val newFragment = DatePickerFragment(addItemView)
        val supportFragmentManager: FragmentManager = parentFragmentManager
        newFragment.show(supportFragmentManager, "datePicker")

        Log.i(TAG, newFragment.dateString)
    }
}