package dr.achim.code_scanner.domain.usecase.code

import dr.achim.code_scanner.domain.model.Code
import dr.achim.code_scanner.domain.repo.CodeRepository

class AddCodeToHistory(private val repository: CodeRepository) {
    suspend operator fun invoke(code: Code) {
        if (!code.isSensitive()) {
            repository.insert(code)
        }
    }
}