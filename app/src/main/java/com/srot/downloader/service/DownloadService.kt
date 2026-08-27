package com.srot.downloader.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.srot.downloader.R
import com.srot.downloader.SrotApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

class DownloadService : Service() {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val jobs = ConcurrentHashMap<String, Job>()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val id = intent.getStringExtra(EXTRA_JOB_ID) ?: return START_NOT_STICKY
                val title = intent.getStringExtra(EXTRA_TITLE) ?: "Srot"
                val argv = intent.getStringArrayListExtra(EXTRA_ARGV) ?: return START_NOT_STICKY
                startForeground(NOTIFICATION_ID, buildNotification(title, "Starting…"))
                runJob(id, title, argv)
            }
            ACTION_CANCEL -> {
                val id = intent.getStringExtra(EXTRA_JOB_ID) ?: return START_NOT_STICKY
                jobs[id]?.cancel()
                jobs.remove(id)
            }
        }
        return START_NOT_STICKY
    }

    private fun runJob(id: String, title: String, argv: ArrayList<String>) {
        val app = application as SrotApp
        val j = scope.launch {
            val result = app.engine.run(argv) { line ->
                val progress = parseProgress(line)
                updateNotification(title, line.take(80), progress)
            }
            result.onSuccess {
                updateNotification(title, "Completed", 100)
            }.onFailure {
                updateNotification(title, it.message ?: "Failed", 0)
            }
            jobs.remove(id)
            if (jobs.isEmpty()) stopForeground(STOP_FOREGROUND_REMOVE)
        }
        jobs[id] = j
    }

    private fun parseProgress(line: String): Int {
        val m = Regex("""(\d{1,3}(?:\.\d+)?)%""").find(line) ?: return -1
        return m.groupValues[1].toFloatOrNull()?.toInt()?.coerceIn(0, 100) ?: -1
    }

    private fun buildNotification(title: String, text: String, progress: Int = -1): Notification {
        val b = NotificationCompat.Builder(this, SrotApp.CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
        if (progress in 0..100) {
            b.setProgress(100, progress, false)
        } else {
            b.setProgress(0, 0, true)
        }
        return b.build()
    }

    private fun updateNotification(title: String, text: String, progress: Int) {
        val nm = getSystemService(NOTIFICATION_SERVICE) as android.app.NotificationManager
        nm.notify(NOTIFICATION_ID, buildNotification(title, text, progress))
    }

    companion object {
        const val ACTION_START = "com.srot.downloader.START"
        const val ACTION_CANCEL = "com.srot.downloader.CANCEL"
        const val EXTRA_JOB_ID = "job_id"
        const val EXTRA_ARGV = "argv"
        const val EXTRA_TITLE = "title"
        private const val NOTIFICATION_ID = 42
    }
}
