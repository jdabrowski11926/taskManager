package i8e2s4.mobileTaskManager.server

import i8e2s4.mobileTaskManager.model.Category
import i8e2s4.mobileTaskManager.model.Task
import i8e2s4.mobileTaskManager.model.User
import i8e2s4.mobileTaskManager.model.UserEditPassword
import retrofit2.Call
import retrofit2.http.*

interface JsonAPI{

    @POST("sign-up")
    fun register(@Body user: User): Call<Void>

    @POST("login")
    fun login(@Body user: User): Call<Void>

    @POST("user/{username}/edit_account")
    fun editAccount(@Path("username") username: String,
                    @Body editPassword: UserEditPassword,
                    @Header("Authorization") jwtToken: String): Call<Void>

    @POST("user/{username}/category")
    fun addCategory(@Path("username") username: String,
                    @Body category: Category,
                    @Header("Authorization") jwtToken: String): Call<Void>

    @GET("user/{username}/category")
    fun getCategoryList(@Path("username") username: String,
                        @Header("Authorization") jwtToken: String): Call<List<Category>>

    @PUT("user/{username}/category/{category_name}")
    fun editCategory(@Path("username") username: String,
                     @Path("category_name") categoryName: String,
                     @Body category: Category,
                     @Header("Authorization") jwtToken: String): Call<Void>

    @DELETE("user/{username}/category/{category_name}")
    fun deleteCategory(@Path("username") username: String,
                       @Path("category_name") categoryName: String,
                       @Header("Authorization") jwtToken: String): Call<Void>

    @GET("user/{username}/category/{category_name}/task")
    fun getTaskList(@Path("username") username: String,
                    @Path("category_name") categoryName: String,
                    @Header("Authorization") jwtToken: String): Call<List<Task>>

    @POST("user/{username}/category/{category_name}/task")
    fun addTask(@Path("username") username: String,
                @Path("category_name") categoryName: String,
                @Body task: Task,
                @Header("Authorization") jwtToken: String): Call<Void>

    @PUT("user/{username}/category/{category_name}/task/{task_id}")
    fun editTask(@Path("username") username: String,
                 @Path("category_name") categoryName: String,
                 @Path("task_id") taskId: String,
                 @Body task: Task,
                 @Header("Authorization") jwtToken: String): Call<Void>

    @DELETE("user/{username}/category/{category_name}/task/{task_id}")
    fun deleteTask(@Path("username") username: String,
                   @Path("category_name") categoryName: String,
                   @Path("task_id") taskId: String,
                   @Header("Authorization") jwtToken: String): Call<Void>
}

