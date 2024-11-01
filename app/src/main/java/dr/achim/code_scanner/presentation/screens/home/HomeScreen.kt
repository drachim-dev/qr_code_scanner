package dr.achim.code_scanner.presentation.screens.home

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dr.achim.code_scanner.R
import dr.achim.code_scanner.common.Animation
import dr.achim.code_scanner.common.DefaultPreview
import dr.achim.code_scanner.common.DynamicPreview
import dr.achim.code_scanner.common.preview.CodeParameterProvider
import dr.achim.code_scanner.domain.model.AssistAction
import dr.achim.code_scanner.domain.model.Code
import dr.achim.code_scanner.presentation.components.ContentContainer
import dr.achim.code_scanner.presentation.components.EmptyView
import dr.achim.code_scanner.presentation.components.FloatingIconBubble
import dr.achim.code_scanner.presentation.components.appbar.DefaultAppBar
import dr.achim.code_scanner.presentation.components.floatingdraggableitem.FloatingDraggableItem
import dr.achim.code_scanner.presentation.components.floatingdraggableitem.FloatingDraggableItemState
import dr.achim.code_scanner.presentation.components.floatingdraggableitem.rememberFloatingDraggableItemState
import dr.achim.code_scanner.presentation.screens.Screen
import dr.achim.code_scanner.presentation.screens.about.AboutDialog
import dr.achim.code_scanner.presentation.theme.AppTheme

@Composable
fun HomeScreen(
    viewState: HomeScreenState,
    autoStartCameraEnabled: Boolean,
    onAutoStartCameraChange: (Boolean) -> Unit,
    showSupportHint: Boolean,
    onDismissSupportHint: () -> Unit,
    onClickAction: (AssistAction) -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToLibraries: () -> Unit,
    currentScreen: Screen,
) {
    val floatingDraggableItemState = rememberFloatingDraggableItemState()
    var showSupportMeDialog by remember { mutableStateOf(false) }
    var textSelectionEnabled by remember { mutableStateOf(true) }

    val hasResult = viewState.code != null

    Scaffold(
        topBar = {
            HomeScreenAppBar(
                currentScreen = currentScreen,
                onNavigateToHistory = onNavigateToHistory,
                autoStartCameraEnabled = autoStartCameraEnabled,
                onAutoStartCameraChange = onAutoStartCameraChange,
                onNavigateToLibraries = onNavigateToLibraries,
                onNavigateToSupport = { showSupportMeDialog = true }
            )
        },
        floatingActionButton = {
            @StringRes
            val buttonText: Int = if (hasResult) {
                R.string.screen_home_button_scan_new
            } else {
                R.string.screen_home_button_scan
            }

            HomeScreenFab(
                text = stringResource(buttonText),
                visible = !floatingDraggableItemState.isDragging,
                onClick = viewState.startScanning
            )
        },
    ) { innerPadding ->

        val toggleTextSelection: (Boolean) -> Unit = { textSelectionEnabled = it }

        Box(
            Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    toggleTextSelection(false)
                },
            contentAlignment = Alignment.Center,
        ) {
            SupportMeBubble(
                showSupportHint = showSupportHint,
                onDismissSupportHint = onDismissSupportHint,
                floatingDraggableItemState = floatingDraggableItemState,
                onClick = { showSupportMeDialog = true }
            )

            ScannedContent(
                code = viewState.code,
                textSelectionEnabled = textSelectionEnabled,
                toggleTextSelection = toggleTextSelection,
                placeholder = {
                    EmptyView(
                        title = stringResource(id = R.string.screen_home_empty_title),
                        description = stringResource(id = R.string.screen_home_empty_description),
                    )
                },
                onClickAction = onClickAction,
            )
        }
    }

    if (showSupportMeDialog) {
        AboutDialog(onDismissRequest = { showSupportMeDialog = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenAppBar(
    currentScreen: Screen,
    onNavigateToHistory: () -> Unit,
    autoStartCameraEnabled: Boolean,
    onAutoStartCameraChange: (Boolean) -> Unit,
    onNavigateToLibraries: () -> Unit,
    onNavigateToSupport: () -> Unit,
) {
    var showMoreDialog by remember { mutableStateOf(false) }

    DefaultAppBar(
        title = currentScreen.title,
        navigateUp = null,
        actions = {
            IconButton(onClick = onNavigateToHistory) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = stringResource(id = R.string.screen_history)
                )
            }
            IconButton(
                onClick = { showMoreDialog = true }
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(id = R.string.access_icon_more_options)
                )
            }

            val dismissDropdownDialog = { showMoreDialog = false }

            DropdownMenu(
                expanded = showMoreDialog,
                onDismissRequest = dismissDropdownDialog,
                modifier = Modifier.padding(horizontal = AppTheme.spacing.s)
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.screen_home_menu_auto_start_camera)) },
                    trailingIcon = {
                        Switch(
                            checked = autoStartCameraEnabled,
                            onCheckedChange = onAutoStartCameraChange
                        )
                    },
                    onClick = { onAutoStartCameraChange(!autoStartCameraEnabled) },
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text(stringResource(id = R.string.screen_home_menu_about)) },
                    onClick = {
                        dismissDropdownDialog()
                        onNavigateToSupport()
                    },
                )
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.screen_home_menu_libraries)) },
                    onClick = {
                        dismissDropdownDialog()
                        onNavigateToLibraries()
                    }
                )
            }
        }
    )
}

