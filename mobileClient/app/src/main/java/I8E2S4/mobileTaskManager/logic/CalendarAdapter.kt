package I8E2S4.mobileTaskManager.logic

import I8E2S4.mobileTaskManager.R
import I8E2S4.mobileTaskManager.ui.CalendarActivity
import I8E2S4.mobileTaskManager.ui.TaskActivities.EditTaskActivity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.task_item.view.*

class CalendarAdapter(private val calendarList: ArrayList<CalendarItem>, private val calendarActivity:CalendarActivity,
                      private val jwtToken: String, private val username: String, private val address: String):
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textViewStartDateTime: TextView = itemView.textViewStartDateTime
        val textViewEndDateTime: TextView = itemView.textViewEndDateTime
        val textViewName: TextView = itemView.textViewName
        val textViewDescription: TextView = itemView.textViewDescription
        val textViewCategory: TextView = itemView.textViewCategory
        val switchListActive: Switch = itemView.switchListActive
        val switchListNotification: Switch = itemView.switchListNotification
        val buttonEditTask : Button = itemView.buttonEditTask
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.task_item, parent, false)
        itemView.setOnLongClickListener {
            return@setOnLongClickListener true
        }
        return CalendarViewHolder(itemView)
    }

    override fun getItemCount() = calendarList.size

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val currentItem = calendarList[position]
        holder.textViewName.text = "Name: "+currentItem.textName
        holder.textViewDescription.text = "Description: "+currentItem.textDescription
        holder.textViewCategory.text = "Category: "+currentItem.textCategory
        holder.textViewStartDateTime.text = "Start Date/Time: "+currentItem.textStartDateTime
        holder.textViewEndDateTime.text = "End Date/Time: "+currentItem.textEndDateTime
        holder.switchListActive.isChecked = currentItem.booleanActive!!
        holder.switchListNotification.isChecked = currentItem.booleanNotification!!
        holder.switchListActive.isEnabled = false
        holder.switchListNotification.isEnabled = false
        holder.buttonEditTask.setOnClickListener {
            println("NUMER POZYCJI: "+position)
            val intent = Intent(calendarActivity, EditTaskActivity::class.java)
            intent.putExtra("jwt_token", jwtToken)
            intent.putExtra("username", username)
            intent.putExtra("address", address)
            intent.putExtra("id", currentItem.id.toString())
            intent.putExtra("name", currentItem.textName)
            intent.putExtra("description", currentItem.textDescription)
            intent.putExtra("category", currentItem.textCategory)
            intent.putExtra("startDateTime", currentItem.textStartDateTime)
            intent.putExtra("endDateTime", currentItem.textEndDateTime)
            intent.putExtra("active", currentItem.booleanActive!!)
            intent.putExtra("notification", currentItem.booleanNotification!!)
            calendarActivity.startActivity(intent)
        }
    }

}