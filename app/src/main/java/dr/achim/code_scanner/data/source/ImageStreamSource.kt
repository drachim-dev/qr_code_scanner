package dr.achim.code_scanner.data.source

import dr.achim.code_scanner.domain.model.Code
import kotlinx.coroutines.flow.Flow

interface ImageStreamSource {
    fun startScanning(): Flow<Code?>
}