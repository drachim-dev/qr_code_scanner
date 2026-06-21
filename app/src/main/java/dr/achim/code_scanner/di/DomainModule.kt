package dr.achim.code_scanner.di

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
import org.koin.dsl.module
import org.koin.plugin.module.dsl.factory

val domainModule = module {
    factory<GetAutoStartCamera>()
    factory<SetAutoStartCamera>()
    factory<AddCodeToHistory>()
    factory<DeleteAllCodesFromHistory>()
    factory<DeleteCodesFromHistory>()
    factory<GetCodesFromHistory>()
    factory<StartScanning>()
    factory<GetHasSeenOnboarding>()
    factory<SetHasSeenOnboarding>()
    factory<GetShowSupportHint>()
    factory<SetShowSupportHint>()
}