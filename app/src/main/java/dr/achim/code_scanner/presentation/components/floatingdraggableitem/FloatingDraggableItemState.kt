package dr.achim.code_scanner.presentation.components.floatingdraggableitem

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize

data class FloatingDraggableItemState(private val onDraggingChangedCallback: (Boolean) -> Unit) {
    var contentSize by mutableStateOf(IntSize.Zero)
    var containerSize by mutableStateOf(IntSize.Zero)
    var containerPosition by mutableStateOf(Offset.Zero)
    var offset by mutableStateOf(IntOffset.Zero)
    var isDragging by mutableStateOf(false)

    val dragAreaSize: IntSize
        get() = IntSize(
            width = containerSize.width - contentSize.width,
            height = containerSize.height - contentSize.height,
        )

    fun updateContentSize(
        size: IntSize,
        initialOffset: (FloatingDraggableItemState) -> IntOffset,
    ) {
        val wasNotRenderedBefore = size != IntSize.Zero && contentSize == IntSize.Zero
        val offset = if (wasNotRenderedBefore) {
            contentSize = size
            initialOffset(this)
        } else {
            offset
        }
        contentSize = size
        this.offset = offset
    }

    fun onDraggingChanged(isDragging: Boolean) {
        this.isDragging = isDragging
        this.onDraggingChangedCallback(isDragging)
    }
}