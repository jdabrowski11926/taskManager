package I8E2S4.mobileTaskManager.ui.AuthActivities

import I8E2S4.mobileTaskManager.model.User
import I8E2S4.mobileTaskManager.server.JsonAPI
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