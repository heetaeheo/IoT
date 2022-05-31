package com.example.iotpush

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.nfc.Tag
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.google.android.gms.common.api.internal.RegisterListenerMethod
import com.google.firebase.messaging.EnhancedIntentService
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val channelId = "notification_channel"
const val channelName = "com.example.iotpush"
class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "FirebaseMessagingServiceUtil.calss"


    // 사용자 지정 레이아웃에 설정


    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "onNewToken : ${token}")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        if(remoteMessage.notification != null){
           generateNotification(remoteMessage.notification!!.title!!,remoteMessage.notification!!.body!!)
        } else{
            Log.d(TAG,"error")
        }
    }


    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title : String, message: String) : RemoteViews{
        val remoteView = RemoteViews("com.example.iotpush",R.layout.notification)

        remoteView.setTextViewText(R.id.title,title)
        remoteView.setTextViewText(R.id.message,message)
        remoteView.setImageViewResource(R.id.logo,R.drawable.ic_launcher_foreground)

        return remoteView
    }




    //알림을 생성
    fun generateNotification(title:String, message:String){
        var intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        //channel id. channel name

        val channelId = "Channel ID"
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        var builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        //builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)

        }

        notificationManager.notify(0,builder.build())

    }


}




