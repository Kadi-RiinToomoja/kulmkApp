package com.example.kulmkapp.ui.fridge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kulmkapp.databinding.FragmentFridgeBinding
import com.example.kulmkapp.logic.IngredientsList
import com.example.kulmkapp.room.KulmkappDao
import com.example.kulmkapp.room.LocalRoomDb

class FridgeFragment : Fragment() {

    private var _binding: FragmentFridgeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(FridgeViewModel::class.java)

        _binding = FragmentFridgeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    fun readIngredientsList(){
        var activity = this.activity
        if(activity!=null){
            val dao = LocalRoomDb.getInstance(activity).getKulmkappDao() // KulmkappDao() //TODO: siin peaks võtma meie dao kuskilt
            val classObject = IngredientsList(activity, dao)
            classObject.readIngredientsIfNeeded()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}