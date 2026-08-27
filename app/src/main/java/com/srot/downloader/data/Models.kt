package com.srot.downloader.data

enum class Preset(val format: String) {
    BEST("bv*+ba/b"),
    UHD("bv*[height<=2160]+ba/b[height<=2160]"),
    P1080("bv*[height<=1080]+ba/b[height<=1080]"),
    P720("bv*[height<=720]+ba/b[height<=720]"),
    AUDIO("ba/b")
}

enum class JobStatus { QUEUED, RUNNING, PAUSED, COMPLETED, FAILED, CANCELLED }

data class MediaInfo(
    val url: String,
    val title: String,
    val uploader: String,
    val thumbnail: String?,
    val provider: String
)

data class DownloadJob(
    val id: String,
    val url: String,
    val title: String,
    val thumbnail: String?,
    val preset: Preset,
    val argv: List<String>,
    val status: JobStatus = JobStatus.QUEUED,
    val progress: Float = 0f,
    val speed: String = "",
    val eta: String = "",
    val error: String? = null,
    val outputPath: String? = null
)

data class AppSettings(
    val language: String = "en",
    val theme: String = "system",
    val wifiOnly: Boolean = false,
    val ytdlpPath: String = "",
    val ffmpegPath: String = "ffmpeg",
    val outputDir: String = "",
    val concurrent: Int = 1
)
