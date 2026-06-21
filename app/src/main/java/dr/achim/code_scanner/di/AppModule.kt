package dr.achim.code_scanner.di

import android.content.Context
import android.content.SharedPreferences
import dr.achim.code_scanner.common.DateTimeFormatter
import dr.achim.code_scanner.presentation.navigation.AppNavigationViewModel
import dr.achim.code_scanner.presentation.screens.about.AboutDialogViewModel
import dr.achim.code_scanner.presentation.screens.history.HistoryViewModel
import dr.achim.code_scanner.presentation.screens.home.HomeViewModel
import dr.achim.code_scanner.presentation.screens.onboarding.OnboardingViewModel
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single
import org.koin.plugin.module.dsl.viewModel

val appModule = module {
    single<SharedPreferences> { create(::provideSharedPreferences) }

    single<DateTimeFormatter>()

    viewModel<AppNavigationViewModel>()
    viewModel<AboutDialogViewModel>()
    viewModel<HistoryViewModel>()
    viewModel<HomeViewModel>()
    viewModel<OnboardingViewModel>()
}

private fun provideSharedPreferences(context: Context): SharedPreferences =
    context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)