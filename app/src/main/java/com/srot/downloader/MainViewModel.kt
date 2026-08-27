package com.srot.downloader

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.srot.downloader.data.AppSettings
import com.srot.downloader.data.DownloadJob
import com.srot.downloader.data.JobStatus
import com.srot.downloader.data.MediaInfo
import com.srot.downloader.data.Preset
import com.srot.downloader.service.DownloadService
import com.srot.downloader.ytdlp.ArgBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class UiState(
    val urlInput: String = "",
    val media: MediaInfo? = null,
    val preset: Preset = Preset.BEST,
    val analyzing: Boolean = false,
    val message: String? = null,
    val jobs: List<DownloadJob> = emptyList(),
    val settings: AppSettings = AppSettings(),
    val binaryReady: Boolean = false
)

class MainViewModel(private val app: SrotApp) : ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            app.settings.settings.collect { s ->
                val bin = app.engine.resolveBinary(s.ytdlpPath)
                _state.update { it.copy(settings = s, binaryReady = bin != null) }
            }
        }
    }

    fun setUrl(url: String) = _state.update { it.copy(urlInput = url) }
    fun setPreset(p: Preset) = _state.update { it.copy(preset = p) }
    fun clearMessage() = _state.update { it.copy(message = null) }

    fun analyze() {
        val url = _state.value.urlInput.trim()
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            _state.update { it.copy(message = app.getString(R.string.invalid_url)) }
            return
        }
        viewModelScope.launch {
            _state.update { it.copy(analyzing = true, message = null) }
            val result = app.engine.analyze(url)
            result.onSuccess { r ->
                _state.update {
                    it.copy(
                        analyzing = false,
                        media = MediaInfo(r.url, r.title, r.uploader, r.thumbnail, r.provider)
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(analyzing = false, message = e.message ?: app.getString(R.string.invalid_url))
                }
            }
        }
    }

    fun enqueueDownload() {
        val st = _state.value
        val media = st.media ?: return
        val binary = app.engine.resolveBinary(st.settings.ytdlpPath)
        if (binary == null) {
            _state.update { it.copy(message = app.getString(R.string.engine_missing)) }
            return
        }
        val argv = ArgBuilder.build(binary.absolutePath, media.url, st.preset, st.settings)
        val job = DownloadJob(
            id = UUID.randomUUID().toString(),
            url = media.url,
            title = media.title,
            thumbnail = media.thumbnail,
            preset = st.preset,
            argv = argv
        )
        _state.update {
            it.copy(jobs = listOf(job) + it.jobs, message = app.getString(R.string.added_queue))
        }
        val intent = Intent(app, DownloadService::class.java).apply {
            action = DownloadService.ACTION_START
            putExtra(DownloadService.EXTRA_JOB_ID, job.id)
            putStringArrayListExtra(DownloadService.EXTRA_ARGV, ArrayList(argv))
            putExtra(DownloadService.EXTRA_TITLE, job.title)
        }
        app.startForegroundService(intent)
    }

    fun updateJob(id: String, transform: (DownloadJob) -> DownloadJob) {
        _state.update { s ->
            s.copy(jobs = s.jobs.map { if (it.id == id) transform(it) else it })
        }
    }

    fun cancelJob(id: String) {
        updateJob(id) { it.copy(status = JobStatus.CANCELLED) }
        val intent = Intent(app, DownloadService::class.java).apply {
            action = DownloadService.ACTION_CANCEL
            putExtra(DownloadService.EXTRA_JOB_ID, id)
        }
        app.startService(intent)
    }

    fun updateSettings(transform: (AppSettings) -> AppSettings) {
        viewModelScope.launch { app.settings.update(transform) }
    }

    fun fetchBinary() {
        viewModelScope.launch {
            _state.update { it.copy(message = "Downloading yt-dlp…") }
            val result = app.engine.installOfficialBinary()
            result.onSuccess { file ->
                app.settings.update { it.copy(ytdlpPath = file.absolutePath) }
                _state.update { it.copy(message = "yt-dlp installed", binaryReady = true) }
            }.onFailure { e ->
                _state.update { it.copy(message = e.message ?: "Install failed") }
            }
        }
    }

    companion object {
        fun factory(app: SrotApp) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return MainViewModel(app) as T
            }
        }
    }
}
