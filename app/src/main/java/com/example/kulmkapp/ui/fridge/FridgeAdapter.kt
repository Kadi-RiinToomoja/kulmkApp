package com.example.kulmkapp.ui.fridge

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.FridgeItemEntity
import com.example.kulmkapp.logic.room.LocalRoomDb
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FridgeAdapter(
    var data: List<FridgeItemEntity>, var activity: Activity
) : RecyclerView.Adapter<FridgeAdapter.FridgeItemViewHolder>() {

    val TAG = "fridge adapter class"
    var itemsChecked = mutableListOf<FridgeItemEntity>();
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener (listener: OnItemClickListener){
        Log.i(TAG, "setOnItemClickListener")
        mListener = listener
    }

    inner class FridgeItemViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener{
                Log.i(TAG, "Adapter pos: $adapterPosition")
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FridgeItemViewHolder {
        Log.i(TAG, "oncreateviewholder called")
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_fridge_item, parent, false)

        Log.i(TAG, mListener.toString())
        return FridgeItemViewHolder(view, mListener)
    }


    override fun onBindViewHolder(holder: FridgeItemViewHolder, position: Int) {
        Log.i(TAG, "onbindviewholder called")
        val fridgeItem = data[position]

        holder.itemView.apply {
            this.findViewById<TextView>(R.id.fridgeItemName).text = fridgeItem.customName
            this.findViewById<TextView>(R.id.fridgeItemDate).text = fridgeItem.expireDate
            this.findViewById<TextView>(R.id.fridgeItemAmount).text = fridgeItem.amount.toString()

            this.findViewById<CheckBox>(R.id.fridgeItemCheckBox).apply {
                this.setOnClickListener {
                    val checked = this.isChecked
                    if (checked) itemsChecked.add(fridgeItem)
                    else itemsChecked.remove(fridgeItem)
                    activity.findViewById<FloatingActionButton>(R.id.fridgeSearchRecipe)?.isEnabled =
                        itemsChecked.isNotEmpty()
                    activity.findViewById<TextView>(R.id.text_fridge)?.visibility =
                        if (itemsChecked.isEmpty()) View.VISIBLE else View.GONE
                }
            }

            val deleteButton: Button = this.findViewById(R.id.fridge_item_delete_button)
            deleteButton.setOnClickListener {
                // delete from list if selected
                //itemsChecked.remove(fridgeItem)
                deleteItemFromFridge(fridgeItem.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun deleteItemFromFridge(itemId: Int) {
        // delete item
        Log.i(TAG, "Deleting item with id $itemId")
        val dao = LocalRoomDb.getInstance(activity).getFridgeDao()

        dao.deleteFridgeOrShoppingListItem(itemId)
        Log.i(TAG, dao.getAllFridgeItems().toString())

        // refresh
        data = dao.getAllFridgeItems();
        notifyDataSetChanged()
    }


}