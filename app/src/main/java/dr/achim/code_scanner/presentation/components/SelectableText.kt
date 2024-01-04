package dr.achim.code_scanner.presentation.components

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun SelectableText(clearFocus: Boolean, content: @Composable () -> Unit) {
    if (clearFocus) {
        content()
    } else {
        SelectionContainer {
            content()
        }
    }
}

