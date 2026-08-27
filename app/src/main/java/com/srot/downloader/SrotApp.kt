package com.srot.downloader

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.srot.downloader.data.SettingsRepository
import com.srot.downloader.ytdlp.YtDlpEngine

class SrotApp : Application() {
    lateinit var settings: SettingsRepository
        private set
    lateinit var engine: YtDlpEngine
        private set

    override fun onCreate() {
        super.onCreate()
        settings = SettingsRepository(this)
        engine = YtDlpEngine(this)
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel),
                NotificationManager.IMPORTANCE_LOW
            )
            val nm = getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "srot_downloads"
    }
}
