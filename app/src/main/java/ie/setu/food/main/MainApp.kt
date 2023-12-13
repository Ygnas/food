package ie.setu.food.main

import android.app.Application
import ie.setu.food.models.FoodStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var foods: FoodStore
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        i("Food started")
    }
}
