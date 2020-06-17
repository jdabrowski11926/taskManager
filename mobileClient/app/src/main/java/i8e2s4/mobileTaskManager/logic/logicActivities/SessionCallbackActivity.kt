package i8e2s4.mobileTaskManager.logic.logicActivities

abstract class SessionCallbackActivity: CallbackActivity(){
    protected  val username: String by lazy {intent.getStringExtra("username")?:""}
    protected  val jwtToken: String by lazy {intent.getStringExtra("jwt_token")?:""}
    protected  val address: String by lazy {intent.getStringExtra("address")?:""}
}