package dr.achim.code_scanner.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import dr.achim.code_scanner.common.DefaultPreview
import dr.achim.code_scanner.common.Dimens
import dr.achim.code_scanner.presentation.theme.AppTheme

@Composable
fun EmptyView(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(AppTheme.spacing.l)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            AppTheme.spacing.m,
            Alignment.CenterVertically
        ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier
                .height(Dimens.contentImageSize)
                .fillMaxWidth(),
            tint = AppTheme.colorScheme.surfaceVariant
        )

        Text(
            text = title,
            textAlign = TextAlign.Center,
            modifier = Modifier,
            style = AppTheme.typography.titleLarge,
            color = AppTheme.colorScheme.secondary,
        )

        Text(
            text = description,
            textAlign = TextAlign.Center,
            modifier = Modifier,
            style = AppTheme.typography.bodyLarge,
            color = AppTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@DefaultPreview
@Composable
private fun Preview() {
    AppTheme {
        EmptyView(title = "title", description = "description", icon = Icons.Default.CameraAlt)
    }
}