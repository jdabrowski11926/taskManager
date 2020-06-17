package I8E2S4.mobileTaskManager.ui.AuthActivities

import I8E2S4.mobileTaskManager.model.User
import I8E2S4.mobileTaskManager.server.JsonAPI
import I8E2S4.mobileTaskManager.ui.CalendarActivity
import android.content.Intent
import retrofit2.Retrofit

class LoginActivity : AuthActivity(){

    override fun onAcceptButtonPressed(address: String, user: User, retrofit: Retrofit){
        val call = retrofit.create(JsonAPI::class.java).login(user)
        runCallback(call) { _, response->
            val intent = Intent(
                applicationContext,
                CalendarActivity::class.java
            )
            intent.putExtra("username", user.username)
            intent.putExtra("jwt_token", response.headers().get("Authorization"))
            intent.putExtra("address", address)
            startActivity(intent)
        }
    }
}