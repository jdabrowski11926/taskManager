package i8e2s4.mobileTaskManager.logic

import android.util.Log
import android.widget.Toast
import i8e2s4.mobileTaskManager.logic.logicActivities.CallbackActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class CallbackActivityCallback<T>(private val activity: CallbackActivity) : Callback<T> {

    abstract fun onSuccessfulResponse(call: Call<T>, response: Response<T>)

    override fun onResponse(call: Call<T>, response: Response<T>) {
        activity.enableAll()
        if (!response.isSuccessful) {
            Toast.makeText(activity, "Błąd ${response.code()}: ${response.message()}" , Toast.LENGTH_SHORT).show()
            return
        }
        onSuccessfulResponse(call, response)
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        activity.enableAll()
        Toast.makeText(activity, "Błąd połączenia.", Toast.LENGTH_SHORT).show()
        Log.d("onFailure", t.message)
    }
}