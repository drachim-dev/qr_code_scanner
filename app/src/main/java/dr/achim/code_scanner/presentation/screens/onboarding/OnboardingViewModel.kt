package dr.achim.code_scanner.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dr.achim.code_scanner.domain.usecase.autostartmode.SetAutoStartCamera
import dr.achim.code_scanner.domain.usecase.onboarding.SetHasSeenOnboarding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val setAutoStartCameraUseCase: SetAutoStartCamera,
    private val setHasSeenOnboarding: SetHasSeenOnboarding,
) : ViewModel() {

    private val _autoStartCameraState = MutableStateFlow(false)
    val autoStartCameraState = _autoStartCameraState.asStateFlow()

    fun setAutoStartCamera(value: Boolean) {
        setAutoStartCameraUseCase(value)
        _autoStartCameraState.value = value
    }

    fun setHasSeenOnboarding() = setHasSeenOnboarding(true)
}