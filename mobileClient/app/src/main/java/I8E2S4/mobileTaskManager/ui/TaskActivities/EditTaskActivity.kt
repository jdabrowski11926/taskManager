package I8E2S4.mobileTaskManager.ui.TaskActivities

import I8E2S4.mobileTaskManager.model.Task
import I8E2S4.mobileTaskManager.server.JsonAPI
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_task.*
import retrofit2.Retrofit

class EditTaskActivity : TaskActivity(){

    private val taskId: String by lazy {intent.getStringExtra("id")?:""}
    private val taskName: String by lazy {intent.getStringExtra("name")?:""}
    private val taskDescription: String by lazy {intent.getStringExtra("description")?:""}
    private val taskCategory: String by lazy {intent.getStringExtra("category")?:""}
    private val taskStartDateTime: String by lazy {intent.getStringExtra("startDateTime")?:""}
    private val taskEndDateTime: String by lazy {intent.getStringExtra("endDateTime")?:""}

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskTextfieldName.setText(taskName)
        taskTextfieldDescription.setText(taskDescription)
        taskTextfieldStartDate.setText(taskStartDateTime.substring(0,10))
        taskTextfieldStartTime.setText(taskStartDateTime.substring(11))
        taskTextfieldEndDate.setText(taskEndDateTime.substring(0,10))
        taskTextfieldEndTime.setText(taskEndDateTime.substring(11))
        taskSwitchActive.isChecked = intent.getBooleanExtra("active", false)
        taskSwitchNotification.isChecked = intent.getBooleanExtra("notification", false)
    }

    override fun onAcceptButtonPressed(task: Task, categoryName: String, retrofit: Retrofit) {
        val call = retrofit.create(JsonAPI::class.java)
            .editTask(username,taskCategory,taskId,task,jwtToken)
        runCallback(call){ _, _ -> finish() }
    }

    override fun onDeleteButtonPressed(retrofit: Retrofit) {
        val call = retrofit.create(JsonAPI::class.java)
            .deleteTask(username,taskSpinnerCategories.selectedItem.toString(),taskId,jwtToken)
        runCallback(call){ _, _ -> finish() }
    }

    override fun selectItemFromSpinner(){
        if(taskSpinnerCategories.count!=0) taskSpinnerCategories.setSelection(spinnerGetValueIndex(taskCategory))
    }
}