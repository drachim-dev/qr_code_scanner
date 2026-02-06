package dr.achim.code_scanner.presentation.screens.libraries

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.libraryColors
import dr.achim.code_scanner.R
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
            val libraries by produceLibraries(R.raw.aboutlibraries)
            LibrariesContainer(
                libraries = libraries,
                modifier = Modifier.fillMaxSize(),
                colors = LibraryDefaults.libraryColors(
                    libraryBackgroundColor = AppTheme.colorScheme.background,
                    libraryContentColor = contentColorFor(backgroundColor = AppTheme.colorScheme.background),
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