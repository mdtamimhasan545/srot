package com.srot.downloader.ui.queue

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.srot.downloader.MainViewModel
import com.srot.downloader.R
import com.srot.downloader.data.JobStatus

@Composable
fun QueueScreen(vm: MainViewModel) {
    val state by vm.state.collectAsState()
    if (state.jobs.isEmpty()) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(stringResource(R.string.empty_queue), style = MaterialTheme.typography.titleMedium)
            Text(stringResource(R.string.empty_queue_hint), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        return
    }
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.jobs, key = { it.id }) { job ->
            Card(shape = RoundedCornerShape(24.dp)) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(job.title, style = MaterialTheme.typography.titleSmall)
                    Text(job.status.name, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    if (job.status == JobStatus.RUNNING) {
                        LinearProgressIndicator(progress = { job.progress.coerceIn(0f, 1f) })
                    }
                    if (job.status == JobStatus.QUEUED || job.status == JobStatus.RUNNING) {
                        TextButton(onClick = { vm.cancelJob(job.id) }) {
                            Text(stringResource(R.string.cancel))
                        }
                    }
                    job.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            }
        }
    }
}
