package com.wifiauto.app.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wifiauto.app.WiFiAutoApplication
import com.wifiauto.app.connectivity.ConnectivityStateMachine
import com.wifiauto.app.connectivity.Estado

class EstadoWorker(
    context: Context,
    params: WorkerParameters,
    private val connectivityStateMachine: ConnectivityStateMachine
) : Worker(context, params) {

    override fun doWork(): Result {
        // Se o serviço foi morto, notificar usuário
        if (connectivityStateMachine.estadoAtual == Estado.MODO_RUA) {
            val app = applicationContext as WiFiAutoApplication
            // Aqui poderia reavaliar a localização e corrigir o estado
        }
        return Result.success()
    }
}

class EstadoWorkerFactory(
    private val connectivityStateMachine: ConnectivityStateMachine
) : androidx.work.WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): Worker? {
        return if (workerClassName == EstadoWorker::class.java.name) {
            EstadoWorker(appContext, workerParameters, connectivityStateMachine)
        } else null
    }
}
