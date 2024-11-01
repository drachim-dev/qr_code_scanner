package dr.achim.code_scanner.data.repo

import dr.achim.code_scanner.data.dao.CodeDao
import dr.achim.code_scanner.data.entity.CodeEntity
import dr.achim.code_scanner.data.mapper.toEntity
import dr.achim.code_scanner.data.mapper.toModel
import dr.achim.code_scanner.data.source.ImageStreamSource
import dr.achim.code_scanner.domain.model.Code
import dr.achim.code_scanner.domain.repo.CodeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CodeRepositoryImpl @Inject constructor(
    private val imageStreamSource: ImageStreamSource,
    private val codeDao: CodeDao
) : CodeRepository {
    override fun startScanning(): Flow<Code?> {
        return imageStreamSource.startScanning()
    }

    override fun getAllCodes(): Flow<List<Code>> {
        return codeDao.getAllCodes().map {
            it.map(CodeEntity::toModel)
        }
    }

    override suspend fun insert(code: Code) {
        codeDao.insert(code.toEntity())
    }

    override suspend fun delete(codes: List<Code>) {
        codeDao.deleteById(
            *codes.map { it.id }.toTypedArray()
        )
    }

    override suspend fun deleteAll() {
        codeDao.deleteAll()
    }
}