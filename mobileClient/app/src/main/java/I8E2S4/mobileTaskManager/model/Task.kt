package I8E2S4.mobileTaskManager.model

class Task{
    var id: Long? = null
    var name: String? = null
    var description: String? = null
    var startDateTime: String? = null
    var endDateTime: String? = null
    var active: Boolean? = null
    var notification: Boolean? = null
    override fun toString(): String{
        return name?:""
    }
}