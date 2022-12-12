package com.example.kulmkapp.ui.fridge

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.FridgeItemEntity
import com.example.kulmkapp.logic.room.LocalRoomDb
import com.example.kulmkapp.ui.fridge.FridgeFragment

class FridgeAdapter(
    var data: List<FridgeItemEntity>,
    var activity: Activity
) : RecyclerView.Adapter<FridgeAdapter.FridgeItemViewHolder>() {

    val TAG = "fridge adapter class"
    fun interface FridgeItemClickListener {
        fun onFridgeItemClick(recipe: FridgeItemEntity)
    }

    inner class FridgeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FridgeItemViewHolder {
        Log.i(TAG, "oncreateviewholder called")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_fridge_item, parent, false)

        return FridgeItemViewHolder(view)
    }


    override fun onBindViewHolder(holder: FridgeItemViewHolder, position: Int) {
        Log.i(TAG, "onbindviewholder called")
        val fridgeItem = data[position]

        holder.itemView.apply {
            this.findViewById<TextView>(R.id.fridgeItemName).text = fridgeItem.customName
            //var pic = this.findViewById<ImageView>(R.id.recipeThumbnail)
            //model.chooseImage(recipe, pic, 200)//kordaja määrab pildi suurust

            //setOnClickListener { listener.onRecipeClick(recipe) }
            val deleteButton: Button = this.findViewById(R.id.fridge_item_delete_button)
            deleteButton.setOnClickListener{
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
        dao.deleteFridgeItem(itemId)
        Log.i(TAG, dao.loadAllFridgeItems().toString())
        // refresh
    }

}