package com.example.kulmkapp.ui.recipes

import android.app.Application
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.RecipeEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class RecipesAdapter(
    var data: List<RecipeEntity> = listOf(),
    var app: Application,
    private var listener: RecipeClickListener
) : RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder>() {

    fun interface RecipeClickListener {
        fun onRecipeClick(recipe: RecipeEntity)
    }


    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.single_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun getItemCount(): Int = data.size


    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = data[position]
        holder.itemView.apply {
            this.findViewById<TextView>(R.id.recipeTitleText).text = recipe.title
            this.findViewById<TextView>(R.id.recipeUsedIngredientsText).text = recipe.title
            this.findViewById<TextView>(R.id.recipeMissingIngredients).text = recipe.title


            recipe.imgUrl?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    var bmp = BitmapFactory.decodeStream(URL(it).openConnection().getInputStream())
                    withContext(Dispatchers.Main) {
                        findViewById<ImageView>(R.id.recipeImage).setImageBitmap(bmp)
                    }
                }
            }
            setOnClickListener { listener.onRecipeClick(recipe) }
        }
    }
}