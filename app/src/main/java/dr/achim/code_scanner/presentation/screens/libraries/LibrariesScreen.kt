package dr.achim.code_scanner.presentation.screens.libraries

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import dr.achim.code_scanner.common.DefaultPreview
import dr.achim.code_scanner.common.DynamicPreview
import dr.achim.code_scanner.presentation.components.appbar.DefaultAppBar
import dr.achim.code_scanner.presentation.navigation.NavigateUp
import dr.achim.code_scanner.presentation.screens.Screen
import dr.achim.code_scanner.presentation.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibrariesScreen(currentScreen: Screen, navigateUp: NavigateUp?) {
    Scaffold(
        topBar = {
            DefaultAppBar(
                title = currentScreen.title,
                navigateUp = navigateUp
            )
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            LibrariesContainer(
                modifier = Modifier.fillMaxSize(),
                colors = LibraryDefaults.libraryColors(
                    backgroundColor = AppTheme.colorScheme.background,
                    contentColor = contentColorFor(backgroundColor = AppTheme.colorScheme.background),
                    dialogConfirmButtonColor = AppTheme.colorScheme.primary
                )
            )
        }
    }
}

@DynamicPreview
@DefaultPreview
@Composable
fun LibrariesScreenPreview() {
    AppTheme {
        LibrariesScreen(currentScreen = Screen.Libraries) {}
    }
}