package com.example.kulmkapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kulmkapp.databinding.FragmentDashboardBinding
import com.example.kulmkapp.logic.IngredientsList
import com.example.kulmkapp.room.KulmkappDao

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        readIngredientsList()
        return root
    }

    fun readIngredientsList(){
        var activity = this.activity
        if(activity!=null){
            val dao = KulmkappDao() //TODO: siin peaks võtma meie dao kuskilt
            val classObject = IngredientsList(activity, dao)
            classObject.readIngredientsIfNeeded()
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}