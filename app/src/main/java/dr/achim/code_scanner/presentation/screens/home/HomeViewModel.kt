package dr.achim.code_scanner.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dr.achim.code_scanner.domain.model.ContentType
import dr.achim.code_scanner.domain.repo.MainRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: MainRepo) : ViewModel() {
    private val _viewState = MutableStateFlow(buildScanningState())
    val viewState = _viewState.asStateFlow()

    init {
        startScanning()
    }

    private fun startScanning() {
        viewModelScope.launch {
            repo.startScanning().collect { contentType ->
                if (contentType != null) {
                    _viewState.value = buildScanningState(contentType)
                }
            }
        }
    }

    private fun buildScanningState(contentType: ContentType? = null) =
        HomeScreenState(
            contentType = contentType,
            startScanning = ::startScanning
        )
}