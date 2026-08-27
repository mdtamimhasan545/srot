package com.srot.downloader.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.srot.downloader.MainViewModel
import com.srot.downloader.R

@Composable
fun SettingsScreen(vm: MainViewModel) {
    val state by vm.state.collectAsState()
    val s = state.settings
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.settings), style = MaterialTheme.typography.headlineSmall)

        Text(stringResource(R.string.language), style = MaterialTheme.typography.titleSmall)
        androidx.compose.foundation.layout.Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = s.language == "en", onClick = {
                vm.updateSettings { it.copy(language = "en") }
            }, label = { Text(stringResource(R.string.english)) })
            FilterChip(selected = s.language == "bn", onClick = {
                vm.updateSettings { it.copy(language = "bn") }
            }, label = { Text(stringResource(R.string.bengali)) })
        }

        Text(stringResource(R.string.theme), style = MaterialTheme.typography.titleSmall)
        androidx.compose.foundation.layout.Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("system" to R.string.theme_system, "light" to R.string.theme_light, "dark" to R.string.theme_dark).forEach { (key, res) ->
                FilterChip(selected = s.theme == key, onClick = {
                    vm.updateSettings { it.copy(theme = key) }
                }, label = { Text(stringResource(res)) })
            }
        }

        androidx.compose.foundation.layout.Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.wifi_only))
            Switch(checked = s.wifiOnly, onCheckedChange = { v -> vm.updateSettings { it.copy(wifiOnly = v) } })
        }

        OutlinedTextField(
            value = s.ytdlpPath,
            onValueChange = { v -> vm.updateSettings { it.copy(ytdlpPath = v) } },
            label = { Text(stringResource(R.string.ytdlp_path)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = s.ffmpegPath,
            onValueChange = { v -> vm.updateSettings { it.copy(ffmpegPath = v) } },
            label = { Text(stringResource(R.string.ffmpeg_path)) },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = s.outputDir,
            onValueChange = { v -> vm.updateSettings { it.copy(outputDir = v) } },
            label = { Text(stringResource(R.string.output_folder)) },
            modifier = Modifier.fillMaxWidth()
        )

        Button(onClick = vm::fetchBinary, modifier = Modifier.fillMaxWidth()) {
            Text(stringResource(R.string.fetch_binary))
        }

        state.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
    }
}
