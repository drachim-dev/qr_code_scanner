package dr.achim.code_scanner.presentation.navigation

import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.currentStateAsState
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dr.achim.code_scanner.domain.model.AssistAction
import dr.achim.code_scanner.presentation.screens.Screen
import dr.achim.code_scanner.presentation.screens.history.HistoryScreen
import dr.achim.code_scanner.presentation.screens.history.HistoryViewModel
import dr.achim.code_scanner.presentation.screens.home.HomeScreen
import dr.achim.code_scanner.presentation.screens.home.HomeViewModel
import dr.achim.code_scanner.presentation.screens.home.ScanResultCallback
import dr.achim.code_scanner.presentation.screens.libraries.LibrariesScreen
import dr.achim.code_scanner.presentation.screens.onboarding.OnboardingScreen
import dr.achim.code_scanner.presentation.screens.onboarding.OnboardingViewModel

typealias NavigateUp = () -> Unit

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier,
    navViewModel: AppNavigationViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    onClickAction: (AssistAction) -> Unit,
    onScanResult: ScanResultCallback,
) {
    val hasSeenOnboarding by navViewModel.getHasSeenOnboardingState.collectAsStateWithLifecycle()

    val defaultDestination = Screen.Home.route
    val startDestination = if (hasSeenOnboarding) defaultDestination else Screen.Onboarding.route

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = Screen.valueOf(backStackEntry?.destination?.route ?: defaultDestination)

    // Prevents quick double taps on back arrow
    val canNavigateBack = navController.previousBackStackEntry != null &&
            backStackEntry?.lifecycle?.currentStateAsState()?.value?.isAtLeast(Lifecycle.State.STARTED) == true
    val navigateUp: NavigateUp? = if (canNavigateBack) {
        { navController.navigateUp() }
    } else null

    NavHost(navController, startDestination, modifier) {
        composable(route = Screen.Onboarding.route) {
            val viewModel = hiltViewModel<OnboardingViewModel>()
            val autoStartCamera by viewModel.autoStartCameraState.collectAsStateWithLifecycle()

            OnboardingScreen(
                currentScreen = currentScreen,
                autoStartCameraEnabled = autoStartCamera,
                onAutoStartCameraChange = viewModel::setAutoStartCamera,
                setHasSeenOnboarding = viewModel::setHasSeenOnboarding,
                onNavigateToHome = navController::navigateToHome,
            )
        }

        composable(route = Screen.Home.route) {
            val viewModel = hiltViewModel<HomeViewModel, HomeViewModel.Factory> { factory ->
                factory.create(onScanResult)
            }

            val viewState by viewModel.viewState.collectAsStateWithLifecycle()
            val autoStartCamera by viewModel.autoStartCameraState.collectAsStateWithLifecycle()
            val showSupportHint by viewModel.showSupportHintState.collectAsStateWithLifecycle()

            HomeScreen(
                viewState = viewState,
                autoStartCameraEnabled = autoStartCamera,
                onAutoStartCameraChange = viewModel::setAutoStartCamera,
                showSupportHint = showSupportHint,
                onDismissSupportHint = { viewModel.setShowSupportHint(false) },
                onClickAction = onClickAction,
                onNavigateToHistory = navController::navigateToHistory,
                onNavigateToLibraries = navController::navigateToLibraries,
                currentScreen = currentScreen,
            )
        }

        composable(route = Screen.History.route) {
            val viewModel = hiltViewModel<HistoryViewModel>()
            val viewState by viewModel.viewState.collectAsStateWithLifecycle()

            HistoryScreen(
                currentScreen = currentScreen,
                navigateUp = navigateUp,
                viewState = viewState,
                onClickAction = onClickAction
            )
        }

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
}

fun NavController.navigateToHistory(navOptions: NavOptions? = null) {
    navigate(Screen.History.route, navOptions)
}

fun NavController.navigateToLibraries(navOptions: NavOptions? = null) {
    navigate(Screen.Libraries.route, navOptions)
}

fun NavController.navigateToHome() {
    navigate(Screen.Home.route) {
        // clear backstack
        popUpTo(graph.id)
    }
}