package dr.achim.code_scanner.presentation.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import dr.achim.code_scanner.domain.model.ContentType
import dr.achim.code_scanner.presentation.components.TintedImage

@Composable
 fun ContentIcon(contentType: ContentType?, defaultIcon: ImageVector = Icons.Filled.CameraAlt) {
    val icon = when (contentType) {
        is ContentType.Phone -> Icons.Default.Phone
        is ContentType.Text -> Icons.Default.TextSnippet
        is ContentType.Url -> Icons.Default.Link
        is ContentType.Wifi -> Icons.Default.Wifi
        is ContentType.Unknown,
        null -> defaultIcon
    }

    TintedImage(
        icon = icon,
        contentDescription = "Content icon",
        modifier = Modifier.fillMaxSize(0.75f),
    )
}