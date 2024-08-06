package net.natsucamellia.lifeclock.ui.model

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import net.natsucamellia.lifeclock.services.TimerService
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import kotlin.math.max

class LifeClockModel : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var service: TimerService? = null
    var birthdayEpoch by mutableLongStateOf(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli())
    var age by mutableIntStateOf(0)
    var remainMillis by mutableLongStateOf(0L)

    fun updateBirthdayEpoch(epoch: Long) {
        this.birthdayEpoch = epoch
    }

    fun updateAge(age: Int) {
        this.age = age
    }

    fun updateClock() {
        // Pretend timezone is UTC
        val birthday = LocalDateTime.ofInstant(Instant.ofEpochMilli(birthdayEpoch), ZoneId.of("Z"))
        val deathDay = birthday.plusYears(age.toLong())
        val now = LocalDateTime.now()
        remainMillis = max(deathDay.toInstant(ZoneOffset.UTC).toEpochMilli() - now.toInstant(ZoneOffset.UTC).toEpochMilli(), 0)

        service?.setTimer {
            if (remainMillis > 0)
                remainMillis = max(remainMillis - it, 0)
        }
    }

    fun updateService(service: TimerService?) {
        this.service = service
    }
}