package i8e2s4.mobileTaskManager.logic.calendarAdapter

data class CalendarItem (val id: Long,
                         val textName: String,
                         val textDescription: String,
                         val textCategory: String,
                         val textStartDateTime: String,
                         val textEndDateTime: String,
                         val booleanActive: Boolean?,
                         val booleanNotification: Boolean?);