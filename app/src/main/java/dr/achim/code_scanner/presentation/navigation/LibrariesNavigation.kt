package dr.achim.code_scanner.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dr.achim.code_scanner.presentation.screens.Screen
import dr.achim.code_scanner.presentation.screens.libraries.LibrariesScreen

// Adds libraries screen to `this` NavGraphBuilder
fun NavGraphBuilder.librariesScreen() {
    composable(Screen.Libraries.route) {
        LibrariesScreen()
    }
}

fun NavController.navigateToLibraries(navOptions: NavOptions? = null) {
    this.navigate(Screen.Libraries.route, navOptions)
}