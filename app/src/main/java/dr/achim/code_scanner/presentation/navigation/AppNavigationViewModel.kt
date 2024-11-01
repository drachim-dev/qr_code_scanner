package dr.achim.code_scanner.presentation.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dr.achim.code_scanner.domain.usecase.onboarding.GetHasSeenOnboarding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AppNavigationViewModel @Inject constructor(getHasSeenOnboarding: GetHasSeenOnboarding) :
    ViewModel() {
    private val _getHasSeenOnboardingState = MutableStateFlow(getHasSeenOnboarding())
    val getHasSeenOnboardingState = _getHasSeenOnboardingState.asStateFlow()
}