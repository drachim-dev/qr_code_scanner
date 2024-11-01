package dr.achim.code_scanner.domain.repo

import dr.achim.code_scanner.domain.model.Code
import kotlinx.coroutines.flow.Flow

interface CodeRepository {
    fun startScanning(): Flow<Code?>
    fun getAllCodes(): Flow<List<Code>>
    suspend fun insert(code: Code)
    suspend fun delete(codes: List<Code>)
    suspend fun deleteAll()
}