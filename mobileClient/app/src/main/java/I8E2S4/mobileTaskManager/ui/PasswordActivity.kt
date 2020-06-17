package I8E2S4.mobileTaskManager.ui

import I8E2S4.mobileTaskManager.R
import I8E2S4.mobileTaskManager.logic.SessionCallbackActivity
import I8E2S4.mobileTaskManager.model.UserEditPassword
import I8E2S4.mobileTaskManager.server.JsonAPI
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_password.*

class PasswordActivity : SessionCallbackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        passwordButtonBack.setOnClickListener {
            super.onBackPressed()
        }

        passwordButtonAccept.setOnClickListener{
            if(passwordTextfieldNewPassword.text.toString() == passwordTextfieldConfirmPassword.text.toString()){
                val retrofit = createRetrofit(address)
                if(retrofit!=null){
                    val call = retrofit.create(JsonAPI::class.java)
                        .editAccount(username, UserEditPassword(passwordTextfieldOldPassword.text.toString(),passwordTextfieldNewPassword.text.toString()),jwtToken)
                    runCallback(call) { _, _ ->
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
        passwordButtonBack.isEnabled = false
        passwordButtonAccept.isEnabled = false
        passwordTextfieldOldPassword.isEnabled = false
        passwordTextfieldNewPassword.isEnabled = false
        passwordTextfieldConfirmPassword.isEnabled = false
    }

    override fun onEnableAll() {
        passwordButtonBack.isEnabled = true
        passwordButtonAccept.isEnabled = true
        passwordTextfieldOldPassword.isEnabled = true
        passwordTextfieldNewPassword.isEnabled = true
        passwordTextfieldConfirmPassword.isEnabled = true
    }
}
