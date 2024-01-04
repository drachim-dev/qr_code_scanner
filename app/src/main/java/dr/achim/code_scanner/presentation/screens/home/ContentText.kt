package dr.achim.code_scanner.presentation.screens.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun ContentText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        overflow = TextOverflow.Ellipsis,
        textAlign = TextAlign.Center,
    )
}