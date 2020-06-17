package i8e2s4.mobileTaskManager.logic.logicActivities

import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import i8e2s4.mobileTaskManager.logic.CallbackActivityCallback
import kotlinx.android.synthetic.main.activity_auth.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class CallbackActivity : AppCompatActivity(){
    private var currCall: Call<*>? = null

    var isEnabled = true
        private set

    private fun disableAll(){
        isEnabled = false
        onDisableAll()
    }

    fun enableAll(){
        isEnabled = true
        onEnableAll()
    }

    private fun onDisableAll(){
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.visibility = ProgressBar.VISIBLE
    }

    private fun onEnableAll(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.visibility = ProgressBar.INVISIBLE
    }

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
        call: Call<T>, onSuccessfulResponse: (call: Call<T>, response: Response<T>) -> Unit){
        disableAll()
        val callback: CallbackActivityCallback<T> = object :
            CallbackActivityCallback<T>(this){
            override fun onSuccessfulResponse(call: Call<T>, response: Response<T>) {
                onSuccessfulResponse(call, response)
            }
        }
        currCall = call
        call.enqueue(callback)
    }
}