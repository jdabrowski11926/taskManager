package I8E2S4.mobileTaskManager.ui.TaskActivities

import I8E2S4.mobileTaskManager.R
import I8E2S4.mobileTaskManager.logic.SessionCallbackActivity
import I8E2S4.mobileTaskManager.model.Task
import I8E2S4.mobileTaskManager.server.JsonAPI
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.isEmpty
import kotlinx.android.synthetic.main.activity_task.*
import retrofit2.Retrofit
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

abstract class TaskActivity : SessionCallbackActivity() {

    abstract fun onAcceptButtonPressed(task: Task, categoryName: String, retrofit: Retrofit)
    abstract fun onDeleteButtonPressed(retrofit: Retrofit)
    abstract fun selectItemFromSpinner()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        taskButtonBack.setOnClickListener {
            super.onBackPressed()
        }
        setCategoriesSpinner()

        taskTextfieldStartDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                var dateString = ""
                dateString = if(mDay<10) "0$mDay/" else "$mDay/"
                dateString = if(mMonth+1<10) dateString+"0"+(mMonth+1)+"/"+mYear else "$dateString$mMonth/$mYear"
                taskTextfieldStartDate.setText(dateString)
            }, year, month, day)
            datePicker.show()
        }

        taskTextfieldEndDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                var dateString = ""
                dateString = if(mDay<10) "0$mDay/" else "$mDay/"
                dateString = if(mMonth+1<10) dateString+"0"+(mMonth+1)+"/"+mYear else "$dateString$mMonth/$mYear"
                taskTextfieldEndDate.setText(dateString)
            }, year, month, day)
            datePicker.show()
        }

        taskTextfieldStartTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                var timeString = ""
                timeString = if(hour<10) "0$hour" else ""+hour
                timeString = if(minute<10) "$timeString:0$minute" else "$timeString:$minute"
                taskTextfieldStartTime.setText(timeString)
            }
            TimePickerDialog(this,timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        taskTextfieldEndTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                var timeString = ""
                timeString = if(hour<10) "0$hour" else ""+hour
                timeString = if(minute<10) "$timeString:0$minute" else "$timeString:$minute"
                taskTextfieldEndTime.setText(timeString)
            }
            TimePickerDialog(this,timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        taskButtonAccept.setOnClickListener {
            if(isDataCorrect()){
                val categoryName = taskSpinnerCategories.selectedItem.toString()
                val task = Task()
                task.name = taskTextfieldName.text.toString()
                task.description = taskTextfieldDescription.text.toString()
                val startDateTimeString =  ""+taskTextfieldStartDate.text.toString()+" "+taskTextfieldStartTime.text.toString()
                val endDateTimeString =  ""+taskTextfieldEndDate.text.toString()+" "+taskTextfieldEndTime.text.toString()

                task.startDateTime = startDateTimeString
                task.endDateTime = endDateTimeString
                task.notification = taskSwitchNotification.isChecked
                task.active = taskSwitchActive.isChecked
                val retrofit = createRetrofit(address)
                if(retrofit!=null){
                    onAcceptButtonPressed(task,categoryName,retrofit)
                }
            }
        }

        taskButtonDelete.setOnClickListener{
            val retrofit = createRetrofit(address)
            if(retrofit!=null){
                onDeleteButtonPressed(retrofit)
            }
        }
    }

    private fun setCategoriesSpinner(){
        val retrofit = createRetrofit(address)
        if(retrofit!=null){
            val call = retrofit.create(JsonAPI::class.java)
                .getCategoryList(username,jwtToken)
            runCallback(call){ _, response ->
                val body = response.body()
                val categoryNameList = ArrayList<String>()
                if (body != null) {
                    for (category in body) {
                        categoryNameList.add(category.name.toString())
                    }
                    val adapter = ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, categoryNameList)
                    taskSpinnerCategories.adapter = adapter
                    selectItemFromSpinner()
                }
            }
        }
    }

    private fun isDataCorrect(): Boolean{
        if(taskSpinnerCategories.isEmpty()){
            Toast.makeText(this@TaskActivity, "You don't have any categories, add category first", Toast.LENGTH_SHORT).show()
            return false
        }
        if(taskTextfieldName.text.toString()==""){
            Toast.makeText(this@TaskActivity, "Field 'Name' can not be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        val startDateTimeString =  ""+taskTextfieldStartDate.text.toString()+" "+taskTextfieldStartTime.text.toString()
        if(!isDateTimeFormatCorrect(startDateTimeString)){
            println(startDateTimeString)
            Toast.makeText(this@TaskActivity, "Start date/time format is incorrect (required dd/MM/yyyy HH:mm)", Toast.LENGTH_SHORT).show()
            return false
        }
        val endDateTimeString =  ""+taskTextfieldEndDate.text.toString()+" "+taskTextfieldEndTime.text.toString()
        if(!isDateTimeFormatCorrect(endDateTimeString)){
            println(endDateTimeString)
            Toast.makeText(this@TaskActivity, "End date/time format is incorrect (required dd/MM/yyyy HH:mm)", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun isDateTimeFormatCorrect(dateString: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
        dateFormat.isLenient = false
        try {
            dateFormat.parse(dateString.trim())
        } catch (pe: ParseException) {
            return false
        }
        return true
    }

    fun spinnerGetValueIndex(itemName: String): Int {
        for (i in 0 until taskSpinnerCategories.count) {
            if (taskSpinnerCategories.getItemAtPosition(i).toString().equals(itemName)) {
                return i
            }
        }
        return 0
    }

    override fun onDisableAll() {
        taskButtonBack.isEnabled = false
        taskButtonDelete.isEnabled = false
        taskButtonAccept.isEnabled = false
        taskSpinnerCategories.isEnabled = false
        taskTextfieldName.isEnabled = false
        taskTextfieldDescription.isEnabled = false
        taskTextfieldStartDate.isEnabled = false
        taskTextfieldStartTime.isEnabled = false
        taskTextfieldEndDate.isEnabled = false
        taskTextfieldEndTime.isEnabled = false
        taskSwitchNotification.isEnabled = false
        taskSwitchActive.isEnabled = false
    }

    override fun onEnableAll() {
        taskButtonBack.isEnabled = true
        taskButtonDelete.isEnabled = true
        taskButtonAccept.isEnabled = true
        taskSpinnerCategories.isEnabled = true
        taskTextfieldName.isEnabled = true
        taskTextfieldDescription.isEnabled = true
        taskTextfieldStartDate.isEnabled = true
        taskTextfieldStartTime.isEnabled = true
        taskTextfieldEndDate.isEnabled = true
        taskTextfieldEndTime.isEnabled = true
        taskSwitchNotification.isEnabled = true
        taskSwitchActive.isEnabled = true
    }
}