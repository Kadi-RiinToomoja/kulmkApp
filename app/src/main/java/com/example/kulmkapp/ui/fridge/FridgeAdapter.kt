package com.example.kulmkapp.ui.fridge

import android.app.Activity
import android.icu.text.SimpleDateFormat
import android.graphics.Color
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
import java.util.*
import kotlin.Comparator

class FridgeAdapter(
    var dao: FridgeDao, var activity: Activity
) : RecyclerView.Adapter<FridgeAdapter.FridgeItemViewHolder>() {

    var data = sortItemsByDateAscending(dao.getAllFridgeItems())
    val TAG = "fridge adapter class"
    var itemsChecked = mutableListOf<FridgeItemEntity>();
    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    inner class FridgeItemViewHolder(
        itemView: View,
        listener: OnItemClickListener
    ) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FridgeItemViewHolder {
        Log.i(TAG, "oncreateviewholder called")
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_fridge_item, parent, false)

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

                    if (itemsChecked.isEmpty()) {
                        activity.findViewById<FloatingActionButton>(R.id.fridgeSearchRecipe)
                            ?.getBackground()
                            ?.mutate()
                            ?.setTint(ContextCompat.getColor(context, R.color.disabled))
                    } else {
                        activity.findViewById<FloatingActionButton>(R.id.fridgeSearchRecipe)
                            ?.getBackground()
                            ?.mutate()
                            ?.setTint(ContextCompat.getColor(context, R.color.brown_orange))
                    }

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
        data = dao.getAllFridgeItems()
    }

    fun refreshData() {
        data = sortItemsByDateAscending(dao.getAllFridgeItems())
        notifyDataSetChanged()
    }

    fun sortItemsByDateAscending(data: List<FridgeItemEntity>): List<FridgeItemEntity> {
        Log.i(TAG, "sort items by date")
        val newData = data.sortedWith(Comparator { i1, i2 ->
            val sdf = SimpleDateFormat("dd/MM/yyyy")

            var expD1 = i1.expireDate
            var expD2 = i2.expireDate
            if (expD1 == null) {
                expD1 = "01/01/0000"
            }
            if (expD2 == null) {
                expD2 = "01/01/0000"
            }

            val firstDate: Date = sdf.parse(expD1)
            val secondDate: Date = sdf.parse(expD2)

            var cmp = 0
            if (firstDate.before(secondDate))
                cmp = -1
            if (firstDate.after(secondDate))
                cmp = 1


            cmp
        })
        return newData
    }
}
