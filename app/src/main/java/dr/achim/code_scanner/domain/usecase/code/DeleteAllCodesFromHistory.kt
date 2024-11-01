package dr.achim.code_scanner.domain.usecase.code

import dr.achim.code_scanner.domain.repo.CodeRepository

class DeleteAllCodesFromHistory(private val repository: CodeRepository) {
    suspend operator fun invoke() = repository.deleteAll()
}