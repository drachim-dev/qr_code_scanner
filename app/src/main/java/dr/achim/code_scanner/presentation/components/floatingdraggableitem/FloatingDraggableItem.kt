package dr.achim.code_scanner.presentation.components.floatingdraggableitem

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toOffset
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.zIndex
import dr.achim.code_scanner.R
import dr.achim.code_scanner.presentation.theme.AppTheme
import kotlin.math.roundToInt

@Composable
fun rememberFloatingDraggableItemState(onDraggingChanged: (Boolean) -> Unit = {}): FloatingDraggableItemState {
    return remember {
        FloatingDraggableItemState(onDraggingChangedCallback = onDraggingChanged)
    }
}

@Composable
fun FloatingDraggableItem(
    initialOffset: (FloatingDraggableItemState) -> IntOffset,
    onDismissRequest: () -> Unit = {},
    state: FloatingDraggableItemState = rememberFloatingDraggableItemState(),
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = Modifier
            .zIndex(1f)
            .fillMaxSize()
            .onGloballyPositioned {
                state.containerSize = it.size
                state.containerPosition = it.positionInRoot()
            },
    ) {
        Box(
            modifier = Modifier
                .zIndex(1f)
                .offset { state.offset }
                .onGloballyPositioned {
                    state.updateContentSize(
                        size = it.size,
                        initialOffset = initialOffset,
                    )
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = {
                            state.onDraggingChanged(true)
                        },
                        onDragEnd = {
                            state.onDraggingChanged(false)
                        },
                        onDragCancel = {
                            state.onDraggingChanged(false)
                        },
                    ) { _, dragAmount ->
                        val calculatedX = state.offset.x + dragAmount.x.roundToInt()
                        val calculatedY = state.offset.y + dragAmount.y.roundToInt()

                        state.offset = IntOffset(
                            calculatedX.coerceIn(0, state.dragAreaSize.width),
                            calculatedY.coerceIn(0, state.dragAreaSize.height),
                        )
                    }
                },
        ) {
            content()
        }

        AnimatedVisibility(
            visible = state.isDragging,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            DragToDeleteContainer(
                isDragging = state.isDragging,
                dragPosition = state.offset.toOffset() + state.containerPosition,
                dragContainerContentSize = state.contentSize.toSize(),
                onDelete = onDismissRequest,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(164.dp)
                    .background(AppTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.5f))
            )
        }

        DisposableEffect(true) {
            // Reset position on hide
            onDispose {
                state.offset = initialOffset(state)
            }
        }
    }
}

@Composable
fun DragToDeleteContainer(
    isDragging: Boolean,
    dragPosition: Offset,
    dragContainerContentSize: Size,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var trashBinSize by remember { mutableStateOf(IntSize.Zero) }
    var trashBinPosition by remember { mutableStateOf(Offset.Zero) }

    val isDragOverBin by remember(dragPosition) {
        derivedStateOf {
            val floatingItemRect = Rect(dragPosition, dragContainerContentSize)
            val trashBinRect = Rect(trashBinPosition, trashBinSize.toSize())
            floatingItemRect.overlaps(trashBinRect)
        }
    }

    LaunchedEffect(key1 = isDragOverBin, key2 = isDragging) {
        if (isDragOverBin && !isDragging) {
            onDelete()
        }
    }

    val iconColor by animateColorAsState(
        if (isDragOverBin) Color.Red else LocalContentColor.current
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = stringResource(R.string.access_icon_trash),
            modifier = Modifier
                .size(if (isDragOverBin) 48.dp else 36.dp)
                .animateContentSize()
                .onGloballyPositioned {
                    trashBinSize = it.size
                    trashBinPosition = it.positionInRoot()
                },
            tint = iconColor
        )
    }
}