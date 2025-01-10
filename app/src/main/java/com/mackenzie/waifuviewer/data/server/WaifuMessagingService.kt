package com.mackenzie.waifuviewer.data.server

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class WaifuMessagingService : FirebaseMessagingService() {


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
        Log.d("MyFirebaseMsgService", "Nuevo token: $token")

        // TOKEN EJemplo
        // Nuevo token: f6oEL7bRSP66hbN5W9Nvsx:APA91bHYqteimxS_5bH6jdEbQhOIyc2T51B1D-tt-KvQz9I4BhwDClXpWvPZ_znTKr-1B0oAhsQnF0MtobfmLsXOSadVUHXkNOZBVoUNr8POj-enkFbp3cc

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        // TODO guardar el token en room
        // sendRegistrationToServer(token)
    }

    /**
     * Se invoca cada vez que se recibe un mensaje de FCM.
     * RemoteMessage contiene la data y/o la notificación asociada.
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        // Si el mensaje contiene datos (remoteMessage.data), puedes procesarlos aquí:
        remoteMessage.data.let { data ->
            Log.d("MyFirebaseMsgService", "Data recibida: $data")
            // Maneja la lógica de los datos aquí
        }

        // Si el mensaje contiene una notificación (remoteMessage.notification), también puedes procesarla:
        remoteMessage.notification?.let { notification ->
            Log.d("MyFirebaseMsgService", "Notificación recibida: ${notification.body}")
            // Por ejemplo, mostrar una notificación en la barra de estado
            // showNotification(notification.title, notification.body)
        }
    }

    /**
     * Se invoca cuando se han eliminado mensajes de FCM en el servidor
     * debido a que el dispositivo ha estado inactivo o el servidor no ha podido entregarlos.
     */
    override fun onDeletedMessages() {
        super.onDeletedMessages()
        Log.d("MyFirebaseMsgService", "Se han eliminado algunos mensajes en el servidor.")
        // Por ejemplo, podrías notificar a tu servidor para volver a sincronizar datos importantes.
    }

    /**
     * Se invoca cuando un mensaje enviado al servidor FCM se ha enviado con éxito.
     * Suele usarse para confirmar que un upstream message realmente llegó.
     */
    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)
        Log.d("MyFirebaseMsgService", "Mensaje enviado con éxito. ID: $msgId")
    }

    /**
     * Se invoca cuando ocurre un error al enviar un mensaje upstream.
     */
    override fun onSendError(msgId: String, exception: Exception) {
        super.onSendError(msgId, exception)
        Log.e("MyFirebaseMsgService", "Error al enviar mensaje: $msgId. Excepción: $exception")
    }

}