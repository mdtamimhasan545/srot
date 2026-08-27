package com.srot.downloader.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("srot_settings")

class SettingsRepository(private val context: Context) {
    private val KEY_LANG = stringPreferencesKey("language")
    private val KEY_THEME = stringPreferencesKey("theme")
    private val KEY_WIFI = booleanPreferencesKey("wifi_only")
    private val KEY_YTDLP = stringPreferencesKey("ytdlp_path")
    private val KEY_FFMPEG = stringPreferencesKey("ffmpeg_path")
    private val KEY_OUT = stringPreferencesKey("output_dir")
    private val KEY_CONCURRENT = intPreferencesKey("concurrent")

    val settings: Flow<AppSettings> = context.dataStore.data.map { p ->
        AppSettings(
            language = p[KEY_LANG] ?: "en",
            theme = p[KEY_THEME] ?: "system",
            wifiOnly = p[KEY_WIFI] ?: false,
            ytdlpPath = p[KEY_YTDLP] ?: "",
            ffmpegPath = p[KEY_FFMPEG] ?: "ffmpeg",
            outputDir = p[KEY_OUT] ?: "",
            concurrent = p[KEY_CONCURRENT] ?: 1
        )
    }

    suspend fun update(transform: (AppSettings) -> AppSettings) {
        context.dataStore.edit { prefs ->
            val current = AppSettings(
                language = prefs[KEY_LANG] ?: "en",
                theme = prefs[KEY_THEME] ?: "system",
                wifiOnly = prefs[KEY_WIFI] ?: false,
                ytdlpPath = prefs[KEY_YTDLP] ?: "",
                ffmpegPath = prefs[KEY_FFMPEG] ?: "ffmpeg",
                outputDir = prefs[KEY_OUT] ?: "",
                concurrent = prefs[KEY_CONCURRENT] ?: 1
            )
            val next = transform(current)
            prefs[KEY_LANG] = next.language
            prefs[KEY_THEME] = next.theme
            prefs[KEY_WIFI] = next.wifiOnly
            prefs[KEY_YTDLP] = next.ytdlpPath
            prefs[KEY_FFMPEG] = next.ffmpegPath
            prefs[KEY_OUT] = next.outputDir
            prefs[KEY_CONCURRENT] = next.concurrent
        }
    }
}
