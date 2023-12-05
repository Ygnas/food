package ie.setu.food.firebase

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import ie.setu.food.models.FoodModel
import ie.setu.food.models.FoodStore
import timber.log.Timber.i

object FirebaseDB: FoodStore {

    var database = FirebaseDatabase.getInstance().reference
    override fun findAll(): List<FoodModel> {
        TODO("Not yet implemented")
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
                        val donation = FoodModel.fromMap(it)
                        localList.add(donation)
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
                        val donation = it.getValue(FoodModel::class.java)
                        localList.add(donation!!)
                    }
                    database.child("user-foods").child(userid)
                        .removeEventListener(this)

                    foodList.value = localList
                }
            })
    }


    override fun findById(id: Long): FoodModel? {
        TODO("Not yet implemented")
    }

    fun findById(uid: String,id: Long) {
        TODO("Not yet implemented")
    }

    override fun create(food: FoodModel) {
        TODO("Not yet implemented")
    }

    override fun create(firebaseUser: MutableLiveData<FirebaseUser>, food: FoodModel) {
        val uid = "12345"
        val key = database.child("foods").push().key ?: return
        food.uid = key

        val childAdd = HashMap<String, Any>()
        childAdd["/foods/$key"] = food.toMap()
        childAdd["/user-foods/$uid/$key"] = food.toMap()

        database.updateChildren(childAdd)
    }

    override fun update(food: FoodModel) {
        TODO("Not yet implemented")
    }

    override fun delete(food: FoodModel) {
        TODO("Not yet implemented")
    }

    fun updateImageRef(userid: String,imageUri: String) {

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