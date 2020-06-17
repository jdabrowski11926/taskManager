package I8E2S4.mobileTaskManager.model

class Category{
    var id: Long? = null
    var name: String? = null
    var description: String? = null
    var user: String? = null
    override fun toString(): String{
        return name?:""
    }

}