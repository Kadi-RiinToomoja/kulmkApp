package com.example.kulmkapp.ui.fridge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kulmkapp.R
import com.example.kulmkapp.databinding.FragmentFridgeBinding
import com.example.kulmkapp.logic.IngredientsListReader
import com.example.kulmkapp.logic.room.FridgeDao
import com.example.kulmkapp.logic.room.FridgeItemEntity
import com.example.kulmkapp.logic.room.LocalRoomDb

class FridgeFragment : Fragment() {

    private var _binding: FragmentFridgeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var fridgeAdapter: FridgeAdapter
    val model: FridgeViewModel by viewModels()
    private lateinit var dao: FridgeDao

    val TAG = "my fridge fragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView start")
        val notificationsViewModel =
            ViewModelProvider(this)[FridgeViewModel::class.java]

        _binding = FragmentFridgeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dao = LocalRoomDb.getInstance(requireContext()).getFridgeDao()

        val textView: TextView = binding.textFridge
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        this.setHasOptionsMenu(false)

        //addToDbTest() // lisab hoopis MainActivity-s db-sse ja siin ainult võtab

        setupRecyclerViewAndButton()

        Log.i(TAG, "onCreateView end")
        return root
    }

    private fun setupRecyclerViewAndButton() {
        Log.i(TAG, "resView start")
        //val fridgeClickListener =
        //FridgeAdapter.FridgeItemClickListener { p -> openRecipeDetailsActivity(p) }
        //kas me tahame et midagi avaneks kui klikkida ühele fridge itemile

        val activity = this.activity
        if (activity != null) {
            Log.i(TAG, "setting up recycler view")
            var kulmkappItems = dao.getAllFridgeItems()

            //miks adapteril val ees on
            val fridgeAdapter = FridgeAdapter(kulmkappItems, activity)
            binding.fridgeRecyclerView.adapter = fridgeAdapter
            binding.fridgeRecyclerView.layoutManager = LinearLayoutManager(this.context)

            binding.fridgeAddButton.setOnClickListener {
                onClickOpenAdd(fridgeAdapter)
            }
            binding.fridgeSearchRecipe.setOnClickListener {
                openRecipes(fridgeAdapter.itemsChecked)
            }

            val dividerItemDecoration = DividerItemDecoration(
                binding.fridgeRecyclerView.context,
                (binding.fridgeRecyclerView.layoutManager as LinearLayoutManager).getOrientation()
            )
            binding.fridgeRecyclerView.addItemDecoration(dividerItemDecoration)

        }
        Log.i(TAG, "setUpRecyclerView method ends")
    }


    fun onClickOpenAdd(fridgeAdapter: FridgeAdapter) {
        val newFragment: DialogFragment = AddToFridgeDialogFragment(fridgeAdapter)
        newFragment.show(this.parentFragmentManager, "fridge_dialog_fragment")
    }

    fun openRecipes(fridgeItems: MutableList<FridgeItemEntity>) {
        val bundle = Bundle()
        bundle.putIntegerArrayList("fridgeIDs", ArrayList(fridgeItems.map { it.id }))
        findNavController().navigate(R.id.action_open_recipes, bundle)
    }

    fun readIngredientsList() {
        var activity = this.activity
        if (activity != null) {
            val dao = LocalRoomDb.getInstance(activity).getFridgeDao()
            val classObject = IngredientsListReader(activity, dao)
            classObject.readIngredientsIfNeeded()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}