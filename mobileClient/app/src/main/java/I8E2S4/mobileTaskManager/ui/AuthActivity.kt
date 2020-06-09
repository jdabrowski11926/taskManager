package I8E2S4.mobileTaskManager.ui

import I8E2S4.mobileTaskManager.R
import I8E2S4.mobileTaskManager.logic.CallbackActivity
import I8E2S4.mobileTaskManager.model.User
import I8E2S4.mobileTaskManager.server.JsonAPI
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import retrofit2.Retrofit

abstract class AuthActivity : CallbackActivity() {

    protected val buttonBack by lazy {findViewById<Button>(R.id.authButtonBack)}
    protected val buttonAccept by lazy {findViewById<Button>(R.id.authButtonAccept)}
    protected val textfieldUsername by lazy {findViewById<EditText>(R.id.authTextfieldUsername)}
    protected val textfieldPassword by lazy {findViewById<EditText>(R.id.authTextfieldPassword)}
    protected val textfieldServerName by lazy {findViewById<EditText>(R.id.authServerName)}
    protected val progressBar by lazy {findViewById<ProgressBar>(R.id.authProgressBar)}

    abstract fun onAcceptButtonPressed(address: String, user: User, retrofit: Retrofit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        buttonBack.setOnClickListener {
            super.onBackPressed()
        }

        buttonAccept.setOnClickListener {
            val username = textfieldUsername.text.toString()
            val password = textfieldPassword.text.toString()
            val address = textfieldServerName.text.toString()
            val retrofit = createRetrofit(address)
            if(retrofit != null){
                onAcceptButtonPressed(address, User(username, password), retrofit)
            }
        }
    }

    override fun onDisableAll() {
        progressBar.visibility = ProgressBar.VISIBLE
        buttonBack.isEnabled = false
        buttonAccept.isEnabled = false
        textfieldPassword.isEnabled = false
        textfieldUsername.isEnabled = false
    }

    override fun onEnableAll() {
        progressBar.visibility = ProgressBar.INVISIBLE
        buttonBack.isEnabled = true
        buttonAccept.isEnabled = true
        textfieldPassword.isEnabled = true
        textfieldUsername.isEnabled = true
    }
}

class LoginActivity : AuthActivity(){

    override fun onAcceptButtonPressed(address: String, user: User, retrofit: Retrofit){
        val call = retrofit.create(JsonAPI::class.java).login(user)
        runCallback(call) { _, response->
            val intent = Intent(
                applicationContext,
                CalendarActivity::class.java
            )
            intent.putExtra("username",user.username)
            intent.putExtra("jwt_token", response.headers().get("Authorization"))
            intent.putExtra("address",address)
            startActivity(intent)
        }
    }
}

class RegisterActivity : AuthActivity(){

    override fun onAcceptButtonPressed(address: String, user: User, retrofit: Retrofit){
        val call = retrofit.create(JsonAPI::class.java).register(user)
        runCallback(call){ _, _->
            Toast.makeText(this@RegisterActivity,"New user created", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
