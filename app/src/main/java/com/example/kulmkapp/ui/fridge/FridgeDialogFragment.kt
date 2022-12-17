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
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.IngredientsList
import com.example.kulmkapp.logic.room.FridgeDao
import com.example.kulmkapp.logic.room.FridgeItemEntity
import com.example.kulmkapp.logic.room.IngredientEntity
import com.example.kulmkapp.logic.room.LocalRoomDb


class FridgeDialogFragment(val fridgeAdapter: FridgeAdapter) : DialogFragment() {

    private val TAG = "MyFridgeDialogFragment"
    private lateinit var dao: FridgeDao
    private lateinit var ingredientsList: List<IngredientEntity>


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            dao = LocalRoomDb.getInstance(requireContext()).getFridgeDao()
            ingredientsList = dao.getAllIngredients()

            val builder = AlertDialog.Builder(it)
            builder.setTitle("Add product to fridge")

            val inflater = requireActivity().layoutInflater;
            val addItemView = inflater.inflate(R.layout.add_item_fridge, null)

            val dateChangeButton = addItemView.findViewById<Button>(R.id.dateChangeButton)
            dateChangeButton.setOnClickListener {
                showDatePickerDialog(addItemView)
            }
            // Kadi test
            showSearchDialog(addItemView)

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
                        } else { // lisa asjad fridgesse
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

    //https://www.geeksforgeeks.org/how-to-implement-custom-searchable-spinner-in-android/
    fun showSearchDialog(addItemView: View) {
        // assign variable
        var textview: TextView = addItemView.findViewById(R.id.testView)

        // initialize array list
        var arrayList: ArrayList<String> = ArrayList(ingredientsList.map { it.name })




        // set value in array list
        //arrayList.add("DSA Self Paced")
        //arrayList.add("Complete Interview Prep")

        textview.let {
            textview.setOnClickListener(View.OnClickListener {
                // Initialize dialog
                var dialog = this.context?.let { it1 -> Dialog(it1) }

                // set custom dialog
                dialog!!.setContentView(R.layout.dialog_searchable_spinner)

                // set custom height and width
                dialog.window!!.setLayout(650, 800) // see oile hea

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

                    override fun afterTextChanged(s: Editable) {}
                })
                listView.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, id -> // when item selected from list
                        // set selected item on textView
                        if (adapter != null) {
                            textview!!.setText(adapter.getItem(position)!!)
                        }
                        // Dismiss dialog
                        dialog!!.dismiss()
                    }
            })
        }
    }

}