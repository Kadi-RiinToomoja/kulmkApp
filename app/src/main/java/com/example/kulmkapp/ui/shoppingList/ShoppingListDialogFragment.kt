package com.example.kulmkapp.ui.shoppingList

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


class ShoppingListDialogFragment(val shoppingListAdapter: ShoppingListAdapter): DialogFragment() {


        private val TAG = "MyShoppinglistDialogFragment"
        private lateinit var dao: FridgeDao

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {

                dao = LocalRoomDb.getInstance(requireContext()).getFridgeDao()

                val builder = AlertDialog.Builder(it)
                builder.setTitle("Add product to shopping list")

                val inflater = requireActivity().layoutInflater;
                val addItemView = inflater.inflate(R.layout.add_item_shopping_list, null)



                builder.setView(addItemView)

                    .setPositiveButton(
                        R.string.add,
                        DialogInterface.OnClickListener { dialog, id ->
                            val itemName =
                                addItemView.findViewById<TextView>(R.id.itemName).text
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
                                        -1, //tüübi id-d pole kuna tüüpi ei tea
                                        amount.toString().toFloat(),
                                        0,
                                        null
                                    )
                                )


                                shoppingListAdapter.data = dao.getAllShoppingListItems()
                                shoppingListAdapter.notifyDataSetChanged()
                            }
                        })
                    .setNegativeButton(
                        R.string.cancel,
                        DialogInterface.OnClickListener { dialog, id ->
                            getDialog()?.cancel()
                        })
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
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




}