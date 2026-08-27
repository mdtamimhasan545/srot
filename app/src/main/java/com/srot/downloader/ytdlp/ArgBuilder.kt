package com.srot.downloader.ytdlp

import com.srot.downloader.data.AppSettings
import com.srot.downloader.data.Preset

/** Builds argv for yt-dlp as a structured list — never passed through a shell. */
object ArgBuilder {
    fun build(
        binary: String,
        url: String,
        preset: Preset,
        settings: AppSettings,
        outputTemplate: String = "%(title)s [%(id)s].%(ext)s"
    ): List<String> {
        val args = mutableListOf(binary)
        args += listOf("-f", preset.format)
        if (preset == Preset.AUDIO) {
            args += listOf("-x", "--audio-format", "m4a")
        } else {
            args += listOf("--merge-output-format", "mp4")
        }
        val outDir = settings.outputDir.ifBlank { null }
        val out = if (outDir != null) "$outDir/$outputTemplate" else outputTemplate
        args += listOf("-o", out)
        args += listOf("--no-overwrites", "--continue", "--windows-filenames")
        args += listOf("--embed-metadata", "--embed-chapters", "--write-thumbnail")
        args += listOf("--no-playlist", "-i")
        if (settings.ffmpegPath.isNotBlank()) {
            args += listOf("--ffmpeg-location", settings.ffmpegPath)
        }
        args += listOf("--newline", "--no-colors")
        args += listOf("--", url)
        return args
    }
}
