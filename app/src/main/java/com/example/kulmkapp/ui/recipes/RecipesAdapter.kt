package com.example.kulmkapp.ui.recipes

import android.app.Application
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kulmkapp.R
import com.example.kulmkapp.logic.room.IngredientEntity
import com.example.kulmkapp.logic.room.RecipeEntity
import com.example.kulmkapp.logic.room.RecipeIngredientEntity
import com.koushikdutta.ion.Ion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class RecipesAdapter(
    var dataRecipes: List<RecipeEntity> = listOf(),
    var dataIDs: List<RecipeIngredientEntity> = listOf(),
    var dataIngredients: List<IngredientEntity> = listOf(),

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

    override fun getItemCount(): Int = dataRecipes.size


    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = dataRecipes[position]
        Log.d("RecipesAdapter", recipe.title + recipe.imgUrl + recipe.recipeUrl)

        holder.itemView.apply {
            this.findViewById<TextView>(R.id.recipeTitleText).text = recipe.title

            var usedIngredients = mutableListOf<IngredientEntity>()
            Log.d("RecipesAdapter", dataIDs.size.toString())

            for (ids in dataIDs.filter { it.recipeId == recipe.id && it.usesIngredient }) {
                Log.d("RecipesAdapter", ids.id.toString() + " " + ids.recipeId.toString())

                var ingredient = dataIngredients.find { it.id == ids.id }
                if (ingredient != null) {
                    usedIngredients.add(ingredient)
                }
            }
            var missedIngredients = mutableListOf<IngredientEntity>()
            for (ids in dataIDs.filter { it.recipeId == recipe.id && !it.usesIngredient }) {
                Log.d("RecipesAdapter", ids.id.toString() + " " + ids.recipeId.toString())
                var ingredient = dataIngredients.find { it.id == ids.id }
                if (ingredient != null) {
                    missedIngredients.add(ingredient)
                }
            }
            Log.d("RecipesAdapter", usedIngredients.toString())
            Log.d("RecipesAdapter", missedIngredients.toString())
            this.findViewById<TextView>(R.id.recipeUsedIngredientsText).text = usedIngredients.joinToString(", ") { it.name }
            this.findViewById<TextView>(R.id.recipeMissingIngredients).text = missedIngredients.joinToString(", ") { it.name }



            recipe.imgUrl?.let {
                Ion.with(this.findViewById<ImageView>(R.id.recipeImage))
                    .load(it)
            }
            setOnClickListener { listener.onRecipeClick(recipe) }
        }
    }
}