package I8E2S4.mobileTaskManager.ui.AuthActivities

import I8E2S4.mobileTaskManager.R
import I8E2S4.mobileTaskManager.logic.CallbackActivity
import I8E2S4.mobileTaskManager.model.User
import I8E2S4.mobileTaskManager.server.JsonAPI
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
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

    override fun onDisableAll() {
        authLayoutId.isEnabled = false
        authProgressBar.visibility = ProgressBar.VISIBLE
        //authButtonBack.isEnabled = false
        //authButtonAccept.isEnabled = false
        //authTextfieldPassword.isEnabled = false
        //authTextfieldUsername.isEnabled = false
    }

    override fun onEnableAll() {
        authLayoutId.isEnabled = true
        authProgressBar.visibility = ProgressBar.INVISIBLE
        //authButtonBack.isEnabled = true
        //authButtonAccept.isEnabled = true
        //authTextfieldPassword.isEnabled = true
        //authTextfieldUsername.isEnabled = true
    }
}
