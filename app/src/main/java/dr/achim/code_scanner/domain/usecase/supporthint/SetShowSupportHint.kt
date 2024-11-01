package dr.achim.code_scanner.domain.usecase.supporthint

import dr.achim.code_scanner.domain.repo.SettingsRepository

class SetShowSupportHint(private val repository: SettingsRepository) {
    operator fun invoke(value: Boolean) {
        repository.setShowSupportHint(value)
    }
}