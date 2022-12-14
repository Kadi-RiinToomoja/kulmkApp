package com.example.kulmkapp.ui.fridge

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
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
    private lateinit var dao: FridgeDao

    val TAG = "my fridge fragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.i(TAG, "onCreateView start")

        _binding = FragmentFridgeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dao = LocalRoomDb.getInstance(requireContext()).getFridgeDao()

        this.setHasOptionsMenu(false)

        setupRecyclerViewAndButton()

        setFragmentResultListener("requestKeyFridge") { requestKey, bundle ->
            fridgeAdapter.refreshData()
        }

        Log.i(TAG, "onCreateView end")
        return root
    }

    private fun setupRecyclerViewAndButton() {
        Log.i(TAG, "resView start")

        val activity = this.activity
        if (activity != null) {
            Log.i(TAG, "setting up recycler view")

            fridgeAdapter = FridgeAdapter(dao, activity)
            binding.fridgeRecyclerView.adapter = fridgeAdapter
            binding.fridgeRecyclerView.layoutManager = LinearLayoutManager(this.context)

            binding.fridgeAddButton.setOnClickListener {
                onClickOpenAdd()
            }
            binding.fridgeSearchRecipe.setOnClickListener {
                if (fridgeAdapter.itemsChecked.isNotEmpty()) openRecipes(fridgeAdapter.itemsChecked)
                else Toast.makeText(
                    this.context,
                    getString(R.string.choose_items_for_recipe),
                    Toast.LENGTH_SHORT
                ).show()
            }

            fridgeAdapter.setOnItemClickListener(object : FridgeAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    Log.i(TAG, "fridgeAdapter.setOnItemClickListener")
                    Log.i(TAG, position.toString())
                    onFridgeItemClick(fridgeAdapter.data[position])
                }
            })

            binding.fridgeItemCheckBox.apply {
                this.setOnClickListener { checkbox ->
                    for (i in fridgeAdapter.data.indices) {
                        fridgeAdapter.setOfCheckBoxes[i].isChecked = this.isChecked
                        val checked = this.isChecked
                        val fridgeItem = fridgeAdapter.data[i]
                        if (checked) fridgeAdapter.itemsChecked.add(fridgeItem)
                        else fridgeAdapter.itemsChecked.remove(fridgeItem)
                    }
                }

            }

            val dividerItemDecoration = DividerItemDecoration(
                binding.fridgeRecyclerView.context,
                (binding.fridgeRecyclerView.layoutManager as LinearLayoutManager).getOrientation()
            )

            binding.fridgeRecyclerView.addItemDecoration(dividerItemDecoration)


        }

        Log.i(TAG, "setUpRecyclerView method ends")
    }

    fun onFridgeItemClick(fridgeItem: FridgeItemEntity) {
        Log.i(TAG, "onFrigeItemClick ${fridgeItem.customName}, $fridgeAdapter")
        val newFragment: DialogFragment = FridgeItemDialogFragment()
        val args = Bundle()
        args.putInt("fridgeItemId", fridgeItem.id)
        newFragment.arguments = args
        newFragment.show(this.parentFragmentManager, "fridge_item_info_dialog_fragment")
    }


    fun onClickOpenAdd() {
        val newFragment: DialogFragment = AddToFridgeDialogFragment()
        newFragment.show(this.parentFragmentManager, "add_to_fridge_dialog_fragment")
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


