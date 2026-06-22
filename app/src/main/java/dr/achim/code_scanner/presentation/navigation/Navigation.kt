package dr.achim.code_scanner.presentation.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
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
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

typealias NavigateUp = () -> Unit

@Composable
fun Navigation(
    navViewModel: NavigationViewModel = koinViewModel(),
    onClickAction: (AssistAction) -> Unit,
    onScanResult: ScanResultCallback,
) {
    val hasSeenOnboarding by navViewModel.getHasSeenOnboardingState.collectAsStateWithLifecycle()

    val startRoute = if (hasSeenOnboarding) Screen.Home else Screen.Onboarding
    val initialBackStack = listOf(startRoute)
    val backStack = rememberNavBackStack(*initialBackStack.toTypedArray())
    val onBack = dropUnlessResumed { backStack.removeLastOrNull() }

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        predictivePopTransitionSpec = {
            ContentTransform(
                fadeIn(
                    spring(
                        dampingRatio = 1.0f,
                        stiffness = 1600.0f,
                    )
                ),
                fadeOut(),
            )
        },
        entryProvider = entryProvider {
            entry<Screen.Onboarding> {
                val viewModel = koinViewModel<OnboardingViewModel>()
                val autoStartCamera by viewModel.autoStartCameraState.collectAsStateWithLifecycle()

                OnboardingScreen(
                    title = Screen.Onboarding.title,
                    autoStartCameraEnabled = autoStartCamera,
                    onAutoStartCameraChange = viewModel::setAutoStartCamera,
                    setHasSeenOnboarding = viewModel::setHasSeenOnboarding,
                    onNavigateToHome = {
                        backStack.clear()
                        backStack += Screen.Home
                    },
                )
            }

            entry<Screen.Home> {
                val viewModel = koinViewModel<HomeViewModel>(
                    parameters = { parametersOf(onScanResult) }
                )

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
                    onNavigateToHistory = { backStack += Screen.History },
                    onNavigateToLibraries = { backStack += Screen.Libraries },
                    title = Screen.Home.title,
                )
            }

            entry<Screen.History> {
                val viewModel = koinViewModel<HistoryViewModel>()
                val viewState by viewModel.viewState.collectAsStateWithLifecycle()

                HistoryScreen(
                    title = Screen.History.title,
                    navigateUp = onBack,
                    viewState = viewState,
                    onClickAction = onClickAction
                )
            }

            entry<Screen.Libraries> {
                LibrariesScreen(Screen.Libraries, onBack)
            }
        }
    )
}
