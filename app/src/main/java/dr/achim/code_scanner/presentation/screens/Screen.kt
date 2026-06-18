package dr.achim.code_scanner.presentation.screens

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import dr.achim.code_scanner.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Screen(@StringRes private val titleResId: Int) : NavKey {
    val title @Composable get() = stringResource(titleResId)

    @Serializable
    data object Onboarding : Screen(R.string.screen_onboarding)

    @Serializable
    data object Home : Screen(R.string.app_name)

    @Serializable
    data object History : Screen(R.string.screen_history)

    @Serializable
    data object Libraries : Screen(R.string.screen_libraries)
}
