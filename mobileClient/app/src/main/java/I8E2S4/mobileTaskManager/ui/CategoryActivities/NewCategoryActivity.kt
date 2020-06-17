package I8E2S4.mobileTaskManager.ui.CategoryActivities

import I8E2S4.mobileTaskManager.model.Category
import I8E2S4.mobileTaskManager.server.JsonAPI
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_category.*
import retrofit2.Retrofit

class NewCategoryActivity : CategoryActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryButtonDelete.isEnabled = false
    }

    override fun onAcceptButtonPressed(category: Category, retrofit: Retrofit) {
        val call = retrofit.create(JsonAPI::class.java)
            .addCategory(username, category, jwtToken)
        runCallback(call){ _, _ -> finish() }
    }

    override fun onEnableAll() {
        super.onEnableAll()
        categoryButtonDelete.isEnabled = false
    }
}