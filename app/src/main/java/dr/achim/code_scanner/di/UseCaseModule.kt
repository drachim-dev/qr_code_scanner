package dr.achim.code_scanner.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dr.achim.code_scanner.domain.repo.CodeRepository
import dr.achim.code_scanner.domain.repo.SettingsRepository
import dr.achim.code_scanner.domain.usecase.autostartmode.GetAutoStartCamera
import dr.achim.code_scanner.domain.usecase.autostartmode.SetAutoStartCamera
import dr.achim.code_scanner.domain.usecase.code.AddCodeToHistory
import dr.achim.code_scanner.domain.usecase.code.DeleteAllCodesFromHistory
import dr.achim.code_scanner.domain.usecase.code.DeleteCodesFromHistory
import dr.achim.code_scanner.domain.usecase.code.GetCodesFromHistory
import dr.achim.code_scanner.domain.usecase.code.StartScanning
import dr.achim.code_scanner.domain.usecase.onboarding.GetHasSeenOnboarding
import dr.achim.code_scanner.domain.usecase.onboarding.SetHasSeenOnboarding
import dr.achim.code_scanner.domain.usecase.supporthint.GetShowSupportHint
import dr.achim.code_scanner.domain.usecase.supporthint.SetShowSupportHint

@Module
@InstallIn(ActivityRetainedComponent::class)
object UseCaseModule {

    @Provides
    fun providesGetHasSeenOnboarding(repository: SettingsRepository) =
        GetHasSeenOnboarding(repository)

    @Provides
    fun providesSetHasSeenOnboarding(repository: SettingsRepository) =
        SetHasSeenOnboarding(repository)

    @Provides
    fun providesStartScanning(repository: CodeRepository) =
        StartScanning(repository)

    @Provides
    fun providesGetAutoStartCamera(repository: SettingsRepository) =
        GetAutoStartCamera(repository)

    @Provides
    fun providesSetAutoStartCamera(repository: SettingsRepository) =
        SetAutoStartCamera(repository)

    @Provides
    fun providesGetShowSupportHint(repository: SettingsRepository) =
        GetShowSupportHint(repository)

    @Provides
    fun providesSetShowSupportHint(repository: SettingsRepository) =
        SetShowSupportHint(repository)

    @Provides
    fun providesGetCodesFromHistory(repository: CodeRepository) =
        GetCodesFromHistory(repository)

    @Provides
    fun providesAddCodeToHistory(repository: CodeRepository) =
        AddCodeToHistory(repository)

    @Provides
    fun providesDeleteCodeFromHistory(repository: CodeRepository) =
        DeleteCodesFromHistory(repository)

    @Provides
    fun providesDeleteAllCodesFromHistory(repository: CodeRepository) =
        DeleteAllCodesFromHistory(repository)
}