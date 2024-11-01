package dr.achim.code_scanner.presentation.components

import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.runtime.Composable

@Composable
fun SelectableText(textSelectionEnabled: Boolean, content: @Composable () -> Unit) {
    if (textSelectionEnabled) {
        SelectionContainer {
            content()
        }
    } else {
        content()
    }
}