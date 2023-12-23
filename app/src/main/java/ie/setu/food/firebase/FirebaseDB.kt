package ie.setu.food.firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ie.setu.food.models.FoodModel
import ie.setu.food.models.FoodStore
import timber.log.Timber.i

object FirebaseDB : FoodStore {

    var database = FirebaseDatabase.getInstance().reference

    private val keyUsed = MutableLiveData<String>()
    var observableKey: LiveData<String>
        get() = keyUsed
        set(value) {
            keyUsed.value = value.value
        }

    override fun findAll(foodList: MutableLiveData<List<FoodModel>>) {
        database.child("foods")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    i("Firebase Food error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<FoodModel>()
                    val children = snapshot.children
                    children.forEach {
                        val food = FoodModel.fromMap(it)
                        localList.add(food)
                    }
                    database.child("foods")
                        .removeEventListener(this)

                    foodList.value = localList
                }
            })
    }

    fun findAll(userid: String, foodList: MutableLiveData<List<FoodModel>>) {

        database.child("user-foods").child(userid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    i("Firebase Food error : ${error.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val localList = ArrayList<FoodModel>()
                    val children = snapshot.children
                    children.forEach {
                        val food = FoodModel.fromMap(it)
                        localList.add(food)
                    }
                    database.child("user-foods").child(userid)
                        .removeEventListener(this)

                    foodList.value = localList
                }
            })
    }


    override fun findById(id: String, foods: List<FoodModel>): FoodModel? {
        foods.let {
            for (food in it) {
                if (food.uid == id) {
                    return food
                }
            }
        }
        return null

    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, food: FoodModel) {
        val uid = firebaseUser.value?.uid
        val key = if (food.uid.isNullOrEmpty()) {
            database.child("foods").push().key ?: return
        } else {
            food.uid
        }
        food.uid = key
        keyUsed.value = key.toString()
        val childAdd = HashMap<String, Any>()
        childAdd["/foods/$key"] = food.toMap()
        childAdd["/user-foods/$uid/$key"] = food.toMap()

        database.updateChildren(childAdd)
    }

    fun setFavoriteStatus(firebaseUser: MutableLiveData<FirebaseUser>, food: FoodModel) {
        val uid = firebaseUser.value?.uid ?: return
        val key = food.uid ?: return

        food.fav = !food.fav

        val childUpdate = HashMap<String, Any>()
        childUpdate["/foods/$key"] = food.toMap()
        childUpdate["/user-foods/$uid/$key"] = food.toMap()

        database.updateChildren(childUpdate)
    }

    override fun delete(userid: String, id: String) {
        val childDelete : MutableMap<String, Any?> = HashMap()
        childDelete["/foods/$id"] = null
        childDelete["/user-foods/$userid/$id"] = null

        database.updateChildren(childDelete)
    }

    fun updateImageRef(userid: String, imageUri: String) {

        val userDonations = database.child("user-donations").child(userid)
        val allDonations = database.child("donations")

        userDonations.addListenerForSingleValueEvent(
            object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach {
                        it.ref.child("profilepic").setValue(imageUri)
                        val donation = it.getValue(FoodModel::class.java)
                        allDonations.child(donation!!.uid!!)
                            .child("profilepic").setValue(imageUri)
                    }
                }
            })
    }
}
