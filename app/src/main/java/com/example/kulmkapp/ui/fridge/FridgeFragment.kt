package com.example.kulmkapp.ui.fridge

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kulmkapp.databinding.FragmentFridgeBinding
import com.example.kulmkapp.logic.IngredientsList
import com.example.kulmkapp.room.KulmkappItemEntity
import com.example.kulmkapp.room.LocalRoomDb

class FridgeFragment : Fragment() {

    private var _binding: FragmentFridgeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val TAG = "fridge fragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this)[FridgeViewModel::class.java]

        _binding = FragmentFridgeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fridgeAddButton.setOnClickListener {
            onClickOpenAdd()
        }

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        this.setHasOptionsMenu(false)
        setupRecyclerView()
        return root
    }

    private fun setupRecyclerView() {
        //val fridgeClickListener =
        //FridgeAdapter.FridgeItemClickListener { p -> openRecipeDetailsActivity(p) }
        //kas me tahame et midagi avaneks kui klikkida ühele fridge itemile

        var activity = this.activity
        if (activity != null) {
            Log.i(TAG, "setting up recycler view")
            val dao = LocalRoomDb.getInstance(activity).getKulmkappDao()
            var kulmkappItems = dao.loadAllKulmkappItems()

            val item1 = KulmkappItemEntity(1, "piim", 0, 1.0.toFloat(), 1, null)
            val item2 = KulmkappItemEntity(2, "leib", 0, 1.0.toFloat(), 1, null)
            kulmkappItems = listOf(item1, item2)

            val fridgeAdapter = FridgeAdapter(kulmkappItems)

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
            val dao = LocalRoomDb.getInstance(activity).getKulmkappDao()
            val classObject = IngredientsList(activity, dao)
            classObject.readIngredientsIfNeeded()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}