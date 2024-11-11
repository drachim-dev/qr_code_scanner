package dr.achim.code_scanner.presentation.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import dr.achim.code_scanner.R
import dr.achim.code_scanner.common.DefaultPreview
import dr.achim.code_scanner.common.Dimens
import dr.achim.code_scanner.common.DynamicPreview
import dr.achim.code_scanner.presentation.components.CheckCard
import dr.achim.code_scanner.presentation.screens.Screen
import dr.achim.code_scanner.presentation.theme.AppTheme

@Composable
fun OnboardingScreen(
    currentScreen: Screen,
    autoStartCameraEnabled: Boolean,
    onAutoStartCameraChange: (Boolean) -> Unit,
    setHasSeenOnboarding: () -> Unit,
    onNavigateToHome: () -> Unit,
) {

    var autoStartCameraSelected by remember {
        mutableStateOf(autoStartCameraEnabled)
    }

    val onAutoStartCameraSelected: (Boolean) -> Unit = {
        autoStartCameraSelected = it
    }

    Surface(color = AppTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .safeContentPadding()
                .padding(AppTheme.spacing.m),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.l)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = AppTheme.spacing.m)
            ) {
                OnboardingHeader(
                    title = currentScreen.title,
                    description = stringResource(R.string.screen_onboarding_subtitle)
                )

                Spacer(modifier = Modifier.height(AppTheme.spacing.l))

                Column(
                    modifier = Modifier.fillMaxWidth(0.95f),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)
                ) {
                    CheckCard(
                        title = { Text(text = stringResource(R.string.screen_onboarding_launch_mode_auto_title)) },
                        description = { Text(text = stringResource(R.string.screen_onboarding_launch_mode_auto_text)) },
                        checked = autoStartCameraSelected,
                        onClick = { onAutoStartCameraSelected(true) }
                    )

                    CheckCard(
                        title = { Text(text = stringResource(R.string.screen_onboarding_launch_mode_manual_title)) },
                        description = { Text(text = stringResource(R.string.screen_onboarding_launch_mode_manual_text)) },
                        checked = !autoStartCameraSelected,
                        onClick = { onAutoStartCameraSelected(false) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            FilledTonalButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onAutoStartCameraChange(autoStartCameraSelected)
                    setHasSeenOnboarding()
                    onNavigateToHome()
                },
            ) {
                Text(text = "Let's go!")
            }
        }
    }
}

@Composable
fun OnboardingHeader(title: String, description: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            AppTheme.spacing.m,
            Alignment.CenterVertically
        ),
    ) {
        Icon(
            imageVector = Icons.Default.CameraAlt,
            contentDescription = null,
            modifier = Modifier
                .height(Dimens.contentImageSize)
                .fillMaxWidth(),
            tint = AppTheme.colorScheme.surfaceVariant
        )

        Text(
            text = title,
            textAlign = TextAlign.Center,
            modifier = Modifier,
            style = AppTheme.typography.titleLarge,
            color = AppTheme.colorScheme.secondary,
        )

        Text(
            text = description,
            textAlign = TextAlign.Center,
            modifier = Modifier,
            style = AppTheme.typography.bodyLarge,
            color = AppTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@DynamicPreview
@DefaultPreview
@Composable
private fun OnboardingScreenContentPreview() {
    AppTheme {
        OnboardingScreen(
            currentScreen = Screen.Onboarding,
            autoStartCameraEnabled = true,
            onAutoStartCameraChange = {},
            setHasSeenOnboarding = {},
            onNavigateToHome = {},
        )
    }
}