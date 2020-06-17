package i8e2s4.mobileTaskManager.ui.authActivities

import i8e2s4.mobileTaskManager.model.User
import i8e2s4.mobileTaskManager.server.JsonAPI
import android.widget.Toast
import retrofit2.Retrofit

class RegisterActivity : AuthActivity(){

    override fun onAcceptButtonPressed(address: String, user: User, retrofit: Retrofit){
        val call = retrofit.create(JsonAPI::class.java).register(user)
        runCallback(call){ _, _->
            Toast.makeText(this@RegisterActivity,"New user created", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}