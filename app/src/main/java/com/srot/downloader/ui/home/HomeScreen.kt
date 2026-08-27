package com.srot.downloader.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.srot.downloader.MainViewModel
import com.srot.downloader.R
import com.srot.downloader.data.Preset

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(vm: MainViewModel, initialUrl: String?) {
    val state by vm.state.collectAsState()
    val clipboard = LocalClipboardManager.current

    LaunchedEffect(initialUrl) {
        if (!initialUrl.isNullOrBlank()) vm.setUrl(initialUrl.trim())
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineMedium)
        Text(stringResource(R.string.app_tag), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Card(shape = RoundedCornerShape(28.dp)) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = state.urlInput,
                    onValueChange = vm::setUrl,
                    label = { Text(stringResource(R.string.paste_url)) },
                    minLines = 3,
                    shape = RoundedCornerShape(16.dp)
                )
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(onClick = {
                        clipboard.getText()?.text?.let { vm.setUrl(it.trim()) }
                    }) { Text(stringResource(R.string.paste)) }
                    Button(onClick = vm::analyze, enabled = !state.analyzing) {
                        if (state.analyzing) Text("...")
                        else Text(stringResource(R.string.analyze))
                    }
                }
            }
        }

        Text(stringResource(R.string.best), style = MaterialTheme.typography.labelLarge)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            PresetChip("Best", Preset.BEST, state.preset, vm::setPreset)
            PresetChip("4K", Preset.UHD, state.preset, vm::setPreset)
            PresetChip("1080p", Preset.P1080, state.preset, vm::setPreset)
            PresetChip("720p", Preset.P720, state.preset, vm::setPreset)
            PresetChip(stringResource(R.string.audio_only), Preset.AUDIO, state.preset, vm::setPreset)
        }

        state.media?.let { media ->
            Card(shape = RoundedCornerShape(28.dp)) {
                Column {
                    media.thumbnail?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(media.title, style = MaterialTheme.typography.titleMedium)
                        Text(media.uploader, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Button(onClick = vm::enqueueDownload) {
                            Text(stringResource(R.string.download))
                        }
                    }
                }
            }
        }

        state.message?.let {
            Text(it, color = MaterialTheme.colorScheme.primary)
        }

        if (!state.binaryReady) {
            Card(shape = RoundedCornerShape(20.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.engine_missing), style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = vm::fetchBinary) { Text(stringResource(R.string.fetch_binary)) }
                }
            }
        }
    }
}

@Composable
private fun PresetChip(label: String, preset: Preset, selected: Preset, onSelect: (Preset) -> Unit) {
    FilterChip(
        selected = selected == preset,
        onClick = { onSelect(preset) },
        label = { Text(label) }
    )
}
