package dr.achim.code_scanner.presentation.screens.home

import dr.achim.code_scanner.domain.model.ContentType

data class HomeScreenState(val contentType: ContentType?, val startScanning: () -> Unit)
