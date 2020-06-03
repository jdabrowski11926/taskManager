package I8E2S4.mobileTaskManager.ui

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
import androidx.core.view.size
import retrofit2.Retrofit
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

abstract class TaskActivity : SessionCallbackActivity() {

    protected val buttonBack by lazy {findViewById<Button>(R.id.taskButtonBack)}
    protected val buttonDelete by lazy {findViewById<Button>(R.id.taskButtonDelete)}
    protected val buttonAccept by lazy {findViewById<Button>(R.id.taskButtonAccept)}
    protected val spinnerCategory by lazy {findViewById<Spinner>(R.id.taskSpinnerCategories)}
    protected val textfieldName by lazy {findViewById<EditText>(R.id.taskTextfieldName)}
    protected val textfieldDescription by lazy {findViewById<EditText>(R.id.taskTextfieldDescription)}
    protected val textfieldStartDate by lazy {findViewById<EditText>(R.id.taskTextfieldStartDate)}
    protected val textfieldStartTime by lazy {findViewById<EditText>(R.id.taskTextfieldStartTime)}
    protected val textfieldEndDate by lazy {findViewById<EditText>(R.id.taskTextfieldEndDate)}
    protected val textfieldEndTime by lazy {findViewById<EditText>(R.id.taskTextfieldEndTime)}
    protected val switchNotification by lazy {findViewById<Switch>(R.id.taskSwitchNotification)}
    protected val switchActive by lazy {findViewById<Switch>(R.id.taskSwitchActive)}

    abstract fun onAcceptButtonPressed(task: Task, categoryName: String, retrofit: Retrofit)
    abstract fun onDeleteButtonPressed(retrofit: Retrofit)
    abstract fun selectItemFromSpinner()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        buttonBack.setOnClickListener {
            super.onBackPressed()
        }
        setCategoriesSpinner()

        textfieldStartDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                var dateString = ""
                dateString = if(mDay<10) "0"+mDay+"/" else ""+mDay+"/"
                dateString = if(mMonth+1<10) dateString+"0"+(mMonth+1)+"/"+mYear else dateString+mMonth+"/"+mYear
                textfieldStartDate.setText(dateString)
            }, year, month, day)
            datePicker.show()
        }

        textfieldEndDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, mYear, mMonth, mDay ->
                var dateString = ""
                dateString = if(mDay<10) "0"+mDay+"/" else ""+mDay+"/"
                dateString = if(mMonth+1<10) dateString+"0"+(mMonth+1)+"/"+mYear else dateString+mMonth+"/"+mYear
                textfieldEndDate.setText(dateString)
            }, year, month, day)
            datePicker.show()
        }

        textfieldStartTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                var timeString = ""
                timeString = if(hour<10) "0"+hour else ""+hour
                timeString = if(minute<10) timeString+":0"+minute else timeString+":"+minute
                textfieldStartTime.setText(timeString)
            }
            TimePickerDialog(this,timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        textfieldEndTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                var timeString = ""
                timeString = if(hour<10) "0"+hour else ""+hour
                timeString = if(minute<10) timeString+":0"+minute else timeString+":"+minute
                textfieldEndTime.setText(timeString)
            }
            TimePickerDialog(this,timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }

        buttonAccept.setOnClickListener {
            if(isDataCorrect()==true){
                val categoryName = spinnerCategory.selectedItem.toString()
                val task = Task()
                task.name = textfieldName.text.toString()
                task.description = textfieldDescription.text.toString()
                val startDateTimeString =  ""+textfieldStartDate.text.toString()+" "+textfieldStartTime.text.toString()
                val endDateTimeString =  ""+textfieldEndDate.text.toString()+" "+textfieldEndTime.text.toString()

                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
                task.startDateTime = startDateTimeString
                task.endDateTime = endDateTimeString
                task.notification = switchNotification.isChecked
                task.active = switchActive.isChecked
                val retrofit = createRetrofit(address)
                if(retrofit!=null){
                    onAcceptButtonPressed(task,categoryName,retrofit)
                }
            }
        }

        buttonDelete.setOnClickListener{
            val retrofit = createRetrofit(address)
            if(retrofit!=null){
                onDeleteButtonPressed(retrofit)
            }
        }
    }

    fun setCategoriesSpinner(){
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
                    spinnerCategory.adapter = adapter
                    selectItemFromSpinner()
                }
            }
        }
    }

    fun isDataCorrect(): Boolean{
        if(spinnerCategory.isEmpty()){
            Toast.makeText(this@TaskActivity, "You don't have any categories, add category first", Toast.LENGTH_SHORT).show()
            return false
        }
        if(textfieldName.text.toString()==""){
            Toast.makeText(this@TaskActivity, "Field 'Name' can not be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        val startDateTimeString =  ""+textfieldStartDate.text.toString()+" "+textfieldStartTime.text.toString()
        if(isDateTimeFormatCorrect(startDateTimeString)==false){
            println(startDateTimeString)
            Toast.makeText(this@TaskActivity, "Start date/time format is incorrect (required dd/MM/yyyy HH:mm)", Toast.LENGTH_SHORT).show()
            return false
        }
        val endDateTimeString =  ""+textfieldEndDate.text.toString()+" "+textfieldEndTime.text.toString()
        if(isDateTimeFormatCorrect(endDateTimeString)==false){
            println(endDateTimeString)
            Toast.makeText(this@TaskActivity, "End date/time format is incorrect (required dd/MM/yyyy HH:mm)", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    fun isDateTimeFormatCorrect(dateString: String): Boolean {
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
        for (i in 0 until spinnerCategory.count) {
            if (spinnerCategory.getItemAtPosition(i).toString().equals(itemName)) {
                return i
            }
        }
        return 0
    }

    override fun onDisableAll() {
        buttonBack.isEnabled = false
        buttonDelete.isEnabled = false
        buttonAccept.isEnabled = false
        spinnerCategory.isEnabled = false
        textfieldName.isEnabled = false
        textfieldDescription.isEnabled = false
        textfieldStartDate.isEnabled = false
        textfieldStartTime.isEnabled = false
        textfieldEndDate.isEnabled = false
        textfieldEndTime.isEnabled = false
        switchNotification.isEnabled = false
        switchActive.isEnabled = false
    }

    override fun onEnableAll() {
        buttonBack.isEnabled = true
        buttonDelete.isEnabled = true
        buttonAccept.isEnabled = true
        spinnerCategory.isEnabled = true
        textfieldName.isEnabled = true
        textfieldDescription.isEnabled = true
        textfieldStartDate.isEnabled = true
        textfieldStartTime.isEnabled = true
        textfieldEndDate.isEnabled = true
        textfieldEndTime.isEnabled = true
        switchNotification.isEnabled = true
        switchActive.isEnabled = true
    }

}

class NewTaskActivity : TaskActivity(){

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buttonDelete.isEnabled = false
    }

    override fun onAcceptButtonPressed(task: Task, categoryName: String, retrofit: Retrofit) {
        val call = retrofit.create(JsonAPI::class.java)
            .addTask(username,categoryName,task,jwtToken)
        runCallback(call){ _, _ -> finish() }
    }

    override fun onDeleteButtonPressed(retrofit: Retrofit) {
        Toast.makeText(this@NewTaskActivity, "Can't delete new category", Toast.LENGTH_SHORT).show()
    }

    override fun onEnableAll() {
        super.onEnableAll()
        buttonDelete.isEnabled = false
    }

    override fun selectItemFromSpinner(){
        if(spinnerCategory.count!=0) spinnerCategory.setSelection(0)
    }
}

class EditTaskActivity : TaskActivity(){

    protected  val taskId: String by lazy {intent.getStringExtra("id")?:""}
    protected  val taskName: String by lazy {intent.getStringExtra("name")?:""}
    protected  val taskDescription: String by lazy {intent.getStringExtra("description")?:""}
    protected  val taskCategory: String by lazy {intent.getStringExtra("category")?:""}
    protected  val taskStartDateTime: String by lazy {intent.getStringExtra("startDateTime")?:""}
    protected  val taskEndDateTime: String by lazy {intent.getStringExtra("endDateTime")?:""}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        textfieldName.setText(taskName)
        textfieldDescription.setText(taskDescription)
        textfieldStartDate.setText(taskStartDateTime.substring(0,10))
        textfieldStartTime.setText(taskStartDateTime.substring(11))
        textfieldEndDate.setText(taskEndDateTime.substring(0,10))
        textfieldEndTime.setText(taskEndDateTime.substring(11))
        switchActive.isChecked = intent.getBooleanExtra("active", false)
        switchNotification.isChecked = intent.getBooleanExtra("notification", false)
    }

    override fun onAcceptButtonPressed(task: Task, categoryName: String, retrofit: Retrofit) {
        val call = retrofit.create(JsonAPI::class.java)
            .editTask(username,taskCategory,taskId,task,jwtToken)
        runCallback(call){ _, _ -> finish() }
    }

    override fun onDeleteButtonPressed(retrofit: Retrofit) {
        val call = retrofit.create(JsonAPI::class.java)
            .deleteTask(username,spinnerCategory.selectedItem.toString(),taskId,jwtToken)
        runCallback(call){ _, _ -> finish() }
    }

    override fun selectItemFromSpinner(){
        if(spinnerCategory.count!=0) spinnerCategory.setSelection(spinnerGetValueIndex(taskCategory))
    }
}