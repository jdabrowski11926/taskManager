package i8e2s4.mobileTaskManager.ui

import i8e2s4.mobileTaskManager.R
import i8e2s4.mobileTaskManager.logic.logicActivities.SessionCallbackActivity
import i8e2s4.mobileTaskManager.model.UserEditPassword
import i8e2s4.mobileTaskManager.server.JsonAPI
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
}
