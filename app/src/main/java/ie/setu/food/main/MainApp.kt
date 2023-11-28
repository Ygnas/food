package ie.setu.food.main

import android.app.Application
import ie.setu.food.models.FoodJSONStore
import ie.setu.food.models.FoodStore
import ie.setu.food.models.UserJSONStore
import ie.setu.food.models.UserStore
import timber.log.Timber
import timber.log.Timber.i

class MainApp : Application() {

    lateinit var foods: FoodStore
    lateinit var users: UserStore
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        foods = FoodJSONStore(applicationContext)
        users = UserJSONStore(applicationContext)
        i("Food started")
    }
}
