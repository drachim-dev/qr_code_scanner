package dr.achim.code_scanner.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import dr.achim.code_scanner.R
import dr.achim.code_scanner.common.DefaultPreview
import dr.achim.code_scanner.presentation.theme.AppTheme

@Composable
fun FloatingIconBubble(
    modifier: Modifier = Modifier,
    initialExpanded: Boolean = false,
    onClick: () -> Unit,
    leading: @Composable BoxScope.() -> Unit,
    content: @Composable RowScope.() -> Unit
) {

    var expanded by remember { mutableStateOf(initialExpanded) }

    ElevatedCard(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.elevatedCardColors(containerColor = AppTheme.colorScheme.secondaryContainer),
        shape = CircleShape
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .animateContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        expanded = !expanded
                    }
                    .padding(AppTheme.spacing.s),
                content = leading
            )

            if (expanded) {
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(start = AppTheme.spacing.xs, end = AppTheme.spacing.m),
                    verticalAlignment = Alignment.CenterVertically,
                    content = content
                )
            }
        }
    }
}

@DefaultPreview
@Composable
private fun ChatBubblePreview() {
    AppTheme {
        FloatingIconBubble(
            initialExpanded = false,
            onClick = {},
            leading = {
                Icon(
                    imageVector = Icons.Default.Handshake,
                    contentDescription = stringResource(R.string.access_icon_handshake)
                )
            }
        ) {
            Text(text = stringResource(R.string.screen_home_support_bubble_expanded_text))
        }
    }
}