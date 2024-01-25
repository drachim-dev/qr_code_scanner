package dr.achim.code_scanner.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dr.achim.code_scanner.domain.model.ContentType
import dr.achim.code_scanner.presentation.screens.Screen

typealias NavigateUp = (() -> Unit)
@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onClickAction: (ContentType.AssistAction) -> Unit,
    onScanResult: (ContentType) -> Unit,
) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(backStackEntry?.destination?.route ?: Screen.Home.route)

    val canNavigateBack = navController.previousBackStackEntry != null
    val navigateUp: NavigateUp? = if (canNavigateBack) {
        {
            navController.navigateUp()
        }
    } else {
        null
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        homeScreen(
            onClickAction = onClickAction,
            onScanResult = onScanResult,
            onNavigateToLibraries = {
                navController.navigateToLibraries()
            },
            currentScreen,
        )
        librariesScreen(
            currentScreen,
            navigateUp,
        )
    }
}