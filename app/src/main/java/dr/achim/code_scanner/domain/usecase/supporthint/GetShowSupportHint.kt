package dr.achim.code_scanner.domain.usecase.supporthint

import dr.achim.code_scanner.domain.repo.SettingsRepository

class GetShowSupportHint(private val repository: SettingsRepository) {
    operator fun invoke() = repository.getShowSupportHint()
}