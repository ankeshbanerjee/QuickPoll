package com.example.quickpoll.service

import android.Manifest
import android.app.Notification.Action
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.example.quickpoll.MainActivity
import com.example.quickpoll.R
import com.example.quickpoll.app.BaseApplication.Companion.CHANNEL_ID
import com.example.quickpoll.data.network.utils.Resource
import com.example.quickpoll.data.repository.UserRepository
import com.example.quickpoll.utils.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FcmService : FirebaseMessagingService() {

    @Inject
    lateinit var userRepository: UserRepository

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        coroutineScope.launch {
            userRepository.saveFcmToken(
                token = token
            ).collect {
                when (it) {
                    is Resource.Success -> {
                        // Do something
                        Log.d("SAVE_FCM_SUCCESS", it.data.toString())
                    }

                    is Resource.Error -> {
                        // Do something
                        Log.e("SAVE_FCM_ERROR", it.message.toString())
                    }

                    is Resource.Loading -> {
                        // Do something
                    }
                }

            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data.isNotEmpty()) {
            val data = message.data
            val pollId = data["pollId"]
            Log.d("FCM_MESSAGE", "$pollId")
            val deeplinkIntent = Intent(
                Intent.ACTION_VIEW,
                "https://${Constants.DEEP_LINK_DOMAIN}/poll/$pollId".toUri(),
                this,
                MainActivity::class.java
            )
            val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(deeplinkIntent)
                getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE)
            }
            val builder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(data["title"])
                .setContentText(data["body"])
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(deepLinkPendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(this)) {
                if (ActivityCompat.checkSelfPermission(
                        applicationContext,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return@with
                }
                // notificationId is a unique int for each notification that you must define.
                notify(Math.random().toInt(), builder.build())
            }
        }
    }
}