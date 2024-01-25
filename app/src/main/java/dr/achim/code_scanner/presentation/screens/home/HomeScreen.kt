package dr.achim.code_scanner.presentation.screens.home

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.google.mlkit.vision.barcode.common.Barcode
import dr.achim.code_scanner.R
import dr.achim.code_scanner.common.Constants
import dr.achim.code_scanner.common.DynamicPreview
import dr.achim.code_scanner.domain.model.ContentType
import dr.achim.code_scanner.presentation.components.ContentContainer
import dr.achim.code_scanner.presentation.components.DefaultAppBar
import dr.achim.code_scanner.presentation.components.EmptyView
import dr.achim.code_scanner.presentation.components.SelectableText
import dr.achim.code_scanner.presentation.screens.Screen
import dr.achim.code_scanner.presentation.screens.info.InfoDialog
import dr.achim.code_scanner.presentation.theme.AppTheme

@Composable
fun HomeScreen(
    viewState: HomeScreenState,
    onClickAction: (ContentType.AssistAction) -> Unit,
    onScanResult: (ContentType) -> Unit,
    onNavigateToLibraries: () -> Unit,
    currentScreen: Screen,
) {

    HomeScreenContent(
        contentType = viewState.contentType,
        onClickRescan = viewState.startScanning,
        onClickAction = onClickAction,
        onNavigateToLibraries = onNavigateToLibraries,
        currentScreen = currentScreen,
    )

    LaunchedEffect(viewState.contentType) {
        viewState.contentType?.let(onScanResult)
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HomeScreenContent(
    contentType: ContentType?,
    onClickRescan: () -> Unit,
    onClickAction: (ContentType.AssistAction) -> Unit,
    onNavigateToLibraries: () -> Unit,
    currentScreen: Screen,
) {

    var fabVisibility by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    val hasResult = contentType != null

    @StringRes
    val buttonText: Int = if (hasResult) R.string.button_scan_new else R.string.button_scan

    LaunchedEffect(Unit) {
        fabVisibility = true
    }

    Scaffold(
        topBar = {
            DefaultAppBar(
                title = currentScreen.title,
                canNavigateBack = false,
                actions = {
                    IconButton(onClick = {
                        showInfoDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = stringResource(id = R.string.screen_about)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = fabVisibility,
                enter = fadeIn(animationSpec = tween(delayMillis = Constants.DEFAULT_ANIMATION_DELAY)) + scaleIn(
                    animationSpec = tween(delayMillis = Constants.DEFAULT_ANIMATION_DELAY)
                )
            ) {
                ExtendedFloatingActionButton(
                    onClick = onClickRescan,
                    icon = {
                        Icon(
                            Icons.Default.QrCodeScanner,
                            stringResource(id = R.string.button_scan)
                        )
                    },
                    text = { Text(text = stringResource(id = buttonText)) },
                )
            }
        },
    ) { innerPadding ->
        var clearFocus by remember { mutableStateOf(false) }
        Box(
            Modifier
                .consumeWindowInsets(innerPadding)
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    clearFocus = true
                },
            contentAlignment = Alignment.Center,
        ) {

            // content actions
            AnimatedContent(
                targetState = contentType,
                label = "AnimatedContent",
                transitionSpec = {
                    fadeIn().togetherWith(fadeOut())
                }
            ) { contentType ->
                if (contentType != null) {
                    ContentContainer(contentType.icon) {
                        // Content text
                        contentType.rawContent?.let {
                            SelectableText(clearFocus = clearFocus) {
                                ContentText(it)
                            }
                        }

                        contentType.additionalContent?.let {
                            SelectableText(clearFocus = clearFocus) {
                                ContentText(
                                    text = it,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }

                        // Actions
                        ActionsRow(contentType.actions, onClickAction)

                        // Reset clear focus
                        SideEffect { if (clearFocus) clearFocus = false }
                    }
                } else {
                    EmptyView(
                        title = stringResource(id = R.string.empty_title),
                        description = stringResource(id = R.string.empty_description),
                    )
                }
            }

        }

        if (showInfoDialog) {
            InfoDialog(
                onNavigateToLibraries = onNavigateToLibraries,
                onDismissRequest = {
                    showInfoDialog = false
                }
            )
        }
    }
}

@DynamicPreview
@Composable
fun HomeScreenEmptyPreview() {
    AppTheme {
        HomeScreenContent(
            contentType = null,
            onClickRescan = {},
            onClickAction = {},
            {},
            currentScreen = Screen.Home
        )
    }
}

@DynamicPreview
@Composable
fun HomeScreenEmptyDarkPreview() {
    AppTheme(darkTheme = true) {
        HomeScreenContent(
            contentType = null,
            onClickRescan = {},
            onClickAction = {},
            {},
            currentScreen = Screen.Home
        )
    }
}

@DynamicPreview
@Composable
fun HomeScreenWifiPreview() {
    AppTheme {
        HomeScreenContent(
            contentType = ContentType.Wifi(
                Barcode.WiFi(
                    "The Ping in the North",
                    "W!n73r_!5_c0m!nG",
                    0
                )
            ),
            onClickRescan = {},
            onClickAction = {},
            {},
            currentScreen = Screen.Home
        )
    }
}

@DynamicPreview
@Composable
fun HomeScreenUrlPreview() {
    AppTheme {
        HomeScreenContent(
            contentType = ContentType.Url(Uri.parse("https://en.akinator.com/")),
            onClickRescan = {},
            onClickAction = {},
            {},
            currentScreen = Screen.Home
        )
    }
}

@DynamicPreview
@Composable
fun HomeScreenPhonePreview() {
    AppTheme {
        HomeScreenContent(
            contentType = ContentType.Phone("+49(0) 123/456789"),
            onClickRescan = {},
            onClickAction = {},
            {},
            currentScreen = Screen.Home
        )
    }
}