package I8E2S4.mobileTaskManager.ui

import I8E2S4.mobileTaskManager.R
import I8E2S4.mobileTaskManager.logic.SessionCallbackActivity
import I8E2S4.mobileTaskManager.server.JsonAPI
import android.os.Build
import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_statictics.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class StaticticsActivity : SessionCallbackActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statictics)

        val retrofit = createRetrofit(address)
        if (retrofit != null) {
            val call = retrofit.create(JsonAPI::class.java)
                .getCategoryList(username, jwtToken)
            runCallback(call) { _, response ->
                val body = response.body()
                if (body != null) {
                    for (category in body) {
                        // TRZEBA W TYM MIEJSCU PODODAWAC WIERSZE DLA KAZDEJ KATEGORII
                        val row = TableRow(this)
                        val categoryNameTextfield = TextView(row.context)
                        val taskAmountTextfield = TextView(row.context)
                        val categoryTimeTextfield = TextView(row.context)
                        categoryNameTextfield.text = "" + category.name
                        taskAmountTextfield.text = "Amount of tasks"
                        categoryTimeTextfield.text = "Category time"
                        row.addView(categoryNameTextfield)
                        row.addView(taskAmountTextfield)
                        row.addView(categoryTimeTextfield)
                        val callTasks = retrofit.create(JsonAPI::class.java)
                            .getTaskList(username, category.name.toString(), jwtToken)
                        runCallback(callTasks) { _, responseTasks ->
                            val body = responseTasks.body()
                            var numerOfTasks = 0
                            var secondsSpent : Long = 0
                            if (body != null) {
                                for (task in body) {
                                    numerOfTasks++
                                    secondsSpent += calculateSecondsBetweenDates(task.startDateTime!!, task.endDateTime!!)
                                }
                                taskAmountTextfield.text = "" + numerOfTasks
                                categoryTimeTextfield.text = "" + convertSecondsToTimeString(secondsSpent)
                            }
                            TableLayoutStatictics.addView(row)
                        }
                    }
                }
            }
            staticticsButtonBack.setOnClickListener {
                super.onBackPressed()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun calculateSecondsBetweenDates(startDateTimeString: String, endDateTimeString: String): Long {
        if(startDateTimeString=="" || endDateTimeString == "") return 0L
        val startDateTime = LocalDateTime.parse(startDateTimeString, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        val endDateTime = LocalDateTime.parse(endDateTimeString, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        val duration: Duration = Duration.between(startDateTime, endDateTime)
        return duration.seconds
    }

    private fun convertSecondsToTimeString(seconds: Long): String{
        val day = TimeUnit.SECONDS.toDays(seconds)
        val hours = TimeUnit.SECONDS.toHours(seconds) - day * 24
        val minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.SECONDS.toHours(seconds) * 60
        val second = TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60
        if(day==0L && hours==0L && minute==0L) return "" + second + "sec"
        if(day==0L && hours==0L) return "" + minute + " min, " + second + "sec"
        if(day==0L) return "" + hours + " hrs, " + minute + " min, " + second + "sec"
        return "" + day + " days, " + hours + " hrs, " + minute + " min, " + second + "sec"
    }

    override fun onDisableAll() {
        staticticsButtonBack.isEnabled = false
    }

    override fun onEnableAll() {
        staticticsButtonBack.isEnabled = true
    }
}
