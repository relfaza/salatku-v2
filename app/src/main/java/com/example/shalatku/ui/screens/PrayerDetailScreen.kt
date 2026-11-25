package com.example.shalatku.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shalatku.Prayer

@Composable
fun PrayerDetailScreen(
    prayer: Prayer?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (prayer == null) {
            Text(
                text = "Prayer not found",
                style = MaterialTheme.typography.headlineSmall
            )
        } else {
            Text(
                text = prayer.name,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = prayer.time,
                style = MaterialTheme.typography.bodyLarge
            )
        }
        Button(onClick = onBack) {
            Text("Back")
        }
    }
}