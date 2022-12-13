package com.example.kulmkapp.ui.shoppingList

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.FridgeDao
import com.example.kulmkapp.logic.room.FridgeItemEntity
import com.example.kulmkapp.logic.room.LocalRoomDb
import com.example.kulmkapp.ui.fridge.FridgeAdapter

class ShoppingListAdapter(var dao: FridgeDao,var activity: Activity): RecyclerView.Adapter<ShoppingListAdapter.ShoppingListItemViewHolder>() {

    val TAG = "shopping list adapter class"

    val item1 = FridgeItemEntity(44, "juust", 22, 1.0.toFloat(), 0, null)
    val item2 = FridgeItemEntity(45, "vorst", 23, 1.0.toFloat(), 0, null)
    var data = listOf(item1, item2)//dao.loadAllShoppingListItems()

    inner class ShoppingListItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListItemViewHolder {
        Log.i(TAG, "oncreateviewholder called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_shoppinglist_item, parent, false)

        return ShoppingListItemViewHolder(view)
    }


    override fun onBindViewHolder(holder: ShoppingListItemViewHolder, position: Int) {
        Log.i(TAG, "onbindviewholder called")
        val shoppingListItem = data[position]

        holder.itemView.apply {
            this.findViewById<TextView>(R.id.shoppingListItemName).text = shoppingListItem.customName

            val deleteButton: Button = this.findViewById(R.id.shoppingList_item_delete_button)
            deleteButton.setOnClickListener{
                deleteItemFromShoppingList(shoppingListItem.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun deleteItemFromShoppingList(itemId: Int) {
        // delete item
        Log.i(TAG, "Deleting item with id $itemId")
        val dao = LocalRoomDb.getInstance(activity).getFridgeDao()
        dao.deleteFridgeOrShoppingListItem(itemId)
        Log.i(TAG, dao.loadAllShoppingListItems().toString())
        // refresh
        data = dao.loadAllShoppingListItems();
        notifyDataSetChanged()
    }
}