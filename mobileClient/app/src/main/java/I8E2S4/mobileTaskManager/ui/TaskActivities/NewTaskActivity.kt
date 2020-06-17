package I8E2S4.mobileTaskManager.ui.TaskActivities

import I8E2S4.mobileTaskManager.model.Task
import I8E2S4.mobileTaskManager.server.JsonAPI
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_task.*
import retrofit2.Retrofit

class NewTaskActivity : TaskActivity(){

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        taskButtonDelete.isEnabled = false
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
        taskButtonDelete.isEnabled = false
    }

    override fun selectItemFromSpinner(){
        if(taskSpinnerCategories.count!=0) taskSpinnerCategories.setSelection(0)
    }
}