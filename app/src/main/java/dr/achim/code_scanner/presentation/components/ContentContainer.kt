package dr.achim.code_scanner.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dr.achim.code_scanner.common.Dimens
import dr.achim.code_scanner.domain.model.Code
import dr.achim.code_scanner.presentation.screens.home.ContentText
import dr.achim.code_scanner.presentation.theme.AppTheme

@Composable
fun ContentContainer(
    code: Code,
    textSelectionEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    bottomContent: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            AppTheme.spacing.m,
            Alignment.CenterVertically
        ),
    ) {
        Icon(
            imageVector = code.icon,
            contentDescription = null,
            modifier = Modifier
                .height(Dimens.contentImageSize)
                .fillMaxWidth(),
            tint = AppTheme.colorScheme.surfaceVariant
        )

        // Content text
        code.rawContent?.let {
            SelectableText(textSelectionEnabled = textSelectionEnabled) {
                ContentText(text = it)
            }
        }

        code.additionalContent?.let {
            SelectableText(textSelectionEnabled = textSelectionEnabled) {
                ContentText(text = it, style = AppTheme.typography.bodyLarge)
            }
        }

        bottomContent()
    }
}