package com.example.kulmkapp.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulmkapp.R
import com.example.kulmkapp.room.RecipeEntity

class RecipesAdapter(var data: List<RecipeEntity> =  listOf(),
                     private var listener: RecipeClickListener
) : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {
    companion object { const val IMAGE_HEIGHT = 300 }

    fun interface RecipeClickListener {
        fun onRecipeClick(recipe: RecipeEntity)
    }


    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_recipe_2, parent, false)
        return RecipeViewHolder(view)
    }

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = data[position]
        holder.itemView.apply {
            this.findViewById<TextView>(R.id.recipeTitleTextView).text = recipe.title
            /*

    if (recipe.filePath != null) {
        holder.itemView.findViewById<ImageView>(R.id.recipeImage).setImageBitmap(ImageUtils.loadImage(app, recipe.filePath!!.toUri(), IMAGE_HEIGHT))
    } else {
        holder.itemView.findViewById<ImageView>(R.id.recipeImage).layoutParams.height = IMAGE_HEIGHT
        holder.itemView.findViewById<ImageView>(R.id.recipeImage).setImageResource(R.drawable.food_placeholder)
    }
*/


            setOnClickListener { listener.onRecipeClick(recipe) }
        }
    }
}