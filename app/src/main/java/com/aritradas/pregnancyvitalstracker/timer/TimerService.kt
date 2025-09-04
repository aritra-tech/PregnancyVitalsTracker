package com.aritradas.pregnancyvitalstracker.timer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.aritradas.pregnancyvitalstracker.MainActivity
import com.aritradas.pregnancyvitalstracker.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TimerService : Service() {

    companion object {
        const val ACTION_START_TIMER = "START_TIMER"
        const val ACTION_STOP_TIMER = "STOP_TIMER"
        const val ACTION_TIME_UPDATE = "com.aritradas.pregnancyvitalstracker.TIME_UPDATE"
        const val EXTRA_TIME = "EXTRA_TIME"

        private const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "TIMER_SERVICE_CHANNEL"
        private const val CHANNEL_NAME = "Timer Service"
    }

    // Binder for local service binding
    private val binder = TimerBinder()

    // Coroutine scope for the service
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // Timer state management using StateFlow
    private val _currentTime = MutableStateFlow("00:00:00")
    val currentTime: StateFlow<String> = _currentTime.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning.asStateFlow()

    private var timerJob: Job? = null
    private var elapsedSeconds = 0

    // Binder class for local binding
    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_TIMER -> startTimer()
            ACTION_STOP_TIMER -> stopTimer()
        }

        // Return START_STICKY to ensure service is restarted if killed
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder = binder

    private fun startTimer() {
        if (_isRunning.value) return

        _isRunning.value = true

        // Start as foreground service with notification
        startForeground(NOTIFICATION_ID, createNotification())

        // Start the timer coroutine on background thread
        timerJob = serviceScope.launch {
            while (_isRunning.value) {
                val formattedTime = formatElapsedTime(elapsedSeconds)
                _currentTime.value = formattedTime

                // Send broadcast for activities that aren't bound
                sendTimeBroadcast(formattedTime)

                // Wait 1 second and increment
                delay(1000)
                elapsedSeconds++
            }
        }
    }

    private fun stopTimer() {
        _isRunning.value = false
        timerJob?.cancel()
        timerJob = null
        elapsedSeconds = 0 // Reset timer when stopped
        _currentTime.value = "00:00:00"

        // Stop foreground service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            @Suppress("DEPRECATION")
            stopForeground(true)
        }
        stopSelf()
    }

    private fun formatElapsedTime(totalSeconds: Int): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun sendTimeBroadcast(time: String) {
        val intent = Intent(ACTION_TIME_UPDATE).apply {
            putExtra(EXTRA_TIME, time)
            setPackage(packageName) // Restrict to our app
        }
        sendBroadcast(intent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows timer running in background"
                setShowBadge(false)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Timer Running")
            .setContentText("Elapsed time: ${_currentTime.value}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        serviceScope.cancel()
    }
}