package I8E2S4.mobileTaskManager.model

import java.time.LocalDateTime

class User(var username: String?, var password: String?){
    var id: Int? = null
    override fun toString(): String{
        return username?:""
    }
}

class Category{
    var id: Long? = null
    var name: String? = null
    var description: String? = null
    var user: String? = null
    override fun toString(): String{
        return name?:""
    }

}

open class Task{
    var id: Long? = null
    var name: String? = null
    var description: String? = null
    //var startDateTime: LocalDateTime? = null
    //var endDateTime: LocalDateTime? = null
    var startDateTime: String? = null
    var endDateTime: String? = null
    var active: Boolean? = null
    var notification: Boolean? = null
    override fun toString(): String{
        return name?:""
    }
}

class UserEditPassword(var oldPassword: String, var newPassword: String)