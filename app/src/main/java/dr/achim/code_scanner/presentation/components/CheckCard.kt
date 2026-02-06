package dr.achim.code_scanner.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dr.achim.code_scanner.presentation.theme.AppTheme

@Composable
fun CheckCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(AppTheme.spacing.m),
    title: @Composable () -> Unit = {},
    description: @Composable () -> Unit = {},
    checked: Boolean = false,
    onClick: () -> Unit,
) {
    val decoratedTitle = @Composable {
        CompositionLocalProvider(LocalTextStyle provides AppTheme.typography.labelLarge) {
            title()
        }
    }

    val decoratedDescription = @Composable {
        CompositionLocalProvider(LocalTextStyle provides AppTheme.typography.bodyLarge) {
            description()
        }
    }

    ElevatedCard(
        modifier = modifier,
        onClick = onClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s),
            modifier = Modifier.padding(contentPadding)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                decoratedTitle()
                Spacer(modifier = Modifier.height(AppTheme.spacing.m))
                decoratedDescription()
            }

            RadioButton(
                selected = checked,
                onClick = onClick
            )
        }
    }
}