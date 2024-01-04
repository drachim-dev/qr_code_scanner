package dr.achim.code_scanner.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dr.achim.code_scanner.domain.model.ContentType
import dr.achim.code_scanner.presentation.screens.Screen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    onClickAction: (ContentType.AssistAction) -> Unit,
    onScanResult: (ContentType) -> Unit,
) {

    val navController = rememberNavController()

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
            }
        )
        librariesScreen()
    }
}