package com.example.shalatku

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shalatku.ui.theme.ShalatkuTheme

data class DayLabel(val weekday: String, val date: String)

val sampleDays = listOf(
    DayLabel("Mon", "20"), DayLabel("Tue", "21"), DayLabel("Wed", "22"),
    DayLabel("Thu", "23"), DayLabel("Fri", "24"), DayLabel("Sat", "25"),
    DayLabel("Sun", "26"),
)

@Composable
fun DateStrip(
    days: List<DayLabel>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(days, key = { _, day -> day.date }) { index, day ->
            val selected = index == selectedIndex
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = if (selected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant,
                contentColor = if (selected) MaterialTheme.colorScheme.onPrimary
                               else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    // M11: at least 48dp touch target, announced as a selectable tab.
                    .heightIn(min = 48.dp)
                    .widthIn(min = 48.dp)
                    .selectable(
                        selected = selected,
                        role = Role.Tab,
                        onClick = { onSelect(index) }
                    )
                    .semantics { contentDescription = "${day.weekday} ${day.date}" }
            ) {
                Column(
                    Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(day.weekday, style = MaterialTheme.typography.labelMedium)
                    Text(day.date, style = MaterialTheme.typography.titleMedium)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DateStripPreview() {
    ShalatkuTheme {
        DateStrip(days = sampleDays, selectedIndex = 2, onSelect = {})
    }
}
