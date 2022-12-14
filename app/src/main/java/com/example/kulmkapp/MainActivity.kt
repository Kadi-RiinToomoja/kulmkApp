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
import com.example.kulmkapp.logic.IngredientsList
import com.example.kulmkapp.logic.room.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: FridgeDao // 123
    var TAG = "MyMainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = LocalRoomDb.getInstance(applicationContext).getFridgeDao()

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

        //addToDbTest() // lisab db-sse kaks asja mida näeb hetkel ainult fridges
        //testFridgeItems()

        IngredientsList(this, dao).readIngredientsIfNeeded() // adds 1k top ingredients to database

        //SpoonacularAPI.getRecipes(applicationContext, "apples,+flour,+sugar", dao)
    }

    private fun testDB() {
        val moos = IngredientEntity(10, "Moos")
        dao.insertIngredient(moos)

        val items = dao.getAllIngredients()
        for (item in items) {
            Log.i("$TAG TestDB", item.name)
        }
    }

    fun addToDbTest() {
        val item1 = FridgeItemEntity(1, "piim uus", 0, 1.0.toFloat(), 1, null)
        val item2 = FridgeItemEntity(-2, "leib uus", 0, 1.0.toFloat(), 1, null)

        dao.insertFridgeOrShoppingListItem(item1)
        dao.insertFridgeOrShoppingListItem(item2)
    }

    fun testFridgeItems(){
        val f1 = FridgeItemEntity(0,"apple uus", 0, 1f, 1, null)
        val f2 = FridgeItemEntity(0,"orange uus", 1, 1f, 1, null)
        dao.insertFridgeOrShoppingListItem(f1)
        dao.insertFridgeOrShoppingListItem(f2)

        dao.getAllFridgeItems().forEach{
            Log.i(TAG, "${it.customName} ${it.id}")
        }
    }
}