// App-level initialization of the Notification Channel
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Build
import android.content.Context
import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.NotificationCompat
import com.example.calorieapp.CalorieActivity
import com.example.calorieapp.R

//const val CHANNEL_ID = "ch-1"
//const val CHANNEL_NAME = "meal_reminder_channel"
//const val NOTIFICATION_ID = 1001
//const val REQUEST_CODE = 1002

//class NotificationService(
//    private val context: Context
//){
//    private val notificationManager =
//        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//    @OptIn(ExperimentalMaterial3Api::class)
//    private val myIntent = Intent(context, CalorieActivity::class.java)
//    private val pendingIntent = PendingIntent.getActivity(
//        context,
//        REQUEST_CODE,
//        myIntent,
//        PendingIntent.FLAG_IMMUTABLE
//    )
//
//    fun showNotification() {
//        android.util.Log.d("NotificationDebug", "showNotification called") // Debug line
//        val notification =
//            NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_launcher_foreground)
//                .setContentTitle("Meal Reminder")
//                .setContentText("Don't forget to log your meals!")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setContentIntent(pendingIntent)
//                .build()
//
//        notificationManager.notify(NOTIFICATION_ID, notification)
//    }
//
//}


//@SuppressLint("ObsoleteSdkInt")
//fun createNotificationChannel(context: Context) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        android.util.Log.d("NotificationDebug", "createNotificationChannel called") // Debug line
//        val name = "Meal Reminder"
//        val description = "Reminders to log your meals in the app."
//        val importance = NotificationManager.IMPORTANCE_DEFAULT
//        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//            this.description = description
//        }
//
//        // Register the channel with the system
//        val notificationManager: NotificationManager =
//            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(channel)
//    }
//}
