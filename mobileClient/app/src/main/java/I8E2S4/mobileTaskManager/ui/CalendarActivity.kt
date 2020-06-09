package I8E2S4.mobileTaskManager.ui

import I8E2S4.mobileTaskManager.R
import I8E2S4.mobileTaskManager.logic.CalendarAdapter
import I8E2S4.mobileTaskManager.logic.CalendarItem
import I8E2S4.mobileTaskManager.logic.SessionCallbackActivity
import I8E2S4.mobileTaskManager.model.Category
import I8E2S4.mobileTaskManager.model.Task
import I8E2S4.mobileTaskManager.server.JsonAPI
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.collections.ArrayList


class CalendarActivity : SessionCallbackActivity() {

    protected val toolbar by lazy {findViewById<Toolbar>(R.id.toolbar)}
    protected val floatingButton by lazy {findViewById<FloatingActionButton>(R.id.calendarFloatingActionButton)}
    protected val listView by lazy {findViewById<RecyclerView>(R.id.calendarListView)}
    protected val taskList = ArrayList<Task>()
    protected val taskListAdapter by lazy {ArrayAdapter<Task>(this,R.layout.activity_calendar,taskList)}

    protected val categoryEditMenuItems = ArrayList<MenuItem>()
    protected val categoryFilterMenuItems = ArrayList<MenuItem>()
    protected var menuItemEditCategories : SubMenu? = null
    protected var menuItemFilterCategories : SubMenu? = null
    protected var userCategoryList = ArrayList<Category>()
    protected var sortingMethod = "date_asc"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        toolbar.title = "Calendar"
        setSupportActionBar(toolbar)
        listView.setHasFixedSize(true)