@Composable
private fun HomeScreenFab(text: String, visible: Boolean = true, onClick: () -> Unit) {
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(tween(delayMillis = Animation.DefaultDelay)),
        exit = scaleOut()
    ) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            icon = {
                Icon(
                    Icons.Default.QrCodeScanner,
                    stringResource(id = R.string.screen_home_button_scan)
                )
            },
            text = { Text(text = text) },
        )
    }
}

@Composable
private fun ScannedContent(
    code: Code?,
    textSelectionEnabled: Boolean,
    toggleTextSelection: (Boolean) -> Unit,
    placeholder: @Composable () -> Unit,
    onClickAction: (AssistAction) -> Unit,
) {
    AnimatedContent(
        targetState = code,
        modifier = Modifier.padding(AppTheme.spacing.m),
        label = "",
        transitionSpec = { fadeIn().togetherWith(fadeOut()) }
    ) { targetState ->

        if (targetState == null) {
            placeholder()
        } else {
            ContentContainer(
                code = targetState,
                textSelectionEnabled = textSelectionEnabled,
                bottomContent = {
                    ActionsRow(targetState.actions, onClickAction)
                }
            )

            // Reset clear focus
            SideEffect { if (!textSelectionEnabled) toggleTextSelection(true) }
        }
    }
}

@Composable
private fun SupportMeBubble(
    showSupportHint: Boolean,
    onDismissSupportHint: () -> Unit,
    floatingDraggableItemState: FloatingDraggableItemState,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = showSupportHint,
        modifier = Modifier.zIndex(1f),
        enter = fadeIn(),
        exit = shrinkOut(shrinkTowards = Alignment.TopCenter) + fadeOut()
    ) {
        FloatingDraggableItem(
            initialOffset = { state ->
                IntOffset(
                    x = 32,
                    y = state.containerSize.height / 6,
                )
            },
            onDismissRequest = onDismissSupportHint,
            state = floatingDraggableItemState
        ) {
            FloatingIconBubble(
                onClick = onClick,
                leading = {
                    Icon(
                        imageVector = Icons.Default.Handshake,
                        contentDescription = stringResource(R.string.access_icon_handshake),
                        modifier = Modifier.size(32.dp)
                    )
                }
            ) {
                Text(text = stringResource(R.string.screen_home_support_bubble_expanded_text))
            }
        }
    }
}

@DynamicPreview
@DefaultPreview
@Composable
private fun EmptyPreview() {
    AppTheme {
        HomeScreen(
            viewState = HomeScreenState(
                code = null,
                startScanning = {}
            ),
            autoStartCameraEnabled = false,
            onAutoStartCameraChange = {},
            showSupportHint = true,
            onDismissSupportHint = {},
            onClickAction = {},
            onNavigateToHistory = {},
            onNavigateToLibraries = {},
            currentScreen = Screen.Home
        )
    }
}

@DefaultPreview
@Composable
private fun Preview(@PreviewParameter(CodeParameterProvider::class) code: Code) {
    AppTheme {
        HomeScreen(
            viewState = HomeScreenState(code = code, startScanning = {}),
            autoStartCameraEnabled = false,
            onAutoStartCameraChange = {},
            showSupportHint = true,
            onDismissSupportHint = {},
            onClickAction = {},
            onNavigateToHistory = {},
            onNavigateToLibraries = {},
            currentScreen = Screen.Home
        )
    }
}