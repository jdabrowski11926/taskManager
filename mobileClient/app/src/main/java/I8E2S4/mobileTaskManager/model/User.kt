package I8E2S4.mobileTaskManager.model

class User(var username: String?, var password: String?){
    var id: Int? = null
    override fun toString(): String{
        return username?:""
    }
}