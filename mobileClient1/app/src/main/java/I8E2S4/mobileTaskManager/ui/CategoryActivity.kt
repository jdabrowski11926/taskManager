package I8E2S4.mobileTaskManager.ui

import I8E2S4.mobileTaskManager.R
import I8E2S4.mobileTaskManager.logic.SessionCallbackActivity
import I8E2S4.mobileTaskManager.model.Category
import I8E2S4.mobileTaskManager.server.JsonAPI
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import retrofit2.Retrofit

abstract class CategoryActivity : SessionCallbackActivity() {

    protected val buttonBack by lazy {findViewById<Button>(R.id.categoryButtonBack)}
    protected val buttonDelete by lazy {findViewById<Button>(R.id.categoryButtonDelete)}
    protected val buttonAccept by lazy {findViewById<Button>(R.id.categoryButtonAccept)}
    protected val textfieldName by lazy {findViewById<EditText>(R.id.categoryTextfieldName)}
    protected val textfieldDescription by lazy {findViewById<EditText>(R.id.categoryTextfieldDescription)}

    abstract fun onAcceptButtonPressed(category: Category, retrofit: Retrofit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        buttonBack.setOnClickListener {
            super.onBackPressed()
        }

        buttonDelete.setOnClickListener {
            val category = Category()
            category.name = textfieldName.text.toString()
            category.description = textfieldDescription.text.toString()
            val retrofit = createRetrofit(address)
            if(retrofit!=null){
                val call = retrofit.create(JsonAPI::class.java)
                    .deleteCategory(username, category.name!!,jwtToken)
                runCallback(call){ _, _ -> finish() }
            }
        }

        buttonAccept.setOnClickListener{
            val category = Category()
            category.name = textfieldName.text.toString()
            category.description = textfieldDescription.text.toString()
            val retrofit = createRetrofit(address)
            if(retrofit!=null){
                onAcceptButtonPressed(category, retrofit)
            }
        }
    }

    override fun onDisableAll() {
        buttonBack.isEnabled = false
        buttonDelete.isEnabled = false
        buttonAccept.isEnabled = false
        textfieldName.isEnabled = false
        textfieldDescription.isEnabled = false
    }

    override fun onEnableAll() {
        buttonBack.isEnabled = true
        buttonDelete.isEnabled = true
        buttonAccept.isEnabled = true
        textfieldName.isEnabled = true
        textfieldDescription.isEnabled = true
    }
}

class NewCategoryActivity : CategoryActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buttonDelete.isEnabled = false
    }

    override fun onAcceptButtonPressed(category: Category, retrofit: Retrofit) {
        val call = retrofit.create(JsonAPI::class.java)
            .addCategory(username, category, jwtToken)
        runCallback(call){ _, _ -> finish() }
    }

    override fun onEnableAll() {
        super.onEnableAll()
        buttonDelete.isEnabled = false
    }
}

class EditCategoryActivity : CategoryActivity(){

    protected  val taskName: String by lazy {intent.getStringExtra("name")?:""}
    protected  val taskDescription: String by lazy {intent.getStringExtra("description")?:""}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buttonDelete.isEnabled = true
        textfieldName.setText(taskName)
        textfieldDescription.setText(taskDescription)
    }

    override fun onAcceptButtonPressed(category: Category, retrofit: Retrofit) {
        val call = retrofit.create(JsonAPI::class.java)
            .editCategory(username,taskName,category,jwtToken)
        runCallback(call){ _, _ -> finish() }
    }
}