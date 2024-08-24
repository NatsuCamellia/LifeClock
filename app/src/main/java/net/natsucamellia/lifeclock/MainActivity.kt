package net.natsucamellia.lifeclock

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import net.natsucamellia.lifeclock.services.TimerService
import net.natsucamellia.lifeclock.ui.model.LifeClockModel
import net.natsucamellia.lifeclock.ui.nav.NavContainer
import net.natsucamellia.lifeclock.ui.theme.LifeClockTheme

class MainActivity : ComponentActivity() {
    val lifeClockModel by viewModels<LifeClockModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        startTimerService()
        setContent {
            LifeClockTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize()) {
                    NavContainer(lifeClockModel = lifeClockModel)
                }
            }
        }
    }

    private fun startTimerService() {
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                lifeClockModel.updateService((binder as TimerService.LocalBinder).getService())
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                lifeClockModel.updateService(null)
            }
        }

        val intent = Intent(applicationContext, TimerService::class.java)
        applicationContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
}