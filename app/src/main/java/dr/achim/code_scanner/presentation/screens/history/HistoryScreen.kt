package dr.achim.code_scanner.presentation.screens.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import dr.achim.code_scanner.R
import dr.achim.code_scanner.common.DateTimeFormat
import dr.achim.code_scanner.common.DateTimeFormatter
import dr.achim.code_scanner.common.DefaultPreview
import dr.achim.code_scanner.common.DynamicPreview
import dr.achim.code_scanner.common.formatWith
import dr.achim.code_scanner.domain.model.AssistAction
import dr.achim.code_scanner.domain.model.Code
import dr.achim.code_scanner.presentation.components.AnimatedRotationContainer
import dr.achim.code_scanner.presentation.components.appbar.DefaultAppBar
import dr.achim.code_scanner.presentation.components.appbar.NavigateUpButton
import dr.achim.code_scanner.presentation.navigation.NavigateUp
import dr.achim.code_scanner.presentation.screens.Screen
import dr.achim.code_scanner.presentation.screens.home.ActionsRow
import dr.achim.code_scanner.presentation.theme.AppTheme

data class Category(val name: String, val items: List<Code>)

@Composable
fun HistoryScreen(
    currentScreen: Screen,
    navigateUp: NavigateUp?,
    viewState: HistoryScreenState,
    onClickAction: (AssistAction) -> Unit,
) {
    var selectionMode by remember { mutableStateOf(false) }
    val selectedItems = remember { mutableStateListOf<Code>() }
    val onChangeSelectionMode: (Boolean) -> Unit = {
        selectionMode = it

        if (!selectionMode) {
            selectedItems.clear()
        }
    }
    val hasHistory = remember(viewState) {
        viewState is HistoryScreenState.Success && viewState.categories.isNotEmpty()
    }

    var showConfirmDeletionDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Crossfade(selectionMode) { selectionMode ->
                if (selectionMode) {
                    SelectionTopAppBar(
                        title = stringResource(
                            R.string.screen_history_items_selected,
                            selectedItems.size
                        ),
                        onChangeSelectionMode = onChangeSelectionMode,
                        onDeleteSelected = {
                            (viewState as? HistoryScreenState.Success)?.onDelete?.invoke(
                                selectedItems
                            )
                        },
                    )
                } else {
                    HistoryTopAppBar(
                        title = currentScreen.title,
                        navigateUp = navigateUp,
                        onChangeSelectionMode = onChangeSelectionMode,
                        clearHistoryEnabled = hasHistory,
                        onDeleteAll = {
                            showConfirmDeletionDialog = true
                        },
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (viewState) {
                is HistoryScreenState.Error -> Text("Error")
                is HistoryScreenState.Loading -> CircularProgressIndicator()
                is HistoryScreenState.Success -> {
                    HistoryScreenContent(
                        categories = viewState.categories,
                        selectedItems = selectedItems,
                        selectionMode = selectionMode,
                        addItem = {
                            selectedItems.add(it)
                            onChangeSelectionMode(true)
                        },
                        removeItem = {
                            selectedItems.remove(it)
                            if (selectedItems.isEmpty()) {
                                onChangeSelectionMode(false)
                            }
                        },
                        onClickAction = onClickAction
                    )
                }
            }
        }
    }

    if (showConfirmDeletionDialog) {
        ConfirmDeletionDialog(
            onDismiss = { showConfirmDeletionDialog = false },
            onConfirm = {
                (viewState as? HistoryScreenState.Success)?.onDeleteAll?.invoke()
                showConfirmDeletionDialog = false
            },
        )
    }
}

@Composable
fun ConfirmDeletionDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.screen_history_confirm_deletion_title))
        },
        text = {
            Text(text = stringResource(R.string.screen_history_confirm_deletion_description))
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.common_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.common_cancel))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectionTopAppBar(
    title: String,
    onChangeSelectionMode: (Boolean) -> Unit,
    onDeleteSelected: () -> Unit
) {
    DefaultAppBar(
        title = title,
        leadingIcon = { NavigateUpButton { onChangeSelectionMode(false) } },
        actions = {
            IconButton(
                onClick = {
                    onDeleteSelected()
                    onChangeSelectionMode(false)
                },
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.access_icon_trash)
                )
            }

        },
        colors = DefaultAppBar.colors(
            containerColor = selectedColorFor(AppTheme.colorScheme.surface)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryTopAppBar(
    title: String,
    navigateUp: NavigateUp?,
    onChangeSelectionMode: (Boolean) -> Unit,
    clearHistoryEnabled: Boolean,
    onDeleteAll: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    DefaultAppBar(
        title = title,
        navigateUp = navigateUp,
        actions = {
            IconButton(onClick = { onChangeSelectionMode(true) }) {
                Icon(
                    Icons.Default.EditNote,
                    contentDescription = stringResource(R.string.access_icon_edit_list)
                )
            }

            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.access_icon_more_options)
                )
            }
            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.screen_history_clear_all)) },
                    onClick = {
                        onDeleteAll()
                        menuExpanded = false
                    },
                    enabled = clearHistoryEnabled
                )
            }
        }
    )
}

