package I8E2S4.mobileTaskManager.logic

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class SessionCallbackActivity: CallbackActivity(){
    protected  val username: String by lazy {intent.getStringExtra("username")?:""}
    protected  val jwtToken: String by lazy {intent.getStringExtra("jwt_token")?:""}
    protected  val address: String by lazy {intent.getStringExtra("address")?:""}
}

abstract class CallbackActivity : AppCompatActivity(){
    var currCall: Call<*>? = null
        private set

    var isEnabled = true
        private set

    fun disableAll(){
        isEnabled = false
        onDisableAll()
    }

    fun enableAll(){
        isEnabled = true
        onEnableAll()
    }

    abstract fun onDisableAll()

    abstract fun onEnableAll()

    override fun onBackPressed() {
        if (isEnabled) {
            super.onBackPressed()
        } else {
            currCall?.cancel()
            enableAll()
        }
    }

    fun createRetrofit(address: String?): Retrofit?{
        if(address == null) return null
        try {
            return Retrofit.Builder()
                .baseUrl(address)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, "Błędny adres serwera.", Toast.LENGTH_SHORT).show()
        }
        return null
    }

    fun <T>runCallback(
        call: Call<T>,
        onSuccessfulResponse: (call: Call<T>, response: Response<T>) -> Unit){
            disableAll()
            val callback: CallbackActivityCallback<T> = object :CallbackActivityCallback<T>(this){
               override fun onSuccessfulResponse(call: Call<T>, response: Response<T>) {
                    onSuccessfulResponse(call, response)
                }
            }
            currCall = call
            call.enqueue(callback)
    }
}