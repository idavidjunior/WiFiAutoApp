package com.wifiauto.app.service

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.wifiauto.app.R
import com.wifiauto.app.utils.Constants

class AlertSonoroManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var repeticoes = 0
    private val maxRepeticoesSemResposta = 12

    fun iniciarAlerta(tipo: String) {
        val prefs = context.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE)
        val ativado = when (tipo) {
            "CHEGADA" -> prefs.getBoolean(Constants.KEY_ALERTA_CHEGADA, true)
            "SAIDA" -> prefs.getBoolean(Constants.KEY_ALERTA_SAIDA, true)
            "RUA" -> prefs.getBoolean(Constants.KEY_ALERTA_RUA, false)
            else -> false
        }

        if (!ativado) return

        val volumePercent = prefs.getInt(Constants.KEY_VOLUME_ALERTA, 80) / 100f

        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, R.raw.alerta_frenetico).apply {
                isLooping = false
                setVolume(volumePercent, volumePercent)
                start()
            }
            repeticoes = 0
            handler.postDelayed(repetirAlerta, 5000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val repetirAlerta = object : Runnable {
        override fun run() {
            if (repeticoes >= maxRepeticoesSemResposta) {
                pararAlerta()
                return
            }
            repeticoes++
            mediaPlayer?.start()
            handler.postDelayed(this, 5000)
        }
    }

    fun pararAlerta() {
        handler.removeCallbacks(repetirAlerta)
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        repeticoes = 0
    }
}
