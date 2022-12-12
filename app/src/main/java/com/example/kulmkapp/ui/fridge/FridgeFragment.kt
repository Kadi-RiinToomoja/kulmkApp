package com.example.kulmkapp.ui.fridge

import android.R
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kulmkapp.databinding.FragmentFridgeBinding
import com.example.kulmkapp.logic.IngredientsList
import com.example.kulmkapp.logic.room.FridgeDao
import com.example.kulmkapp.logic.room.LocalRoomDb

class FridgeFragment : Fragment() {

    private var _binding: FragmentFridgeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var fridgeAdapter: FridgeAdapter
    val model: FridgeViewModel by viewModels()
    private lateinit var dao: FridgeDao

    val TAG = "fridge fragment"

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

        binding.fridgeAddButton.setOnClickListener {
            onClickOpenAdd()
        }

        dao = LocalRoomDb.getInstance(requireContext()).getFridgeDao()

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        this.setHasOptionsMenu(false)

        //addToDbTest() // lisab hoopis MainActivity-s db-sse ja siin ainult võtab

        setupRecyclerView()
        Log.i(TAG, "onCreateView end")
        return root
    }

    private fun setupRecyclerView() {
        Log.i(TAG, "resView start")
        //val fridgeClickListener =
        //FridgeAdapter.FridgeItemClickListener { p -> openRecipeDetailsActivity(p) }
        //kas me tahame et midagi avaneks kui klikkida ühele fridge itemile

        val activity = this.activity
        if (activity != null) {
            Log.i(TAG, "setting up recycler view")
            var kulmkappItems = dao.loadAllFridgeItems()

            val fridgeAdapter = FridgeAdapter(kulmkappItems, activity)
            binding.fridgeRecyclerView.adapter = fridgeAdapter
            binding.fridgeRecyclerView.layoutManager = LinearLayoutManager(this.context)
        }
        Log.i(TAG, "setUpRecyclerView method ends")
    }


    fun onClickOpenAdd() {
        val newFragment: DialogFragment = FridgeDialogFragment()
        newFragment.show(this.parentFragmentManager, "fridge_dialog_fragment")

        //val builder: AlertDialog.Builder? = activity?.let {
        //            AlertDialog.Builder(it)
        //        }
        //
        //        // 2. Chain together various setter methods to set the dialog characteristics
        //        builder?.setMessage("this is a message uno")?.setTitle("this is title dos")
        //
        //        // 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
        //        val dialog: AlertDialog? = builder?.create()
        //
        //        dialog?.show()
    }

    fun readIngredientsList(){
        var activity = this.activity
        if(activity!=null){
            val dao = LocalRoomDb.getInstance(activity).getFridgeDao()
            val classObject = IngredientsList(activity, dao)
            classObject.readIngredientsIfNeeded()
        }
    }

    //override fun onResume() {
    //        super.onResume()
    //        model.refresh()
    //        fridgeAdapter.data = model.fridgeArray
    //        fridgeAdapter.notifyDataSetChanged()
    //    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}