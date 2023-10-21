package ie.setu.food.main

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ie.setu.food.models.FoodJSONStore
import ie.setu.food.models.FoodMemStore
import ie.setu.food.models.FoodStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var foods: FoodStore
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        foods = FoodJSONStore(applicationContext)
        i("Food started")
    }
}
