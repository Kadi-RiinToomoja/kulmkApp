package com.example.kulmkapp.ui.shoppingList

import android.app.Activity
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
import com.example.kulmkapp.logic.room.FridgeDao
import com.example.kulmkapp.logic.room.FridgeItemEntity
import com.example.kulmkapp.logic.room.LocalRoomDb
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShoppingListAdapter(var dao: FridgeDao,var activity: Activity): RecyclerView.Adapter<ShoppingListAdapter.ShoppingListItemViewHolder>() {

    val TAG = "shopping list adapter class"
    var data = dao.getAllShoppingListItems()
    var itemsChecked = mutableListOf<FridgeItemEntity>();
    var setOfCheckBoxes = mutableSetOf<CheckBox>() //vb saaks ka ilma aga ma ei oska. ilma oleks muidugi parem kui saaks

    inner class ShoppingListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListItemViewHolder {
        //val item1 = FridgeItemEntity(44, "igavene juust", "cheese", 1.0.toFloat(), 0, null)
        //val item2 = FridgeItemEntity(45, "igavene vorst", "sausage", 1.0.toFloat(), 0, null)
        //dao.insertFridgeOrShoppingListItem(item1)
        //dao.insertFridgeOrShoppingListItem(item2)
        data = dao.getAllShoppingListItems()


        Log.i(TAG, "oncreateviewholder called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_shoppinglist_item, parent, false)

        return ShoppingListItemViewHolder(view)
    }


    override fun onBindViewHolder(holder: ShoppingListItemViewHolder, position: Int) {
        Log.i(TAG, "onbindviewholder called")
        val shoppingListItem = data[position]

        holder.itemView.apply {
            this.findViewById<TextView>(R.id.shoppingListItemName).text = shoppingListItem.customName
            this.findViewById<TextView>(R.id.shoppingListItemAmount).text = shoppingListItem.amount.toString()

            val checkBox = this.findViewById<CheckBox>(R.id.shoppingListItemCheckBox)
            setOfCheckBoxes.add(checkBox)

            checkBox.apply {
                this.setOnClickListener {
                    Log.i(TAG, "clicked checkbox of item $it")
                    val checked = this.isChecked
                    if (checked) itemsChecked.add(shoppingListItem)
                    else itemsChecked.remove(shoppingListItem)

                    Log.i(TAG, "items checked are: $itemsChecked")
                    if (itemsChecked.isEmpty()){
                        activity.findViewById<FloatingActionButton>(R.id.shoppingListMoveToFridgeButton)?.background
                            ?.mutate()
                            ?.setTint(ContextCompat.getColor(context, R.color.disabled))
                    }

                    else {
                        activity.findViewById<FloatingActionButton>(R.id.shoppingListMoveToFridgeButton)?.background
                            ?.mutate()
                            ?.setTint(ContextCompat.getColor(context, R.color.purple_200))
                    }
                }
            }

            val deleteButton: Button = this.findViewById(R.id.shoppingList_item_delete_button)
            deleteButton.setOnClickListener{
                Log.i("deleting from shopping list", "${shoppingListItem.customName}")
                itemsChecked.remove(shoppingListItem)
                deleteItemFromShoppingList(shoppingListItem.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    private fun deleteItemFromShoppingList(itemId: Int) {
        // delete item

        Log.i(TAG, "shopping list has this number of elements before deleting: ${data.size}")
        Log.i(TAG, "shopping list elements: "+dao.getAllShoppingListItems().toString())
        Log.i(TAG, "Deleting item with id $itemId")
        val dao = LocalRoomDb.getInstance(activity).getFridgeDao()
        dao.deleteFridgeOrShoppingListItem(itemId)
        Log.i(TAG, dao.getAllShoppingListItems().toString())
        // refresh
        data = dao.getAllShoppingListItems();
        Log.i(TAG, "shopping list has this number of elements after deleting: ${data.size}")
        notifyDataSetChanged()
    }
}