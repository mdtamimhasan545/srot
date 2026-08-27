package com.srot.downloader.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.modifier.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.srot.downloader.R

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineMedium)
        Text("v1.0.0", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(stringResource(R.string.about_body), style = MaterialTheme.typography.bodyMedium)
        Text(stringResource(R.string.legal_title), style = MaterialTheme.typography.titleMedium)
        Text(stringResource(R.string.legal_body), style = MaterialTheme.typography.bodyMedium)
        Text("yt-dlp - Unlicense", style = MaterialTheme.typography.bodySmall)
        Text("FFmpeg - LGPL / GPL", style = MaterialTheme.typography.bodySmall)
    }
}
