package dr.achim.code_scanner.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import dr.achim.code_scanner.common.DefaultPreview
import dr.achim.code_scanner.common.Dimens
import dr.achim.code_scanner.domain.model.AssistAction
import dr.achim.code_scanner.presentation.theme.AppTheme

@Composable
fun ContentAssistChip(
    assistAction: AssistAction,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    AssistChip(
        modifier = modifier
            .widthIn(max = Dimens.chipMaxWidth),
        onClick = onClick,
        leadingIcon = { Icon(assistAction.icon, null) },
        label = {
            Text(
                modifier = Modifier.padding(vertical = AppTheme.spacing.m),
                text = stringResource(id = assistAction.label),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AppTheme.typography.bodyLarge,
            )
        },
        shape = ShapeDefaults.Large,
        border = null,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = AppTheme.colorScheme.primaryContainer,
        )
    )
}

@DefaultPreview
@Composable
private fun UriAssistChipPreview() {
    AppTheme {
        ContentAssistChip(assistAction = AssistAction.Copy("")) { }
    }
}