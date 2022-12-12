package com.example.kulmkapp

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kulmkapp.databinding.ActivityMainBinding
import com.example.kulmkapp.room.IngredientEntity
import com.example.kulmkapp.room.KulmkappDao
import com.example.kulmkapp.room.KulmkappItemEntity
import com.example.kulmkapp.room.LocalRoomDb
import com.example.kulmkapp.ui.recipes.SpoonacularAPI

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: KulmkappDao // 123
    var TAG = "MyMainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = LocalRoomDb.getInstance(applicationContext).getKulmkappDao()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_shopping_list, R.id.navigation_fridge, R.id.navigation_recipes
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        addToDbTest() // lisab db-sse kaks asja mida näeb hetkel ainult fridges

        //SpoonacularAPI.getRecipes(applicationContext, "apples,+flour,+sugar", db)
    }
    

    private fun testDB() {
        val moos = IngredientEntity(10, "Moos")
        dao.insertIngredient(moos)

        val items = dao.loadAllIngredients()
        for (item in items) {
            Log.i("$TAG TestDB", item.name)
        }
    }

    fun addToDbTest() {
        val item1 = KulmkappItemEntity(1, "piim", 0, 1.0.toFloat(), 1, null)
        val item2 = KulmkappItemEntity(2, "leib", 0, 1.0.toFloat(), 1, null)

        dao.insertKulmkappItem(item1)
        dao.insertKulmkappItem(item2)
    }
}