package com.example.kulmkapp.ui.fridge

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulmkapp.R
import com.example.kulmkapp.room.KulmkappItemEntity

class FridgeAdapter(
    var data: List<KulmkappItemEntity>
) : RecyclerView.Adapter<FridgeAdapter.FridgeItemViewHolder>() {


    val TAG = "fridge adapter class"
    fun interface FridgeItemClickListener {
        fun onFridgeItemClick(recipe: KulmkappItemEntity)
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
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}