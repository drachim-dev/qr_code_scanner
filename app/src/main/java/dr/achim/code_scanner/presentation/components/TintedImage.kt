package dr.achim.code_scanner.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun TintedImage(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String?,
    tint: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint
    )
}