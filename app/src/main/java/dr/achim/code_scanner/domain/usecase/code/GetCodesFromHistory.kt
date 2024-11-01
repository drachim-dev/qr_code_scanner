package dr.achim.code_scanner.domain.usecase.code

import dr.achim.code_scanner.domain.repo.CodeRepository

class GetCodesFromHistory(private val repository: CodeRepository) {
    operator fun invoke() = repository.getAllCodes()
}