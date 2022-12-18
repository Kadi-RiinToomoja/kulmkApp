package com.example.kulmkapp.ui.shoppingList

import android.app.Activity
import android.app.AlertDialog
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.FridgeItemEntity
import com.example.kulmkapp.logic.room.LocalRoomDb
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShoppingListAdapter(var data: List<FridgeItemEntity>, var activity: Activity) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingListItemViewHolder>() {

    val TAG = "shopping list adapter class"
    var itemsChecked = mutableListOf<FridgeItemEntity>();
    var setOfCheckBoxes = mutableListOf<CheckBox>()
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    inner class ShoppingListItemViewHolder(
        itemView: View,
        listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListItemViewHolder {
        //val item1 = FridgeItemEntity(44, "igavene juust", "cheese", 1.0.toFloat(), 0, null)
        //val item2 = FridgeItemEntity(45, "igavene vorst", "sausage", 1.0.toFloat(), 0, null)
        //dao.insertFridgeOrShoppingListItem(item1)
        //dao.insertFridgeOrShoppingListItem(item2)

        Log.i(TAG, "oncreateviewholder called")
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_shoppinglist_item, parent, false)

        return ShoppingListItemViewHolder(view, mListener)
    }


    override fun onBindViewHolder(holder: ShoppingListItemViewHolder, position: Int) {
        Log.i(TAG, "onbindviewholder called")
        val shoppingListItem = data[position]

        holder.itemView.apply {
            this.findViewById<TextView>(R.id.shoppingListItemName).text =
                shoppingListItem.customName
            this.findViewById<TextView>(R.id.shoppingListItemAmount).text =
                shoppingListItem.amount.toString()

            val checkBox = this.findViewById<CheckBox>(R.id.shoppingListItemCheckBox)
            setOfCheckBoxes.add(checkBox)

            checkBox.apply {
                this.setOnClickListener {
                    Log.i(TAG, "clicked checkbox of item $it")
                    val checked = this.isChecked
                    if (checked) {
                        itemsChecked.add(shoppingListItem)
                    }
                    else {
                        itemsChecked.remove(shoppingListItem)
                    }


                    Log.i(TAG, "items checked are: $itemsChecked")
                    if (itemsChecked.isEmpty()){
                        activity.findViewById<FloatingActionButton>(R.id.shoppingListMoveToFridgeButton)?.background
                            ?.mutate()
                            ?.setTint(ContextCompat.getColor(context, R.color.disabled))
                    }

                    else {
                        activity.findViewById<FloatingActionButton>(R.id.shoppingListMoveToFridgeButton)?.background
                            ?.mutate()
                            ?.setTint(ContextCompat.getColor(context, R.color.yellow_orange))
                    }
                }
            }

            val deleteButton: Button = this.findViewById(R.id.shoppingList_item_delete_button)
            deleteButton.setOnClickListener {
                Log.i("deleting from shopping list", "${shoppingListItem.customName}")
                askIfWantsToDeleteItemFromShoppingList(shoppingListItem.id)
                //deleteItemFromShoppingList(shoppingListItem.id)
            }
        }
    }

    fun askIfWantsToDeleteItemFromShoppingList(id: Int) {
        val alertDialog = AlertDialog.Builder(this.activity).create()
        alertDialog.setTitle(R.string.question_title)
        alertDialog.setMessage(this.activity.getText(R.string.delete_from_shopping_list))

        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, this.activity.getText(R.string.answer_yes)
        ) { dialog, which ->
            deleteItemFromShoppingList(id)
        }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, this.activity.getText(R.string.answer_no)
        ) { dialog, which ->
        }

        alertDialog.show()
    }

    override fun getItemCount(): Int {
        return data.size
    }


    private fun deleteItemFromShoppingList(itemId: Int) {
        // delete item
        Log.i(TAG, "Deleting item with id $itemId")
        val dao = LocalRoomDb.getInstance(activity).getFridgeDao()
        val item = data.filter { it.id == itemId }[0]
        val pos = data.indexOf(item)
        itemsChecked.remove(item)
        setOfCheckBoxes.removeAt(pos)
        dao.deleteFridgeOrShoppingListItem(itemId)
        Log.i(TAG, dao.getAllShoppingListItems().toString())

        // refresh
        data = dao.getAllShoppingListItems();
        notifyDataSetChanged()
    }
}