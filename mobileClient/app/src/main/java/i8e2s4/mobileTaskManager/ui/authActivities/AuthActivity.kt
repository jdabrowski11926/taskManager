package i8e2s4.mobileTaskManager.ui.authActivities

import i8e2s4.mobileTaskManager.R
import i8e2s4.mobileTaskManager.logic.logicActivities.CallbackActivity
import i8e2s4.mobileTaskManager.model.User
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_auth.*
import retrofit2.Retrofit

abstract class AuthActivity : CallbackActivity() {

    abstract fun onAcceptButtonPressed(address: String, user: User, retrofit: Retrofit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        authButtonBack.setOnClickListener {
            super.onBackPressed()
        }

        authButtonAccept.setOnClickListener {
            val username = authTextfieldUsername.text.toString()
            val password = authTextfieldPassword.text.toString()
            val address = authServerName.text.toString()
            val retrofit = createRetrofit(address)
            if(retrofit != null){
                onAcceptButtonPressed(address, User(username, password), retrofit)
            }
        }
    }
}
