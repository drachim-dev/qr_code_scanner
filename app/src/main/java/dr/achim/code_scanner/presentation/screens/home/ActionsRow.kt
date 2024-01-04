package dr.achim.code_scanner.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import dr.achim.code_scanner.domain.model.ContentType
import dr.achim.code_scanner.presentation.components.ContentAssistChip
import dr.achim.code_scanner.presentation.theme.LocalSpacing

@Composable
fun ActionsRow(
    actions: List<ContentType.AssistAction>,
    onClickAction: (ContentType.AssistAction) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(LocalSpacing.current.m),
        horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.s)
    ) {
        items(actions) {
            ContentAssistChip(it) {
                onClickAction(it)
            }
        }
    }
}