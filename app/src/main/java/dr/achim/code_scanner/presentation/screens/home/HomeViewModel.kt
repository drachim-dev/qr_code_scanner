package dr.achim.code_scanner.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dr.achim.code_scanner.domain.model.Code
import dr.achim.code_scanner.domain.usecase.autostartmode.GetAutoStartCamera
import dr.achim.code_scanner.domain.usecase.autostartmode.SetAutoStartCamera
import dr.achim.code_scanner.domain.usecase.code.AddCodeToHistory
import dr.achim.code_scanner.domain.usecase.code.StartScanning
import dr.achim.code_scanner.domain.usecase.supporthint.GetShowSupportHint
import dr.achim.code_scanner.domain.usecase.supporthint.SetShowSupportHint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = HomeViewModel.Factory::class)
class HomeViewModel @AssistedInject constructor(
    @Assisted val onScanResultCallback: ScanResultCallback,
    private val startScanningUseCase: StartScanning,
    private val getAutoStartCamera: GetAutoStartCamera,
    private val setAutoStartCameraUseCase: SetAutoStartCamera,
    private val getShowSupportHintUseCase: GetShowSupportHint,
    private val setShowSupportHintUseCase: SetShowSupportHint,
    private val addCodeToHistory: AddCodeToHistory,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(onScanResultCallback: ScanResultCallback): HomeViewModel
    }

    private val _viewState = MutableStateFlow(buildViewState())
    val viewState = _viewState.asStateFlow()

    private val _autoStartCameraState = MutableStateFlow(false)
    val autoStartCameraState = _autoStartCameraState.asStateFlow()

    private val _showSupportHintState = MutableStateFlow(false)
    val showSupportHintState = _showSupportHintState.asStateFlow()

    init {
        initAutoStartCameraState()
        initShowSupportHintState()

        if (_autoStartCameraState.value) startScanning()
    }

    private fun initAutoStartCameraState() {
        val autoScanEnabled = getAutoStartCamera()
        _autoStartCameraState.value = autoScanEnabled
    }

    private fun initShowSupportHintState() {
        val showSupportHint = getShowSupportHintUseCase()
        _showSupportHintState.value = showSupportHint
    }

    private fun startScanning() {
        viewModelScope.launch {
            startScanningUseCase().collect { code ->
                code?.let {
                    onScanResultCallback(code)
                    _viewState.update {
                        buildViewState(code)
                    }
                    addCodeToHistory(code)
                }
            }
        }
    }

    private fun buildViewState(code: Code? = null) =
        HomeScreenState(code = code, startScanning = ::startScanning)

    fun setAutoStartCamera(value: Boolean) {
        setAutoStartCameraUseCase(value)
        initAutoStartCameraState()
    }

    fun setShowSupportHint(value: Boolean) {
        setShowSupportHintUseCase(value)
        initShowSupportHintState()
    }
}