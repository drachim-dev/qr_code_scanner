package dr.achim.code_scanner.domain.usecase.onboarding

import dr.achim.code_scanner.domain.repo.SettingsRepository

class SetHasSeenOnboarding(private val repository: SettingsRepository) {
    operator fun invoke(value: Boolean) {
        repository.setHasSeenOnboarding(value)
    }
}