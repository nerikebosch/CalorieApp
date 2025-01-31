//import android.annotation.SuppressLint
//import android.app.PendingIntent
//import android.content.Context
//import android.content.Intent
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import com.example.calorieapp.CalorieActivity
//import com.example.calorieapp.R
//
//@OptIn(ExperimentalMaterial3Api::class)
//@SuppressLint("MissingPermission")
//fun showNotification(context: Context) {
//    android.util.Log.d("NotificationDebug", "showNotification called") // Debug line
//    val intent = Intent(context, CalorieActivity::class.java) // Open the app when tapped
//    val pendingIntent = PendingIntent.getActivity(
//        context,
//        0,
//        intent,
//        PendingIntent.FLAG_IMMUTABLE
//    )
//
//    val builder = NotificationCompat.Builder(context, CHANNEL_ID)
//        .setSmallIcon(R.drawable.notification_icon) // Replace with your icon
//        .setContentTitle("Don't forget to log your meals!")
//        .setContentText("Log your breakfast, lunch, or dinner for today.")
//        .setPriority(NotificationCompat.PRIORITY_HIGH)
//        .setContentIntent(pendingIntent)
//        .setAutoCancel(true) // Dismiss notification when tapped
//
//    with(NotificationManagerCompat.from(context)) {
//        notify(1001, builder.build()) // Notification ID can be any unique int
//    }
//}
