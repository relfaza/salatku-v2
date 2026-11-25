package com.example.shalatku

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shalatku.ui.theme.ShalatkuTheme

@Composable
fun TrackerScreen(
    prayers: List<Prayer>,
    donePrayers: List<String>,
    doneCount: Int,
    onToggle: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(16.dp)) {
        item {
            Text("Prayer Tracker", style = MaterialTheme.typography.headlineMedium)
            Text(
                "$doneCount of ${prayers.size} prayers done today",
                style = MaterialTheme.typography.bodyLarge
            )
            // HW12 Part A: the screen only reports the tap. The ViewModel clears the list.
            OutlinedButton(onClick = onClear) { Text("Clear all") }
            Spacer(Modifier.height(16.dp))
        }
        items(prayers, key = { it.name }) { prayer ->
            val done = prayer.name in donePrayers
            Row(
                // M11: 48dp row, and one TalkBack node that says the name and the done state.
                modifier = Modifier
                    .heightIn(min = 48.dp)
                    .semantics(mergeDescendants = true) {
                        stateDescription = if (done) "Done" else "Not done"
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = done,
                    onCheckedChange = { onToggle(prayer.name) }
                )
                Text(prayer.name, style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrackerScreenPreview() {
    ShalatkuTheme {
        TrackerScreen(
            prayers = samplePrayers,
            donePrayers = listOf("Subuh", "Dzuhur"),
            doneCount = 2,
            onToggle = {},
            onClear = {}
        )
    }
}
