package i8e2s4.mobileTaskManager.ui.categoryActivities

import i8e2s4.mobileTaskManager.model.Category
import i8e2s4.mobileTaskManager.server.JsonAPI
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_category.*
import retrofit2.Retrofit

class EditCategoryActivity : CategoryActivity(){

    private val taskName: String by lazy {intent.getStringExtra("name")?:""}
    private val taskDescription: String by lazy {intent.getStringExtra("description")?:""}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryButtonDelete.isEnabled = true
        categoryTextfieldName.setText(taskName)
        categoryTextfieldDescription.setText(taskDescription)
    }

    override fun onAcceptButtonPressed(category: Category, retrofit: Retrofit) {
        val call = retrofit.create(JsonAPI::class.java)
            .editCategory(username,taskName,category,jwtToken)
        runCallback(call){ _, _ -> finish() }
    }
}