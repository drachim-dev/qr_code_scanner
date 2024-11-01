package dr.achim.code_scanner.domain.usecase.autostartmode

import dr.achim.code_scanner.domain.repo.SettingsRepository

class GetAutoStartCamera(private val repository: SettingsRepository) {
    operator fun invoke() = repository.getAutoStartCamera()
}