package com.example.kulmkapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kulmkapp.databinding.ActivityMainBinding
import com.example.kulmkapp.logic.IngredientsListReader
import com.example.kulmkapp.logic.room.*
import com.example.kulmkapp.ui.AlarmReceiver
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: FridgeDao // 123
    var TAG = "MyMainActivity"

    override fun onPause() {
        super.onPause()
        //source: https://www.droidcon.com/2022/09/27/everything-you-need-to-know-about-adding-notifications-with-alarm-manager-in-android/
        testAlarm()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = LocalRoomDb.getInstance(applicationContext).getFridgeDao()

        //source: https://www.droidcon.com/2022/09/27/everything-you-need-to-know-about-adding-notifications-with-alarm-manager-in-android/
        //testAlarm()


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

        IngredientsListReader(
            this,
            dao
        ).readIngredientsIfNeeded() // adds 1k top ingredients to database
    }

    private fun testAlarm() {
        createNotificationChannel()
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        //c.set(year, month, day, 9, 0, 0)

        // add alarm to next day if past 9
        if(c.before(Calendar.getInstance())) {
            //c.add(Calendar.DAY_OF_MONTH, 1)
        }
        c.add(Calendar.MINUTE, 1)



        var dateString = "$day/$month/$year"

        var expiringItems = dao.getFridgeItemsByDate(dateString)
        Log.d(TAG, dateString)
        Log.d(TAG, expiringItems.size.toString())


        var alarmDesc = getString(R.string.notification_message, expiringItems.size)

        setAlarm(NotificationEntity(0, alarmDesc, Date(c.timeInMillis), 0, false, ""))
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
        val item1 = FridgeItemEntity(1, "piim uus", "milk", 1.0.toFloat(), 1, null)
        val item2 = FridgeItemEntity(-2, "leib uus", "bread", 1.0.toFloat(), 1, null)

        dao.insertFridgeOrShoppingListItem(item1)
        dao.insertFridgeOrShoppingListItem(item2)
    }

    fun testFridgeItems() {
        val f1 = FridgeItemEntity(0, "apple uus", "apple", 1f, 1, null)
        val f2 = FridgeItemEntity(0, "orange uus", "orange", 1f, 1, null)
        dao.insertFridgeOrShoppingListItem(f1)
        dao.insertFridgeOrShoppingListItem(f2)

        dao.getAllFridgeItems().forEach {
            Log.i(TAG, "${it.customName} ${it.id}")
        }
    }

    //source: https://www.droidcon.com/2022/09/27/everything-you-need-to-know-about-adding-notifications-with-alarm-manager-in-android/
    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel =
            NotificationChannel("to_do_list", "Tasks Notification Channel", importance).apply {
                description = "Notification for Tasks"
            }
        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    //source: https://www.droidcon.com/2022/09/27/everything-you-need-to-know-about-adding-notifications-with-alarm-manager-in-android/
    private fun setAlarm(notificationEntity: NotificationEntity) {
        // creating alarmManager instance
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // adding intent and pending intent to go to AlarmReceiver Class in future
        val intent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra("task_info", notificationEntity)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            notificationEntity.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        cancelAlarmIfExists(notificationEntity.id,intent)

        alarmManager.set(
            AlarmManager.RTC_WAKEUP, notificationEntity.date.time,
            pendingIntent
        )

    }

    //https://stackoverflow.com/questions/25009195/how-to-edit-reset-alarm-manager
    fun cancelAlarmIfExists(requestCode: Int, intent: Intent?) {
        try {
            val pendingIntent = PendingIntent.getBroadcast(
                this, requestCode,
                intent!!, 0
            )
            val am = this.getSystemService(ALARM_SERVICE) as AlarmManager
            am.cancel(pendingIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}