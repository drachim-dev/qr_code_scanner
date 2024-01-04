package dr.achim.code_scanner.domain.repo

import dr.achim.code_scanner.domain.model.ContentType
import kotlinx.coroutines.flow.Flow

interface MainRepo {
    fun startScanning() : Flow<ContentType?>
}