package dr.achim.code_scanner.presentation.screens.libraries

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.android.produceLibraries
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
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
        val libraries by produceLibraries(R.raw.aboutlibraries)
        LibrariesContainer(
            libraries = libraries,
            contentPadding = innerPadding,
            modifier = Modifier.fillMaxSize(),
        )
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