package com.example.kulmkapp.ui.shoppingList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kulmkapp.databinding.FragmentShoppingListBinding
import com.example.kulmkapp.logic.room.FridgeDao
import com.example.kulmkapp.logic.room.LocalRoomDb


class ShoppingListFragment : Fragment() {

    private var _binding: FragmentShoppingListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var shoppingListAdapter = activity?.let { ShoppingListAdapter(dao, it) }
    val homeViewModel: ShoppingListViewModel by viewModels()
    private lateinit var dao: FridgeDao

    val TAG = "shopping list fragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //val homeViewModel =
          //  ViewModelProvider(this).get(ShoppingListViewModel::class.java)

        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        dao = LocalRoomDb.getInstance(requireContext()).getFridgeDao()

        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        this.setHasOptionsMenu(false)

        setupRecyclerView()

        return root
    }

    private fun setupRecyclerView() {
        Log.i(TAG, "recyclerView start")

        val activity = this.activity
        if (activity != null) {
            Log.i(TAG, "setting up recycler view")
            var shoppingListItems = dao.getAllShoppingListItems()

            shoppingListAdapter = ShoppingListAdapter(dao,  activity)
            binding.shoppingListRecyclerView.adapter = shoppingListAdapter
            binding.shoppingListRecyclerView.layoutManager = LinearLayoutManager(this.context)

            binding.shoppingListAddButton.setOnClickListener {
                onClickOpenAdd(shoppingListAdapter!!)//siin vist ei tohiks neid 2 hüüumärki olla
            }
            binding.shoppingListMoveToFridgeButton.setOnClickListener {
                Log.i(TAG, "moving items from shopping list to fridge: ${shoppingListAdapter!!.itemsChecked.toString()}")
            }

            val dividerItemDecoration = DividerItemDecoration(
                binding.shoppingListRecyclerView.context,
                (binding.shoppingListRecyclerView.layoutManager as LinearLayoutManager).getOrientation()
            )
            binding.shoppingListRecyclerView.addItemDecoration(dividerItemDecoration)

        }
        Log.i(TAG, "setUpRecyclerView method ends")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun onClickOpenAdd(shoppingListAdapter: ShoppingListAdapter) {
        val newFragment: DialogFragment = ShoppingListDialogFragment(shoppingListAdapter)
        newFragment.show(this.parentFragmentManager, "shopping_list_dialog_fragment")
        Log.i(TAG, "on click open add is called")
    }
}