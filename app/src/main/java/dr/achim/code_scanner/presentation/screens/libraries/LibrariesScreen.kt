package dr.achim.code_scanner.presentation.screens.libraries

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import dr.achim.code_scanner.presentation.components.DefaultAppBar
import dr.achim.code_scanner.presentation.navigation.NavigateUp
import dr.achim.code_scanner.presentation.screens.Screen
import dr.achim.code_scanner.presentation.theme.AppTheme

@Composable
fun LibrariesScreen(currentScreen: Screen, navigateUp: NavigateUp?) {
    Scaffold(topBar = {
        DefaultAppBar(
            title = currentScreen.title,
            canNavigateBack = navigateUp != null,
            navigateUp = { navigateUp?.invoke() })
    }) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LibrariesContainer(
                modifier = Modifier.fillMaxSize(),
                colors = LibraryDefaults.libraryColors(
                    backgroundColor = MaterialTheme.colorScheme.background,
                    contentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.background),
                    dialogConfirmButtonColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Preview
@Composable
fun LibrariesScreenPreview() {
    AppTheme {
        LibrariesScreen(currentScreen = Screen.Libraries, {})
    }
}