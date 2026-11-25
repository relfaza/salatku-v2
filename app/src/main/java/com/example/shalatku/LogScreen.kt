package com.example.shalatku

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.shalatku.ui.theme.ShalatkuTheme

data class LogCell(val id: Int, val done: Boolean)

// 7 days x 5 prayers = 35 cells, read left to right then top to bottom
val sampleLog: List<LogCell> = List(35) { index ->
    LogCell(id = index, done = index % 4 != 0)
}

@Composable
fun LogScreen(cells: List<LogCell>, modifier: Modifier = Modifier) {
    Column(modifier.padding(16.dp)) {
        Text("Weekly Log", style = MaterialTheme.typography.headlineMedium)
        Text("7 days, 5 prayers each", style = MaterialTheme.typography.bodyMedium)

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(cells, key = { it.id }) { cell ->
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    // M11: the symbol alone means nothing to a screen reader.
                    modifier = Modifier
                        .aspectRatio(1f)
                        .semantics { contentDescription = if (cell.done) "Done" else "Missed" }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(if (cell.done) "✓" else "·")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogScreenPreview() {
    ShalatkuTheme {
        LogScreen(cells = sampleLog)
    }
}
