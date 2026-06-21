package dr.achim.code_scanner.presentation.navigation

import androidx.lifecycle.ViewModel
import dr.achim.code_scanner.domain.usecase.onboarding.GetHasSeenOnboarding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppNavigationViewModel(getHasSeenOnboarding: GetHasSeenOnboarding) :
    ViewModel() {
    private val _getHasSeenOnboardingState = MutableStateFlow(getHasSeenOnboarding())
    val getHasSeenOnboardingState = _getHasSeenOnboardingState.asStateFlow()
}