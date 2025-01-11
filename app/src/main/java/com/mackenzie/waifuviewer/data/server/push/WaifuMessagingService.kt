package com.mackenzie.waifuviewer.data.server.push

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mackenzie.waifuviewer.R
import com.mackenzie.waifuviewer.data.FavoritesRepository
import com.mackenzie.waifuviewer.data.PushRepository
import com.mackenzie.waifuviewer.data.db.dao.WaifuFcmTokenDao
import com.mackenzie.waifuviewer.data.server.mapper.toDomainModel
import com.mackenzie.waifuviewer.data.server.models.RemoteConnect
import com.mackenzie.waifuviewer.domain.Notification
import com.mackenzie.waifuviewer.domain.NotificationType
import com.mackenzie.waifuviewer.ui.NavHostActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WaifuMessagingService : FirebaseMessagingService() {

    private val channelId = "notification_channel"
    private val channelName = "com.mackenzie.waifuviewer"

    @Inject
    lateinit var notificationRepository: PushRepository


    /**
     * Se invoca cuando se recibe un nuevo token de registro.
     * Este token es único y puede utilizarse para enviar notificaciones dirigidas a este dispositivo.
     *
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Aquí puedes guardar el token localmente, enviarlo a tu servidor, etc.
        Log.e("WaifuMessagingService", "Nuevo token: $token")
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.

        Log.e("WaifuMessagingService", "FcmToken: ${token.toDomainModel()}")
        CoroutineScope(Dispatchers.IO).launch {
            saveToken(token)
        }

        // TODO guardar el token en servidor
        // sendRegistrationToServer(token)
    }

    private suspend fun saveToken(token: String) {
        // val error = repo.saveToken(token.toDomainModel())
        val error = notificationRepository.saveToken(token.toDomainModel())
        error?.let {
            Log.e("WaifuMessagingService", "Error al guardar el token: $error")
        } ?: Log.e("WaifuMessagingService", "Token guardado con éxito")
    }


    /**
     * Se invoca cada vez que se recibe un mensaje de FCM.
     * RemoteMessage contiene la data y/o la notificación asociada.
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.e("WaifuMessagingService", "Notification:Data ${message.data}")
        Log.e("WaifuMessagingService", "Notification title=${message.notification?.title}, body=${message.notification?.body}")


        if (message.data.isNotEmpty()) {
            var textReferences: Pair<Int, Int>? = null
            var notificationTitle: Int? = null



            val notification = createCustomNotification(
                NotificationType.UPDATES, message.notification?.title ?: "", message.notification?.body ?: ""
            )

            // TODO guardar la notificación en room
            CoroutineScope(Dispatchers.IO).launch {
                // notificationRepository.savePush(notification)
            }

            generateNotification(notification, notification.pushId)

            Log.d("WaifuMessagingService", "Data recibida: ${message.data}")
        } else if (message.notification != null) {
            val notification = createCustomNotification(
                NotificationType.NEWS, message.notification?.title ?: "", message.notification?.body ?: ""
            )
            // TODO guardar la notificación en room
            // notificationRepository.savePush(notification)
            generateNotification(notification, notification.pushId)
            Log.d("WaifuMessagingService", "Notificación recibida: title=${message.notification?.title}, Body=${message.notification?.body}")
        }

        // Si el mensaje contiene datos (remoteMessage.data), puedes procesarlos aquí:
        message.data.let { data ->
            // Maneja la lógica de los datos aquí

        }

        // Si el mensaje contiene una notificación (remoteMessage.notification), también puedes procesarla:
        message.notification?.let { notification ->
            // Log.d("WaifuMessagingService", "Notificación recibida: ${notification.body}")
            // Por ejemplo, mostrar una notificación en la barra de estado
            // showNotification(notification.title, notification.body)
        }
    }

    private inline fun <T1 : Any, T2 : Any, R : Any> checkNotNulls(
        p1: T1?, p2: T2?, block: (T1, T2) -> R?
    ): R? {
        return if (p1 != null && p2 != null) block(p1, p2) else null
    }

    private fun generateNotification(
        notification: Notification,
        notificationId: String,
        // loginRequired: Boolean,
        pendingActivity: Activity? = null
    ) {
        val smallIcon = R.mipmap.ic_launcher

        /** Go to Notifications Activity by default */
        /*val activity2 = pendingActivity ?: AppComponents.assembly?.getFeatures()?.let { features ->
            features.firstOrNull { it is IFeatureNotifications }?.let { fragment ->
                (fragment as IFeatureNotifications).notificationsActivity
            }
        }*/



        val activity = NavHostActivity::class.java

        activity.let {
            // Aqui se guarda el id de la push en las preferencias
            /*applicationContext.putPreferenceString(
                hashMapOf(
                    Pair(PreferencesKey.NOTIFICATIONID.value, notificationId)
                )
            )*/

            val intent = Intent(this, activity::class.java).apply {
                // putExtra(NOTIFICATION_TOOLBAR, false)
                // putExtra(NOTIFICATION_LOGIN_REQUIRED, loginRequired)
            }
            val pendingIntent = PendingIntent.getActivity(
                this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
            )

            val builder = NotificationCompat.Builder(applicationContext, channelId)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                builder.setUpBuilder(
                    smallIcon, notification.title, notification.description, pendingIntent
                )
            } else {
                builder.setUpBuilderWithBackground(
                    smallIcon, notification.title, resources, notification.description, pendingIntent
                )
            }

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel =
                    NotificationChannel(channelId, channelName, IMPORTANCE_HIGH)
                notificationManager.createNotificationChannel(notificationChannel)
            }
            notificationManager.notify((0..10000).random(), builder.build())
        }
    }

    /**
     * Se invoca cuando se han eliminado mensajes de FCM en el servidor
     * debido a que el dispositivo ha estado inactivo o el servidor no ha podido entregarlos.
     */
    override fun onDeletedMessages() {
        super.onDeletedMessages()
        Log.d("WaifuMessagingService", "Se han eliminado algunos mensajes en el servidor.")
        // Por ejemplo, podrías notificar a tu servidor para volver a sincronizar datos importantes.
    }

    /**
     * Se invoca cuando un mensaje enviado al servidor FCM se ha enviado con éxito.
     * Suele usarse para confirmar que un upstream message realmente llegó.
     */
    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)
        Log.d("WaifuMessagingService", "Mensaje enviado con éxito. ID: $msgId")
    }

    /**
     * Se invoca cuando ocurre un error al enviar un mensaje upstream.
     */
    override fun onSendError(msgId: String, exception: Exception) {
        super.onSendError(msgId, exception)
        Log.e("WaifuMessagingService", "Error al enviar mensaje: $msgId. Excepción: $exception")
    }

}