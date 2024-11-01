package dr.achim.code_scanner.presentation.screens.home

import dr.achim.code_scanner.domain.model.Code

data class HomeScreenState(val code: Code?, val startScanning: () -> Unit)

typealias ScanResultCallback = (Code) -> Unit