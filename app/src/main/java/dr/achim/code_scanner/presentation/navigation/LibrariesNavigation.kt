package dr.achim.code_scanner.presentation.navigation

import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dr.achim.code_scanner.presentation.screens.Screen
import dr.achim.code_scanner.presentation.screens.libraries.LibrariesScreen

// Adds libraries screen to `this` NavGraphBuilder
fun NavGraphBuilder.librariesScreen(currentScreen: Screen, navigateUp: NavigateUp?) {
    composable(
        route = Screen.Libraries.route,
        enterTransition = {
            slideInVertically(initialOffsetY = { it / 10 })
        },
        popExitTransition = {
            slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
        }
    ) {
        LibrariesScreen(currentScreen, navigateUp)
    }
}

fun NavController.navigateToLibraries(navOptions: NavOptions? = null) {
    navigate(Screen.Libraries.route, navOptions)
}