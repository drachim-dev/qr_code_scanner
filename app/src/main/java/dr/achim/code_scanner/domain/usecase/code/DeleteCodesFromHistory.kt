package dr.achim.code_scanner.domain.usecase.code

import dr.achim.code_scanner.domain.model.Code
import dr.achim.code_scanner.domain.repo.CodeRepository

class DeleteCodesFromHistory(private val repository: CodeRepository) {
    suspend operator fun invoke(codes: List<Code>) = repository.delete(codes)
}