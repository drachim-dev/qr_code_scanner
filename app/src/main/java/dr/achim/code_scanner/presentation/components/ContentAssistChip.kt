package dr.achim.code_scanner.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import dr.achim.code_scanner.common.DefaultPreview
import dr.achim.code_scanner.common.Dimens
import dr.achim.code_scanner.domain.model.ContentType
import dr.achim.code_scanner.presentation.theme.AppTheme
import dr.achim.code_scanner.presentation.theme.LocalSpacing

@Composable
fun ContentAssistChip(
    assistAction: ContentType.AssistAction,
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
                modifier = Modifier.padding(vertical = LocalSpacing.current.m),
                text = assistAction.label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        shape = ShapeDefaults.Large,
        border = null,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        )
    )
}

@DefaultPreview
@Composable
fun UriAssistChipPreview() {
    AppTheme {
        ContentAssistChip(assistAction = ContentType.AssistAction.Copy("Dummy")) { }
    }
}