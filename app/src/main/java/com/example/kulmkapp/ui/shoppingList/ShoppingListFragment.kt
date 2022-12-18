package com.example.kulmkapp.ui.shoppingList

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kulmkapp.R
import com.example.kulmkapp.databinding.FragmentShoppingListBinding
import com.example.kulmkapp.logic.room.FridgeDao
import com.example.kulmkapp.logic.room.FridgeItemEntity
import com.example.kulmkapp.logic.room.LocalRoomDb
import com.example.kulmkapp.ui.fridge.FridgeAdapter
import com.example.kulmkapp.ui.fridge.FridgeItemDialogFragment


class ShoppingListFragment : Fragment() {

    private var _binding: FragmentShoppingListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var shoppingListAdapter : ShoppingListAdapter
    private lateinit var dao: FridgeDao

    val TAG = "shopping list fragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dao = LocalRoomDb.getInstance(requireContext()).getFridgeDao()

        this.setHasOptionsMenu(false)

        setupRecyclerView()

        return root
    }

    private fun setupRecyclerView() {
        Log.i(TAG, "recyclerView start")

        val activity = this.activity
        if (activity != null) {
            Log.i(TAG, "setting up recycler view")
            val shoppingListItems = dao.getAllShoppingListItems()

            shoppingListAdapter = ShoppingListAdapter(shoppingListItems,  activity)
            binding.shoppingListRecyclerView.adapter = shoppingListAdapter
            binding.shoppingListRecyclerView.layoutManager = LinearLayoutManager(this.context)

            binding.shoppingListAddButton.setOnClickListener {
                onClickOpenAdd(shoppingListAdapter!!)
            }
            binding.shoppingListMoveToFridgeButton.setOnClickListener {
                if (shoppingListAdapter!!.itemsChecked.isNotEmpty()) askIfWantsToMoveCheckedItemsToFridge()
                else Toast.makeText(this.context,getString(R.string.choose_items_to_add_to_fridge),
                    Toast.LENGTH_SHORT).show()
            }

            shoppingListAdapter!!.setOnItemClickListener(object : ShoppingListAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    Log.i(TAG, "fridgeAdapter.setOnItemClickListener")
                    Log.i(TAG, position.toString())
                    onShoppingListItemClick(shoppingListAdapter.data[position])
                }
            })

            val dividerItemDecoration = DividerItemDecoration(
                binding.shoppingListRecyclerView.context,
                (binding.shoppingListRecyclerView.layoutManager as LinearLayoutManager).getOrientation()
            )
            binding.shoppingListRecyclerView.addItemDecoration(dividerItemDecoration)

        }
        Log.i(TAG, "setUpRecyclerView method ends")
    }

    fun askIfWantsToMoveCheckedItemsToFridge() {
        val alertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setTitle(R.string.question_title)
        alertDialog.setMessage(getString(R.string.are_you_sure_move_to_fridge))

        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, getString(R.string.answer_yes)
        ) { dialog, which ->
            moveSelectedItemsToFridge()
        }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, getString(R.string.answer_no)
        ) { dialog, which ->
        }

        alertDialog.show()
    }

    private fun moveSelectedItemsToFridge() {

        shoppingListAdapter.let{
            val itemsToMove = it.itemsChecked
            Log.i(TAG, "moving items from shopping list to fridge: ${itemsToMove}")
            itemsToMove.forEach {
                dao.moveFromShoppingListToFridge(it.id)
            }
            it.data = dao.getAllShoppingListItems()
            it.notifyDataSetChanged()
            removeTicksFromAllCheckboxes(it)
            it.itemsChecked = mutableListOf()
        }
    }

    fun removeTicksFromAllCheckboxes(adapter: ShoppingListAdapter){
        Log.i(TAG, "removing ticks from all checkboxes. number of checkboxes total: ${adapter.setOfCheckBoxes.size}")
        adapter.setOfCheckBoxes.forEach {
            it.isChecked = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onShoppingListItemClick(shoppingListItem: FridgeItemEntity) {
        Log.i(TAG, "onFrigeItemClick ${shoppingListItem.customName}, $shoppingListItem")
        val newFragment: DialogFragment = ShoppingListItemDialogFragment(shoppingListItem, shoppingListAdapter)
        newFragment.show(this.parentFragmentManager, "fridge_item_info_dialog_fragment")
    }

    fun onClickOpenAdd(shoppingListAdapter: ShoppingListAdapter) {
        val newFragment: DialogFragment = AddToShoppingListDialogFragment(shoppingListAdapter)
        newFragment.show(this.parentFragmentManager, "shopping_list_dialog_fragment")
        Log.i(TAG, "on click open add is called")
    }
}