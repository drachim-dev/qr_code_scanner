package dr.achim.code_scanner.presentation.screens

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dr.achim.code_scanner.R

enum class Screen(@StringRes private val titleResId: Int) {
    Onboarding(R.string.screen_onboarding),
    Home(R.string.app_name),
    History(R.string.screen_history),
    HistoryDetail(R.string.screen_history),
    Libraries(R.string.screen_libraries),
    ;

    val route get() = name
    val title @Composable get() = stringResource(titleResId)
}