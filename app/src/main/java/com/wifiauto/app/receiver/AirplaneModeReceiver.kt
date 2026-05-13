package com.wifiauto.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.wifiauto.app.WiFiAutoApplication

class AirplaneModeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_AIRPLANE_MODE_CHANGED) {
            val ativado = intent.getBooleanExtra("state", false)
            val app = context.applicationContext as WiFiAutoApplication
            if (ativado) {
                app.connectivityStateMachine.modoAviaoAtivado()
            } else {
                app.connectivityStateMachine.modoAviaoDesativado()
            }
        }
    }

    companion object {
        fun register(context: Context) {
            val filter = IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            context.registerReceiver(AirplaneModeReceiver(), filter)
        }
    }
}
