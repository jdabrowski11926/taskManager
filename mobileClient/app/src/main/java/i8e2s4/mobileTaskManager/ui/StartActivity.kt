package i8e2s4.mobileTaskManager.ui

import i8e2s4.mobileTaskManager.R
import i8e2s4.mobileTaskManager.ui.authActivities.LoginActivity
import i8e2s4.mobileTaskManager.ui.authActivities.RegisterActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        buttonLogin.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
        }

        buttonRegister.setOnClickListener{
            startActivity(
                Intent(this, RegisterActivity::class.java)
            )
        }
    }
}
