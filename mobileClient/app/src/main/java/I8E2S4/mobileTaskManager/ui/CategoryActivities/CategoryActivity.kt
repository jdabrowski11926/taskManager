package I8E2S4.mobileTaskManager.ui.CategoryActivities

import I8E2S4.mobileTaskManager.R
import I8E2S4.mobileTaskManager.logic.SessionCallbackActivity
import I8E2S4.mobileTaskManager.model.Category
import I8E2S4.mobileTaskManager.server.JsonAPI
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_category.*
import retrofit2.Retrofit

abstract class CategoryActivity : SessionCallbackActivity() {

    abstract fun onAcceptButtonPressed(category: Category, retrofit: Retrofit)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        categoryButtonBack.setOnClickListener {
            super.onBackPressed()
        }

        categoryButtonDelete.setOnClickListener {
            val category = Category()
            category.name = categoryTextfieldName.text.toString()
            category.description = categoryTextfieldDescription.text.toString()
            val retrofit = createRetrofit(address)
            if(retrofit!=null){
                val call = retrofit.create(JsonAPI::class.java)
                    .deleteCategory(username, category.name!!,jwtToken)
                runCallback(call){ _, _ -> finish() }
            }
        }

        categoryButtonAccept.setOnClickListener{
            val category = Category()
            category.name = categoryTextfieldName.text.toString()
            category.description = categoryTextfieldDescription.text.toString()
            val retrofit = createRetrofit(address)
            if(retrofit!=null){
                onAcceptButtonPressed(category, retrofit)
            }
        }
    }

    override fun onDisableAll() {
        categoryButtonBack.isEnabled = false
        categoryButtonDelete.isEnabled = false
        categoryButtonAccept.isEnabled = false
        categoryTextfieldName.isEnabled = false
        categoryTextfieldDescription.isEnabled = false
    }

    override fun onEnableAll() {
        categoryButtonBack.isEnabled = true
        categoryButtonDelete.isEnabled = true
        categoryButtonAccept.isEnabled = true
        categoryTextfieldName.isEnabled = true
        categoryTextfieldDescription.isEnabled = true
    }
}



