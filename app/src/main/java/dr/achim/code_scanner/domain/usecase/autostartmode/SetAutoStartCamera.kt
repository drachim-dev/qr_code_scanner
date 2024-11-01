package dr.achim.code_scanner.domain.usecase.autostartmode

import dr.achim.code_scanner.domain.repo.SettingsRepository

class SetAutoStartCamera(private val repository: SettingsRepository) {
    operator fun invoke(value: Boolean) {
        repository.setAutoStartCamera(value)
    }
}