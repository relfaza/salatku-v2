package com.example.shalatku

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shalatku.ui.theme.ShalatkuTheme

// Stateless. `expanded` is an M11 detail: false = one column, true = list + Qibla placeholder.
@Composable
fun HomeScreen(
    prayers: List<Prayer>,
    selectedDay: Int,
    onSelectDay: (Int) -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = false
) {
    if (expanded) {
        Row(modifier) {
            PrayerList(prayers, selectedDay, onSelectDay, Modifier.weight(1f))
            QiblaPlaceholder(Modifier.weight(1f).fillMaxHeight().padding(16.dp))
        }
    } else {
        PrayerList(prayers, selectedDay, onSelectDay, modifier)
    }
}

@Composable
private fun PrayerList(
    prayers: List<Prayer>,
    selectedDay: Int,
    onSelectDay: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            DateStrip(sampleDays, selectedDay, onSelectDay, Modifier.padding(bottom = 8.dp))
        }
        item {
            // M11 hierarchy: hero first, then the list, then secondary text.
            HeroCard(sampleDays.getOrNull(selectedDay), Modifier.padding(horizontal = 16.dp))
        }
        item {
            Text(
                "Today's prayer times",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
        items(prayers, key = { it.name }) { prayer ->
            PrayerCard(prayer, Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Composable
private fun HeroCard(day: DayLabel?, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Box(Modifier.padding(16.dp)) {
            Text(
                text = if (day != null) "Shalatku · ${day.weekday} ${day.date}" else "Shalatku",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Composable
fun PrayerCard(prayer: Prayer, modifier: Modifier = Modifier) {
    Card(
        modifier
            .fillMaxWidth()
            // M11: TalkBack reads "Subuh, 04:35" as one item.
            .semantics(mergeDescendants = true) {
                contentDescription = "${prayer.name}, ${prayer.time}"
            }
    ) {
        Row(
            Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(prayer.name, style = MaterialTheme.typography.titleMedium)
            Text(prayer.time, style = MaterialTheme.typography.titleMedium)
        }
    }
}

// Empty on purpose: the Qibla compass arrives in a later meeting.
@Composable
private fun QiblaPlaceholder(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Qibla (coming soon)", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ShalatkuTheme {
        HomeScreen(samplePrayers, selectedDay = 2, onSelectDay = {})
    }
}

@Preview(showBackground = true, fontScale = 2f)
@Composable
fun HomeScreenLargeFontPreview() {
    ShalatkuTheme {
        HomeScreen(samplePrayers, selectedDay = 2, onSelectDay = {})
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun HomeScreenDarkPreview() {
    ShalatkuTheme {
        HomeScreen(samplePrayers, selectedDay = 2, onSelectDay = {})
    }
}

@Preview(showBackground = true, widthDp = 900, heightDp = 600)
@Composable
fun HomeScreenExpandedPreview() {
    ShalatkuTheme {
        HomeScreen(samplePrayers, selectedDay = 2, onSelectDay = {}, expanded = true)
    }
}
