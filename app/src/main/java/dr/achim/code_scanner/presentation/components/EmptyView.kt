package dr.achim.code_scanner.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun EmptyView(title: String, description: String) {
    ContentContainer(Icons.Default.CameraAlt) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            modifier = Modifier,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
        )

        Text(
            text = description,
            textAlign = TextAlign.Center,
            modifier = Modifier,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}