        floatingButton.setOnClickListener {
            val popupMenu: PopupMenu = PopupMenu(this,floatingButton)
            popupMenu.menuInflater.inflate(R.menu.create_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item->
                when(item.itemId){
                    R.id.item_create_category->{
                        val intent = Intent(this, NewCategoryActivity::class.java)
                        intent.putExtra("jwt_token", jwtToken)
                        intent.putExtra("username", username)
                        intent.putExtra("address", address)
                        startActivity(intent)
                    }
                    R.id.item_create_task->{
                        val intent = Intent(this, NewTaskActivity::class.java)
                        intent.putExtra("jwt_token", jwtToken)
                        intent.putExtra("username", username)
                        intent.putExtra("address", address)
                        startActivity(intent)
                    }
                }
                true
            })
            popupMenu.show()
        }
    }

    override fun onResume() {
        super.onResume()
        getCategoryList()
        invalidateOptionsMenu()
    }

    fun getCategoryList(){
        userCategoryList.clear()
        val retrofit = createRetrofit(address)
        if(retrofit!=null) {
            val call = retrofit.create(JsonAPI::class.java)
                .getCategoryList(username, jwtToken)
            runCallback(call) { _, response ->
                val body = response.body()
                if (body != null) {
                    for(category in body){
                        userCategoryList.add(category)
                    }
                    updateMenuItems()
                    updateTaskList()
                }
            }
        }
    }

    fun updateMenuItems(){
        categoryEditMenuItems.clear()
        categoryFilterMenuItems.clear()
        for(category in userCategoryList){
            if(menuItemEditCategories!=null){
                categoryEditMenuItems.add(menuItemEditCategories?.add("" + category.name)!!)
            }
            if(menuItemFilterCategories!=null){
                var menuItemFilter = menuItemFilterCategories?.add("" + category.name)!!
                menuItemFilter.isCheckable = true
                menuItemFilter.isChecked = true
                categoryFilterMenuItems.add(menuItemFilter)
            }
        }
    }

    fun updateTaskList(){
        val retrofit = createRetrofit(address)
        val calendarItemList = ArrayList<CalendarItem>()
        if(retrofit!=null){
            for(category in userCategoryList){
                if(isCategoryAllowedByFilter(category)){
                    val callCategory = retrofit.create(JsonAPI::class.java)
                        .getTaskList(username,category.name.toString(),jwtToken)
                    runCallback(callCategory){ _, responseTasks ->
                        val bodyTasks = responseTasks.body()
                        if (bodyTasks != null) {
                            for(task in bodyTasks){
                                calendarItemList.add(CalendarItem(task.id!!,task.name.toString(),task.description.toString(),category.name.toString(),task.startDateTime.toString(),task.endDateTime.toString(),task.active!!,task.notification!!))
                            }
                            var sortedList = sortCalendarItemList(calendarItemList)
                            listView.adapter = CalendarAdapter(sortedList,this, jwtToken,username, address)
                            listView.layoutManager = LinearLayoutManager(this)
                        }
                    }
                }
                if(!isAnyCategoryAllowedByFilter()){
                    listView.adapter = CalendarAdapter(calendarItemList,this, jwtToken,username, address)
                    listView.layoutManager = LinearLayoutManager(this)
                }
            }
        }
    }

    fun sortCalendarItemList(calendarItemList: ArrayList<CalendarItem>):ArrayList<CalendarItem>{
        if(sortingMethod=="date_asc")           return ArrayList(calendarItemList.sortedWith(compareBy {it.textStartDateTime}))
        if(sortingMethod=="date_desc")          return ArrayList(calendarItemList.sortedWith(compareBy {it.textStartDateTime}).reversed())
        if(sortingMethod=="name_asc")           return ArrayList(calendarItemList.sortedWith(compareBy {it.textName}))
        if(sortingMethod=="name_desc")          return ArrayList(calendarItemList.sortedWith(compareBy {it.textName}).reversed())
        if(sortingMethod=="category_asc")       return ArrayList(calendarItemList.sortedWith(compareBy {it.textCategory}))
        if(sortingMethod=="category_desc")      return ArrayList(calendarItemList.sortedWith(compareBy {it.textCategory}).reversed())
        if(sortingMethod=="active_asc")         return ArrayList(calendarItemList.sortedWith(compareBy {it.booleanActive}).reversed())
        if(sortingMethod=="active_desc")        return ArrayList(calendarItemList.sortedWith(compareBy {it.booleanActive}))
        if(sortingMethod=="notification_asc")   return ArrayList(calendarItemList.sortedWith(compareBy {it.booleanNotification}).reversed())
        if(sortingMethod=="notification_desc")  return ArrayList(calendarItemList.sortedWith(compareBy {it.booleanNotification}))
        return calendarItemList
    }

    fun isCategoryAllowedByFilter(category: Category): Boolean{
        for(categoryItem in categoryFilterMenuItems){
            if(category.name.equals(categoryItem.title.toString())){
                println("KATEGORIA "+categoryItem.title.toString()+" jest ustawiona na "+categoryItem.isChecked.toString())
                return categoryItem.isChecked
            }
        }
        return false
    }

    fun isAnyCategoryAllowedByFilter(): Boolean{
        for(category in userCategoryList){
            if(isCategoryAllowedByFilter(category)) return true
        }
        return false
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.calendar_menu, menu)
        println("CREATE OPTION MENU")

        menuItemEditCategories = menu.addSubMenu("Edit categories")
        menuItemFilterCategories = menu.addSubMenu("Filter categories")

        updateMenuItems()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.item_change_password -> {
                val intent = Intent(this, PasswordActivity::class.java)
                intent.putExtra("jwt_token", jwtToken)
                intent.putExtra("username", username)
                intent.putExtra("address", address)
                startActivity(intent)
            }
            R.id.item_statistics -> {
                val intent = Intent(this, StaticticsActivity::class.java)
                intent.putExtra("jwt_token", jwtToken)
                intent.putExtra("username", username)
                intent.putExtra("address", address)
                startActivity(intent)
            }
            R.id.item_log_out -> {
                val intent = Intent(
                    applicationContext,
                    StartActivity::class.java
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                return true
            }
            R.id.item_sort_name_asc ->{
                println("SORTOWANIE PO NAZWIE (+)")
                sortingMethod = "name_asc"
                updateTaskList()
            }
            R.id.item_sort_name_desc ->{
                println("SORTOWANIE PO NAZWIE (-)")
                sortingMethod = "name_desc"
                updateTaskList()
            }
            R.id.item_sort_date_asc ->{
                println("SORTOWANIE PO DACIE (+)")
                sortingMethod = "date_asc"
                updateTaskList()
            }
            R.id.item_sort_date_desc ->{
                println("SORTOWANIE PO DACIE (-)")
                sortingMethod = "date_desc"
                updateTaskList()
            }
            R.id.item_sort_category_asc ->{
                println("SORTOWANIE PO KATEGORII (+)")
                sortingMethod = "category_asc"
                updateTaskList()
            }
            R.id.item_sort_category_desc ->{
                println("SORTOWANIE PO KATEGORII (-)")
                sortingMethod = "category_desc"
                updateTaskList()
            }
            R.id.item_sort_active_asc ->{
                println("SORTOWANIE PO AKTYWNOSCI (+)")
                sortingMethod = "active_asc"
                updateTaskList()
            }
            R.id.item_sort_active_desc ->{
                println("SORTOWANIE PO AKTYWNOSCI (-)")
                sortingMethod = "active_desc"
                updateTaskList()
            }
            R.id.item_sort_notification_asc ->{
                println("SORTOWANIE PO NOTYFIKACJI (+)")
                sortingMethod = "notification_asc"
                updateTaskList()
            }
            R.id.item_sort_notification_desc ->{
                println("SORTOWANIE PO NOTYFIKACJI (-)")
                sortingMethod = "notification_desc"
                updateTaskList()
            }
        }
        for(categoryMenuItem in categoryEditMenuItems){
            if(item == categoryMenuItem){
                if(getCategoryOfName(item.title.toString())!=null){
                    val intent = Intent(this, EditCategoryActivity::class.java)
                    intent.putExtra("jwt_token", jwtToken)
                    intent.putExtra("username", username)
                    intent.putExtra("address", address)
                    intent.putExtra("name", item.title.toString())
                    intent.putExtra("description", (getCategoryOfName(item.title.toString())?.description ?: ""))
                    startActivity(intent)
                }
            }
        }
        for(categoryMenuItem in categoryFilterMenuItems) {
            if (item == categoryMenuItem) {
                println("FILTROWANIE PO KATEGORII : "+categoryMenuItem.title.toString())
                categoryMenuItem.isChecked = !categoryMenuItem.isChecked
                updateTaskList()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun getCategoryOfName(categoryName: String): Category? {
        for(category in userCategoryList){
            if(categoryName.equals(category.name))
                return category
        }
        return null
    }

    override fun onDisableAll() {}
    override fun onEnableAll() {}
}