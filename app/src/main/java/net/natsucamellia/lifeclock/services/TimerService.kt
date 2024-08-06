package net.natsucamellia.lifeclock.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import java.util.Timer
import java.util.TimerTask

class TimerService : Service() {
    private val binder = LocalBinder()
    private val timer = Timer()
    private val handler = Handler(Looper.getMainLooper())
    private var onChange: ((Long) -> Unit)? = null

    private val updateDelay = 10L

    override fun onCreate() {
        super.onCreate()
        timer.schedule(
            object : TimerTask() {
                override fun run() {
                    handler.post(this@TimerService::updateState)
                }
            },
            0,
            updateDelay
        )
        Log.d("Timer", "Service Created")
    }

    override fun onDestroy() {
        timer.cancel()
        stopSelf()

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder {
        Log.d("Timer", "Service Bound")
        return binder
    }

    fun setTimer(onChange: (Long) -> Unit) {
        this.onChange = onChange
    }

    private fun updateState() {
        onChange?.let { it(updateDelay) }
    }

    inner class LocalBinder : Binder() {
        fun getService() = this@TimerService
    }
}