@Composable
private fun HistoryScreenContent(
    categories: List<Category>,
    selectedItems: List<Code>,
    selectionMode: Boolean,
    addItem: (Code) -> Unit,
    removeItem: (Code) -> Unit,
    onClickAction: (AssistAction) -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            vertical = AppTheme.spacing.m,
            horizontal = AppTheme.spacing.s
        )
    ) {
        categories.forEach { category ->
            item {
                CategoryHeader(category.name)
            }
            items(category.items, key = { it.id }) { code ->
                HistoryListItem(
                    code = code,
                    selectedItems = selectedItems,
                    selectionMode = selectionMode,
                    addItem = { addItem(code) },
                    removeItem = { removeItem(code) },
                    onClickAction = onClickAction,
                    modifier = Modifier.animateItem(),
                )
            }
        }
    }
}

@Composable
private fun CategoryHeader(name: String) {
    Text(
        text = name,
        modifier = Modifier
            .padding(top = AppTheme.spacing.m, bottom = AppTheme.spacing.s)
            .padding(horizontal = AppTheme.spacing.m),
        color = AppTheme.colorScheme.secondary,
        style = AppTheme.typography.titleSmall
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HistoryListItem(
    code: Code,
    selectedItems: List<Code>,
    selectionMode: Boolean,
    addItem: () -> Unit,
    removeItem: () -> Unit,
    onClickAction: (AssistAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val dateTimeFormatter = remember { DateTimeFormatter(context) }
    var expanded by remember { mutableStateOf<Boolean>(false) }

    val selected = selectedItems.contains(code)
    val colors = if (selected) {
        ListItemDefaults.colors(containerColor = selectedColorFor(AppTheme.colorScheme.surfaceContainerHighest))
    } else {
        ListItemDefaults.colors()
    }

    val toggleSelectItem: () -> Unit = {
        if (selected) removeItem() else addItem()
    }

    val expandArrowRotation by animateFloatAsState(if (expanded) 180f else 0f)

    ListItem(
        headlineContent = {
            Text(
                text = code.displayValue ?: "",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        modifier = modifier
            .padding(vertical = AppTheme.spacing.xs)
            .clip(AppTheme.shapes.medium)
            .combinedClickable(
                onClick = {
                    if (selected or selectionMode) {
                        toggleSelectItem()
                    } else {
                        expanded = !expanded
                    }
                },
                onLongClick = toggleSelectItem,
            ),
        overlineContent = {
            val formattedDateTime = code.created.formatWith(dateTimeFormatter) {
                format = DateTimeFormat.Style.DateTimeShort
            }
            Text(formattedDateTime)
        },
        leadingContent = {
            AnimatedRotationContainer(
                targetState = selected,
                initialContainerColor = AppTheme.colorScheme.surfaceContainerHigh,
                targetContainerColor = selectedColorFor(AppTheme.colorScheme.primary),
                shape = CircleShape,
                modifier = Modifier
                    .combinedClickable(
                        onClick = toggleSelectItem,
                        onLongClick = toggleSelectItem,
                    )
                    .padding(AppTheme.spacing.sm)
            ) { selectedState ->
                if (selectedState) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                    )
                } else {
                    Icon(
                        imageVector = code.icon,
                        contentDescription = null,
                    )
                }
            }
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Default.ExpandMore,
                contentDescription = null,
                modifier = Modifier.rotate(expandArrowRotation)
            )
        },
        colors = colors
    )
    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn() + expandVertically(),
        exit = shrinkVertically() + fadeOut()
    ) {
        ActionsRow(code.actions, onClickAction)
    }
}

@Composable
private fun selectedColorFor(color: Color): Color {
    val colorOverlay = AppTheme.colorScheme.primaryContainer
    return remember(color) {
        lerp(
            start = colorOverlay,
            stop = color,
            fraction = 0.5f
        )
    }
}

@DynamicPreview
@DefaultPreview
@Composable
private fun Preview() {
    val categories = (0 until 90 step 5).map {
        Code.Text(
            id = "$it",
            rawValue = "rawValue $it",
            displayValue = "displayValue $it",
            text = "text $it"
        )
    }
        .sortedByDescending { it.created }
        .groupBy {
            it.created.formatWith(DateTimeFormatter(LocalContext.current)) {
                format = DateTimeFormat.Pattern.MonthYear
            }
        }
        .map { Category(it.key, it.value) }

    AppTheme {
        HistoryScreen(
            currentScreen = Screen.History,
            navigateUp = {},
            viewState = HistoryScreenState.Success(
                categories = categories,
                onDelete = {},
                onDeleteAll = {}
            ),
            onClickAction = {}
        )
    }
}