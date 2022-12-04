package com.example.kulmkapp.ui.recipes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.kulmkapp.databinding.FragmentRecipeDetailsBinding
import com.example.kulmkapp.databinding.FragmentRecipesBinding
import com.example.kulmkapp.room.RecipeEntity

class RecipeDetailsFragment : Fragment() {
    private var _binding: FragmentRecipeDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var chosenRecipe: RecipeEntity? = null
    val model: RecipesViewModel by viewModels()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().popBackStack()
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setHasOptionsMenu(true)
        val id = arguments?.getInt("recipeId")
        model.refresh()
        chosenRecipe = model.recipeArray.find {
            it.id == id
        }

        // only do this if not null
        chosenRecipe?.apply {
            binding.titleText.text = this.title
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

}