package I8E2S4.mobileTaskManager.ui

import I8E2S4.mobileTaskManager.R
import I8E2S4.mobileTaskManager.ui.AuthActivities.LoginActivity
import I8E2S4.mobileTaskManager.ui.AuthActivities.RegisterActivity
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
