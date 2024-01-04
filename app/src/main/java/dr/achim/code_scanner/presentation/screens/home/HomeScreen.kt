package dr.achim.code_scanner.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dr.achim.code_scanner.common.DefaultPreview
import dr.achim.code_scanner.domain.model.ContentType
import dr.achim.code_scanner.presentation.components.ContentContainer
import dr.achim.code_scanner.presentation.components.EmptyView
import dr.achim.code_scanner.presentation.components.SelectableText
import dr.achim.code_scanner.presentation.screens.info.InfoDialog
import dr.achim.code_scanner.presentation.theme.AppTheme

@Composable
fun HomeScreen(
    viewState: HomeScreenState,
    onClickAction: (ContentType.AssistAction) -> Unit,
    onScanResult: (ContentType) -> Unit,
    onNavigateToLibraries: () -> Unit,
) {

    HomeScreenContent(
        contentType = viewState.contentType,
        onClickRescan = viewState.startScanning,
        onClickAction = onClickAction,
        onNavigateToLibraries = onNavigateToLibraries,
    )

    LaunchedEffect(viewState.contentType) {
        viewState.contentType?.let(onScanResult)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun HomeScreenContent(
    contentType: ContentType?,
    onClickRescan: () -> Unit,
    onClickAction: (ContentType.AssistAction) -> Unit,
    onNavigateToLibraries: () -> Unit,
) {

    var showInfoDialog by remember { mutableStateOf(false) }

    val hasResult = contentType != null
    val buttonText = if (hasResult) "Scan new" else "Scan"

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                title = { Text("Slim Code Scanner") },
                actions = {
                    IconButton(onClick = {
                        showInfoDialog = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = "Info"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onClickRescan,
                icon = { Icon(Icons.Filled.QrCodeScanner, "Scan") },
                text = { Text(text = buttonText) },
            )
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
            if (contentType != null) {

                val icon = remember(contentType) {
                    when (contentType) {
                        is ContentType.Phone -> Icons.Default.Phone
                        is ContentType.Text -> Icons.Default.TextSnippet
                        is ContentType.Url -> Icons.Default.Link
                        is ContentType.Wifi -> Icons.Default.Wifi
                        is ContentType.Unknown -> Icons.Filled.CameraAlt
                    }
                }

                ContentContainer(icon) {
                    // Content text
                    SelectableText(clearFocus = clearFocus) {
                        ContentText(contentType.rawContent ?: "Empty")
                    }

                    // Actions
                    ActionsRow(contentType.actions, onClickAction)

                    // Reset clear focus
                    SideEffect { if (clearFocus) clearFocus = false }
                }
            } else {
                EmptyView(
                    title = "Looks quite empty here.",
                    description = "Start now by scanning a code",
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

@DefaultPreview
@Composable
fun HomeScreenResultPreview() {
    AppTheme {
        HomeScreenContent(
            contentType = ContentType.Phone("+49(0) 123/456789"),
            onClickRescan = {},
            onClickAction = {}) {}
    }
}

@DefaultPreview
@Composable
fun HomeScreenEmptyPreview() {
    AppTheme {
        HomeScreenContent(
            contentType = null,
            onClickRescan = {},
            onClickAction = {}) {}
    }
}