package com.wifiauto.app.connectivity

import android.content.Context
import com.wifiauto.app.WiFiAutoApplication
import com.wifiauto.app.data.AppDatabase
import com.wifiauto.app.data.PlaceEntity
import com.wifiauto.app.service.AlertSonoroManager
import com.wifiauto.app.service.MobileDataService
import com.wifiauto.app.service.WifiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

enum class Estado {
    MODO_RUA,
    EM_AREA_SALVA,
    MODO_AVIAO
}

class ConnectivityStateMachine(private val context: Context) {

    var estadoAtual: Estado = Estado.MODO_RUA
        private set
    private var lugarAtual: PlaceEntity? = null
    private var modoOperacao: String = "GEOFENCE"

    private val wifiService = WifiService()
    private val mobileDataService = MobileDataService()
    private val alertSonoro: AlertSonoroManager
        get() = (context.applicationContext as WiFiAutoApplication).alertSonoroManager

    fun setModoOperacao(modo: String) {
        modoOperacao = modo
    }

    fun entrouEmAreaSalva(requestId: String?) {
        if (estadoAtual == Estado.MODO_AVIAO) return

        CoroutineScope(Dispatchers.IO).launch {
            val place = AppDatabase.getDatabase(context).placeDao().getById(requestId?.toLongOrNull() ?: return@launch)
            if (place != null) {
                lugarAtual = place
                estadoAtual = Estado.EM_AREA_SALVA

                // Desligar dados móveis (ação automática)
                mobileDataService.disable(context)

                // Sugerir ligar Wi-Fi (card do sistema)
                wifiService.enable(context)

                // Alerta sonoro frenético
                alertSonoro.iniciarAlerta("CHEGADA")
            }
        }
    }

    fun saiuDeAreaSalva() {
        if (estadoAtual == Estado.MODO_AVIAO) return

        lugarAtual = null
        estadoAtual = Estado.MODO_RUA

        // Ligar dados móveis (ação automática)
        mobileDataService.enable(context)

        // Sugerir desligar Wi-Fi (card do sistema)
        wifiService.disable(context)

        // Alerta sonoro frenético
        alertSonoro.iniciarAlerta("SAIDA")
    }

    fun modoAviaoAtivado() {
        estadoAtual = Estado.MODO_AVIAO
        alertSonoro.pararAlerta()
    }

    fun modoAviaoDesativado() {
        // Reavaliar estado com base na última localização conhecida
        estadoAtual = Estado.MODO_RUA
    }

    fun sugerirNovoLugar(ssid: String, latitude: Double, longitude: Double) {
        // Criar notificação de sugestão
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager
        val channelId = "aprendizagem"

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                channelId,
                "Sugestões de Lugares",
                android.app.NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val pendingIntent = android.app.PendingIntent.getActivity(
            context, 0, intent,
            android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val notification = android.app.Notification.Builder(context, channelId)
            .setContentTitle("WiFi Auto - Novo Lugar Detectado")
            .setContentText("Conectou-se a $ssid várias vezes aqui. Quer salvar este lugar?")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(1001, notification)
    }
}
