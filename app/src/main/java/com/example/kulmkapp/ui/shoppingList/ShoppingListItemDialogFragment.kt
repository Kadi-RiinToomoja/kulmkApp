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
import android.view.WindowManager
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.FridgeDao
import com.example.kulmkapp.logic.room.FridgeItemEntity
import com.example.kulmkapp.logic.room.IngredientEntity
import com.example.kulmkapp.logic.room.LocalRoomDb


class ShoppingListItemDialogFragment : DialogFragment() {

    private val TAG = "MyShoppinglistItemDialogFragment"
    private lateinit var dao: FridgeDao
    private lateinit var ingredientsList: List<IngredientEntity>
    private lateinit var shoppingListItem: FridgeItemEntity

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {

            dao = LocalRoomDb.getInstance(requireContext()).getFridgeDao()

            var shoppingListItemId = arguments?.getInt("shoppingListItemId")
            shoppingListItem =
                dao.getAllShoppingListItems().filter { it.id == shoppingListItemId }[0]

            ingredientsList = dao.getAllIngredients()

            val builder = AlertDialog.Builder(it)
            builder.setTitle(getString(R.string.item_change_title))

            val inflater = requireActivity().layoutInflater;
            val itemDetailsView = inflater.inflate(R.layout.add_item_shopping_list, null)

            itemDetailsView.findViewById<TextView>(R.id.customName).text =
                shoppingListItem.customName
            itemDetailsView.findViewById<TextView>(R.id.foodTypeSpinner).text =
                shoppingListItem.itemType
            itemDetailsView.findViewById<TextView>(R.id.itemAmount).text =
                shoppingListItem.amount.toString()

            showSearchDialog(itemDetailsView)

            builder.setView(itemDetailsView)

                .setPositiveButton(
                    R.string.change,
                    null
                )
                .setNegativeButton(
                    R.string.cancel,
                    DialogInterface.OnClickListener { dialog, id ->
                        getDialog()?.cancel()
                    })

            var newDialog = builder.create()
            // kontrolli kas k??ik v??ljad on t??idetud, kui pole siis alert et t??ida koik
            dialogPositiveButtonOnClick(newDialog, itemDetailsView)
            newDialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun dialogPositiveButtonOnClick(dialog: AlertDialog, addItemView: View) {
        dialog.setOnShowListener {
            val button: Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val itemName =
                    addItemView.findViewById<TextView>(R.id.customName).text
                val itemType: String =
                    addItemView.findViewById<TextView>(R.id.foodTypeSpinner).text.toString()
                var amount = addItemView.findViewById<EditText>(R.id.itemAmount).text.toString()

                // kontrolli kas k??ik v??ljad on t??idetud, kui pole siis alert et t??ida koik
                if (amount.isEmpty()) {
                    amount = "1"
                }

                if (itemName.isEmpty() || itemType.isEmpty()) {
                    val toast =
                        Toast.makeText(
                            context,
                            getString(R.string.fields_not_filled),
                            Toast.LENGTH_LONG
                        )
                    toast.show()

                } else { // lisa asjad shopping listi
                    dao.updateFridgeItem(
                        shoppingListItem.id,
                        itemName.toString(),
                        itemType,
                        amount.toFloat(),
                        null.toString()
                    )
                    setFragmentResult("requestKeyShopping", bundleOf())
                    dialog.dismiss()
                }
            }
        }
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
                )
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