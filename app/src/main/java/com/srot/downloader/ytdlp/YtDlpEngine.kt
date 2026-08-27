package com.srot.downloader.ytdlp

import android.content.Context
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

class YtDlpEngine(private val context: Context) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    fun defaultBinaryPath(): File = File(context.filesDir, "bin/yt-dlp")

    fun resolveBinary(configured: String): File? {
        if (configured.isNotBlank()) {
            val f = File(configured)
            if (f.exists() && f.canExecute()) return f
        }
        val bundled = defaultBinaryPath()
        if (bundled.exists() && bundled.canExecute()) return bundled
        return null
    }

    /** Public metadata only (oEmbed). Does not extract stream URLs. */
    suspend fun analyze(url: String): Result<AnalyzeResult> = withContext(Dispatchers.IO) {
        try {
            val endpoints = listOf(
                "https://www.youtube.com/oembed?format=json&url=${java.net.URLEncoder.encode(url, "UTF-8")}",
                "https://vimeo.com/api/oembed.json?url=${java.net.URLEncoder.encode(url, "UTF-8")}"
            )
            for (ep in endpoints) {
                val req = Request.Builder().url(ep).header("User-Agent", "Srot/1.0").build()
                client.newCall(req).execute().use { res ->
                    if (!res.isSuccessful) return@use
                    val body = res.body?.string() ?: return@use
                    val j = JSONObject(body)
                    val title = j.optString("title").ifBlank { return@use }
                    return@withContext Result.success(
                        AnalyzeResult(
                            url = url,
                            title = title,
                            uploader = j.optString("author_name"),
                            thumbnail = j.optString("thumbnail_url").ifBlank { null },
                            provider = j.optString("provider_name")
                        )
                    )
                }
            }
            val host = try { java.net.URI(url).host ?: url } catch (_: Exception) { url }
            Result.success(AnalyzeResult(url, host, host, null, host))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Run yt-dlp with structured argv. Progress lines are forwarded via [onLine]. */
    suspend fun run(
        argv: List<String>,
        onLine: (String) -> Unit
    ): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val pb = ProcessBuilder(argv)
                .redirectErrorStream(true)
                .directory(context.filesDir)
            val env = pb.environment()
            env["PYTHONUNBUFFERED"] = "1"
            val process = pb.start()
            BufferedReader(process.inputStream.reader()).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    onLine(line!!)
                }
            }
            val code = process.waitFor()
            if (code == 0) Result.success(code) else Result.failure(Exception("yt-dlp exit $code"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Fetch official yt-dlp release into app files. */
    suspend fun installOfficialBinary(): Result<File> = withContext(Dispatchers.IO) {
        try {
            val url = "https://github.com/yt-dlp/yt-dlp/releases/latest/download/yt-dlp"
            val dest = defaultBinaryPath()
            dest.parentFile?.mkdirs()
            val req = Request.Builder().url(url).header("User-Agent", "Srot/1.0").build()
            client.newCall(req).execute().use { res ->
                if (!res.isSuccessful) return@withContext Result.failure(Exception("HTTP ${res.code}"))
                res.body?.byteStream()?.use { input ->
                    FileOutputStream(dest).use { output -> input.copyTo(output) }
                }
            }
            dest.setExecutable(true, false)
            Result.success(dest)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    data class AnalyzeResult(
        val url: String,
        val title: String,
        val uploader: String,
        val thumbnail: String?,
        val provider: String
    )
}
