package com.example.jason.schedule

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.data.store.MyPrefImpl
import com.example.jason.ui.adapter.match.MatchModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val pref: MyPrefImpl,
) {
    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    fun startSchedule(activity: Activity, match: MatchModel, callback: () -> Unit) {
        val alertDialog = AlertDialog.Builder(activity)
        alertDialog.setTitle("Match Reminder")
        alertDialog.setMessage("Your match will be notified when it's stated!")
        alertDialog.setPositiveButton("Remind me") { dialog, _ ->
            setReminder(match)
            pref.saveReminder(match.unique)
            Toast.makeText(context, "Your match reminder was saved!", Toast.LENGTH_LONG).show()
            dialog.dismiss()
            callback.invoke()
        }
        alertDialog.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    private fun setReminder(match: MatchModel) {
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.putMatchExtra(Pair(match.time, match.description))
            PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
        }

        alarmMgr?.setExact(
            AlarmManager.RTC_WAKEUP,
            Calendar.getInstance().apply {
                add(Calendar.SECOND, 10)
            }.timeInMillis,
            alarmIntent
        )
    }
}