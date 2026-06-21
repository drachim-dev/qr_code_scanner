package dr.achim.code_scanner.data.repo

import dr.achim.code_scanner.data.source.SettingsSource
import dr.achim.code_scanner.domain.repo.SettingsRepository
class SettingsRepositoryImpl(
    private val settingsSource: SettingsSource
) : SettingsRepository, SettingsSource by settingsSource