package dr.achim.code_scanner.presentation.screens.home

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dr.achim.code_scanner.domain.model.AssistAction
import dr.achim.code_scanner.presentation.components.ContentAssistChip
import dr.achim.code_scanner.presentation.theme.AppTheme

@Composable
fun ActionsRow(
    actions: List<AssistAction>,
    onClickAction: (AssistAction) -> Unit
) {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(PaddingValues(AppTheme.spacing.m)),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.s)
    ) {
        actions.forEach {
            ContentAssistChip(it) {
                onClickAction(it)
            }
        }
    }
}