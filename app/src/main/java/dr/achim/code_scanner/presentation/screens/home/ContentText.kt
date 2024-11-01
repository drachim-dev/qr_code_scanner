package dr.achim.code_scanner.presentation.screens.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import dr.achim.code_scanner.presentation.theme.AppTheme

@Composable
fun ContentText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = AppTheme.typography.headlineSmall
) {
    Text(
        text = text,
        modifier = modifier,
        style = style.copy(
            lineBreak = LineBreak(
                LineBreak.Strategy.HighQuality,
                LineBreak.Strictness.Strict,
                LineBreak.WordBreak.Unspecified
            )
        ),
        overflow = TextOverflow.Ellipsis,
        maxLines = 4,
        textAlign = TextAlign.Center,
    )
}