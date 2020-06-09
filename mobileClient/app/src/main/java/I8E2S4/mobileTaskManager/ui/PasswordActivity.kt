package I8E2S4.mobileTaskManager.ui

import I8E2S4.mobileTaskManager.R
import I8E2S4.mobileTaskManager.logic.SessionCallbackActivity
import I8E2S4.mobileTaskManager.model.User
import I8E2S4.mobileTaskManager.model.UserEditPassword
import I8E2S4.mobileTaskManager.server.JsonAPI
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class PasswordActivity : SessionCallbackActivity() {

    protected val buttonBack by lazy {findViewById<Button>(R.id.passwordButtonBack)}
    protected val buttonAccept by lazy {findViewById<Button>(R.id.passwordButtonAccept)}
    protected val textfieldOldPassword by lazy {findViewById<EditText>(R.id.passwordTextfieldOldPassword)}
    protected val textfieldNewPassword by lazy {findViewById<EditText>(R.id.passwordTextfieldNewPassword)}
    protected val textfieldConfirmPassword by lazy {findViewById<EditText>(R.id.passwordTextfieldConfirmPassword)}


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        buttonBack.setOnClickListener {
            super.onBackPressed()
        }

        buttonAccept.setOnClickListener{
            if(textfieldNewPassword.text.toString().equals(textfieldConfirmPassword.text.toString())){
                val retrofit = createRetrofit(address)
                if(retrofit!=null){
                    val call = retrofit.create(JsonAPI::class.java)
                        .editAccount(username, UserEditPassword(textfieldOldPassword.text.toString(),textfieldNewPassword.text.toString()),jwtToken)
                    runCallback(call) { _, responseTasks ->
                        val intent = Intent(applicationContext, StartActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        Toast.makeText(this@PasswordActivity, "Password has been changed. Please log in again", Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    }
                }
            }
            else Toast.makeText(this@PasswordActivity, "Fields 'New password' and 'confirm password' are different", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDisableAll() {
        buttonBack.isEnabled = false
        buttonAccept.isEnabled = false
        textfieldOldPassword.isEnabled = false
        textfieldNewPassword.isEnabled = false
        textfieldConfirmPassword.isEnabled = false
    }

    override fun onEnableAll() {
        buttonBack.isEnabled = true
        buttonAccept.isEnabled = true
        textfieldOldPassword.isEnabled = true
        textfieldNewPassword.isEnabled = true
        textfieldConfirmPassword.isEnabled = true
    }
}
