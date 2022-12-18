package com.example.kulmkapp.ui.shoppingList

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
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.FridgeDao
import com.example.kulmkapp.logic.room.FridgeItemEntity
import com.example.kulmkapp.logic.room.IngredientEntity
import com.example.kulmkapp.logic.room.LocalRoomDb


class ShoppingListDialogFragment(val shoppingListAdapter: ShoppingListAdapter) : DialogFragment() {


    private val TAG = "MyShoppinglistDialogFragment"
    private lateinit var dao: FridgeDao
    private lateinit var ingredientsList: List<IngredientEntity>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            dao = LocalRoomDb.getInstance(requireContext()).getFridgeDao()
            ingredientsList = dao.getAllIngredients()

            val builder = AlertDialog.Builder(it)
            builder.setTitle("Add product to shopping list")

            val inflater = requireActivity().layoutInflater;
            val addItemView = inflater.inflate(R.layout.add_item_shopping_list, null)

                showSearchDialog(addItemView)

            builder.setView(addItemView)

                .setPositiveButton(
                    R.string.add,
                    null
                )/*
                        DialogInterface.OnClickListener { dialog, id ->
                            val itemName =
                                addItemView.findViewById<TextView>(R.id.customName).text
                            val itemTypeId: Int = 0//addItemView.findViewById<12345>(...)
                            val amount = addItemView.findViewById<EditText>(R.id.itemAmount).text

                            // kontrolli kas kõik väljad on täidetud, kui pole siis alert et täida koik
                            if (itemName.isEmpty() || amount.isEmpty()) {
                                showAlertDialog()
                            }
                            else { // lisa asjad fridgesse
                                dao.insertFridgeOrShoppingListItem(
                                    FridgeItemEntity(
                                        0,
                                        itemName.toString(),
                                        "", //tüüpi pole kuna tüüpi ei tea
                                        amount.toString().toFloat(),
                                        0,
                                        null
                                    )
                                )
                                shoppingListAdapter.data = dao.getAllShoppingListItems()
                                shoppingListAdapter.notifyDataSetChanged()
                            }
                        })*/
                .setNegativeButton(
                    R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })
            var newDialog = builder.create()
            // kontrolli kas kõik väljad on täidetud, kui pole siis alert et täida koik
            dialogPositiveButtonOnClick(newDialog, addItemView)
            newDialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun dialogPositiveButtonOnClick(dialog: AlertDialog, addItemView: View) {
        dialog.setOnShowListener(DialogInterface.OnShowListener {
            val button: Button = (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener(object : View.OnClickListener {
                override fun onClick(view: View?) {
                    val itemName =
                        addItemView.findViewById<TextView>(R.id.customName).text
                    val itemType: String =
                        addItemView.findViewById<TextView>(R.id.foodTypeSpinner).text.toString()
                    val amount = addItemView.findViewById<EditText>(R.id.itemAmount).text

                    // kontrolli kas kõik väljad on täidetud, kui pole siis alert et täida koik
                    if (itemName.isEmpty() || amount.isEmpty() || itemType.isEmpty()) {
                        val toast =
                            Toast.makeText(context, getString(R.string.fields_not_filled), Toast.LENGTH_LONG)
                        toast.show()
                        //showAlertDialog()
                    } else { // lisa asjad fridgesse
                        dao.insertFridgeOrShoppingListItem(
                            FridgeItemEntity(
                                0,
                                itemName.toString(),
                                itemType,
                                amount.toString().toFloat(),
                                0,
                                null
                            )
                        )

                        shoppingListAdapter.data = dao.getAllFridgeItems()
                        shoppingListAdapter.notifyDataSetChanged()
                        //Dismiss once everything is OK.
                        dialog.dismiss()
                    }
                }
            })
        })
    }


    fun showAlertDialog() {
        val alertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setTitle("Error")
        alertDialog.setMessage(getString(R.string.fields_not_filled))
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
                //dialog.window!!.setLayout(650, 800) // see oile hea
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
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
                        dialog.dismiss()
                    }
            })
        }
    }
}