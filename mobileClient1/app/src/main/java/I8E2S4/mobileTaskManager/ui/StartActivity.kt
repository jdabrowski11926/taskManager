package I8E2S4.mobileTaskManager.ui

import I8E2S4.mobileTaskManager.R
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StartActivity : AppCompatActivity() {

    protected val buttonLogin by lazy {findViewById<Button>(R.id.buttonLogin)}
    protected val buttonRegister by lazy {findViewById<Button>(R.id.buttonRegister)}

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
