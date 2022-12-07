package com.example.kulmkapp.ui.recipes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kulmkapp.R
import com.example.kulmkapp.databinding.FragmentRecipesBinding
import com.example.kulmkapp.room.RecipeEntity

class RecipesFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var recipesAdapter: RecipesAdapter
    val model: RecipesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(RecipesViewModel::class.java)

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        this.setHasOptionsMenu(false)
        setupRecyclerView()
        return root
    }

    override fun onResume() {
        super.onResume()
        model.refresh()
        recipesAdapter.data = model.recipeArray
        recipesAdapter.notifyDataSetChanged()
    }

    private fun setupRecyclerView() {
        val recipeClickListener = RecipesAdapter.RecipeClickListener { recipe -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(recipe.recipeUrl))) }
        recipesAdapter = RecipesAdapter(model.recipeArray, recipeClickListener)
        binding.recyclerviewRecipelist.adapter = recipesAdapter
        binding.recyclerviewRecipelist.layoutManager = GridLayoutManager(context, 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipeDetails(recipe: RecipeEntity) {
        val bundle = Bundle()
        bundle.putInt("recipeId", recipe.id)
        findNavController().navigate(R.id.action_open_recipe_details, bundle)
    }
}