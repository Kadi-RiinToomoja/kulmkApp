package com.example.kulmkapp.ui.fridge

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.FridgeDao
import com.example.kulmkapp.logic.room.FridgeItemEntity
import com.example.kulmkapp.logic.room.IngredientEntity
import com.example.kulmkapp.logic.room.LocalRoomDb


class AddToFridgeDialogFragment(val fridgeAdapter: FridgeAdapter) : DialogFragment() {

    private val TAG = "MyAddToFridgeDialogFragment"
    private lateinit var dao: FridgeDao
    private lateinit var ingredientsList: List<IngredientEntity>


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            dao = LocalRoomDb.getInstance(requireContext()).getFridgeDao()
            ingredientsList = dao.getAllIngredients()

            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.add_product))

            val inflater = requireActivity().layoutInflater;
            val addItemView = inflater.inflate(R.layout.add_item_fridge, null)

            val dateChangeButton = addItemView.findViewById<Button>(R.id.dateChangeButton)
            dateChangeButton.setOnClickListener {
                showDatePickerDialog(addItemView)
            }
            // Kadi test
            showSearchDialog(addItemView)

            builder.setView(addItemView)
                .setPositiveButton(R.string.add, null)
                .setNegativeButton(R.string.cancel) { dialog, id ->
                    getDialog()?.cancel()
                }

            // kontrolli kas kõik väljad on täidetud, kui pole siis alert et täida koik
            var newDialog = builder.create()
            dialogPositiveButtonOnClick(newDialog, addItemView)
            newDialog

        } ?: throw IllegalStateException("Activity cannot be null")

    }

    private fun dialogPositiveButtonOnClick(
        dialog: AlertDialog,
        addItemView: View
    ) {
        dialog.setOnShowListener {
            val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
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

                    } else { // lisa asjad fridgesse
                        dao.insertFridgeOrShoppingListItem(
                            FridgeItemEntity(
                                0,
                                itemName.toString(),
                                itemType,
                                amount.toFloat(),
                                1,
                                dateString.toString()
                            )
                        )

                        fridgeAdapter.refreshData()
                        //Dismiss once everything is OK.
                        dialog.dismiss()
                    }
                }
            })
        }
    }

    fun showDatePickerDialog(addItemView: View) {
        val newFragment = DatePickerFragment(addItemView)
        val supportFragmentManager: FragmentManager = parentFragmentManager
        newFragment.show(supportFragmentManager, "datePicker")

        Log.i(TAG, newFragment.dateString)
    }

    //https://www.geeksforgeeks.org/how-to-implement-custom-searchable-spinner-in-android/
    fun showSearchDialog(addItemView: View) {
        // assign variable
        var textview: TextView = addItemView.findViewById(R.id.foodTypeSpinner)

        // initialize array list
        var arrayList: ArrayList<String> = ArrayList(ingredientsList.map { it.name })

        textview.let {
            textview.setOnClickListener(View.OnClickListener {
                Log.i(TAG, "set on click listener called")
                // Initialize dialog
                var dialog = this.context?.let { it1 -> Dialog(it1) }

                // set custom dialog
                dialog!!.setContentView(R.layout.dialog_searchable_spinner)

                // set custom height and width
                dialog.window!!.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT
                ) // see oile hea

                // set transparent background
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                // show dialog
                dialog.show()

                // Initialize and assign variable
                val editText = dialog.findViewById<EditText>(R.id.edit_text)
                val listView = dialog.findViewById<ListView>(R.id.list_view)

                // Initialize array adapter
                val adapter = this.context?.let { it1 ->
                    ArrayAdapter(
                        it1, android.R.layout.simple_list_item_1,
                        arrayList
                    )
                }

                // set adapter
                listView.adapter = adapter
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        if (adapter != null) {
                            adapter.filter.filter(s)
                        }
                    }

                    override fun afterTextChanged(s: Editable) {
                        Log.i(TAG, "after text changed called")
                    }
                })
                listView.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id -> // when item selected from list
                        // set selected item on textView
                        if (adapter != null) {
                            val selectedFoodType = adapter.getItem(position)
                            textview!!.setText(selectedFoodType)
                            Log.i(TAG, "selected item $selectedFoodType")
                            addItemView.findViewById<TextView>(R.id.customName).text =
                                selectedFoodType

                        }
                        // Dismiss dialog
                        dialog!!.dismiss()
                    }
            })
        }
    }

}