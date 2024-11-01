package dr.achim.code_scanner.domain.usecase.onboarding

import dr.achim.code_scanner.domain.repo.SettingsRepository

class GetHasSeenOnboarding(private val repository: SettingsRepository) {
    operator fun invoke() = repository.getHasSeenOnboarding()
}