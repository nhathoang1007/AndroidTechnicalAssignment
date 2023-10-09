package com.example.jason.schedule

import android.Manifest
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.jason.MainActivity
import com.example.jason.MyApplication
import com.example.jason.R
import kotlin.random.Random

private const val CHANNEL_ID = "_match_channel_id"
private const val importance = NotificationManagerCompat.IMPORTANCE_DEFAULT

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null) return

        val data = intent?.getMatchExtra() ?: return

        val newIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putMatchExtra(data)
        }

        if ((context.applicationContext as MyApplication).isAppIsRunning) {
            context.startActivity(newIntent)
        } else {

            val mChannel = NotificationChannelCompat.Builder(CHANNEL_ID, importance).apply {
                setName("channel name") // Must set! Don't remove
                setDescription("channel description")
                setLightsEnabled(true)
                setLightColor(Color.RED)
                setVibrationEnabled(true)
                setVibrationPattern(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
            }.build()

            NotificationManagerCompat.from(context).createNotificationChannel(mChannel)
            val notification: Notification = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_match)
                .setContentTitle("Match Started!")
                .setContentText(data.second)
                .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
            }
            NotificationManagerCompat.from(context).notify(Random.nextInt(), notification)
        }
    }
}