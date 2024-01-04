package dr.achim.code_scanner.presentation.navigation

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import dr.achim.code_scanner.domain.model.ContentType
import dr.achim.code_scanner.presentation.screens.Screen
import dr.achim.code_scanner.presentation.screens.home.HomeScreen
import dr.achim.code_scanner.presentation.screens.home.HomeViewModel

// Adds home screen to `this` NavGraphBuilder
fun NavGraphBuilder.homeScreen(
    onClickAction: (ContentType.AssistAction) -> Unit,
    onScanResult: (ContentType) -> Unit,
    // Navigation events are exposed to the caller to be handled at a higher level
    onNavigateToLibraries: () -> Unit
) {
    composable(Screen.Home.route) {
        val viewModel: HomeViewModel = hiltViewModel()
        val viewState by viewModel.viewState.collectAsStateWithLifecycle()
        HomeScreen(
            viewState = viewState,
            onClickAction = onClickAction,
            onScanResult = onScanResult,
            onNavigateToLibraries = onNavigateToLibraries
        )
    }
}

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    this.navigate(Screen.Home.route, navOptions)
}