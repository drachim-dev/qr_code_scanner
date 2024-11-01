package dr.achim.code_scanner.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import dr.achim.code_scanner.common.DefaultPreview
import dr.achim.code_scanner.common.DynamicPreview
import dr.achim.code_scanner.presentation.theme.AppTheme

@Composable
fun AnimatedRotationContainer(
    targetState: Boolean,
    initialContainerColor: Color,
    targetContainerColor: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
    content: @Composable (targetState: Boolean) -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (targetState) 180f else 0f,
        animationSpec = tween(easing = LinearEasing)
    )

    val containerColor = if (targetState) targetContainerColor else initialContainerColor

    Box(
        modifier = Modifier
            .graphicsLayer { rotationY = rotation }
            .clip(shape)
            .background(containerColor)
            .then(modifier),
    ) {
        AnimatedContent(targetState) { targetState ->
            Box(modifier = Modifier.graphicsLayer { rotationY = -rotation }) {
                content(targetState)
            }
        }
    }
}

@DynamicPreview
@DefaultPreview
@Composable
private fun Preview() {
    AppTheme {
        var selected by remember { mutableStateOf(false) }

        AnimatedRotationContainer(
            targetState = selected,
            initialContainerColor = AppTheme.colorScheme.surfaceContainerHigh,
            targetContainerColor = AppTheme.colorScheme.primary,
            shape = CircleShape,
            modifier = Modifier
                .clickable(onClick = { selected = !selected })
                .padding(AppTheme.spacing.sm)
        ) { targetState ->
            Icon(
                imageVector = if (targetState) Icons.Default.Check else Icons.Default.People,
                contentDescription = null,
            )
        }
    }
}