package dr.achim.code_scanner

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.EngagementSignalsCallback
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import dr.achim.code_scanner.domain.model.Code
import dr.achim.code_scanner.presentation.navigation.AppNavigation
import dr.achim.code_scanner.presentation.theme.AppTheme
import dr.achim.code_scanner.service.InAppReviewService
import dr.achim.code_scanner.service.customtab.CustomTabHelper

@AndroidEntryPoint
class MainActivity : ComponentActivity(), EngagementSignalsCallback {

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            // Called when user is finished with action
            if (result.resultCode == RESULT_CANCELED) {
                inAppReviewService.mayShowReview(this)
            }
        }
    private val actionHandler by lazy { ActionHandler(this, resultLauncher) }
    private val customTabHelper = CustomTabHelper(this, lifecycle)
    private val inAppReviewService = InAppReviewService(this, lifecycle)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )

        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                AppNavigation(
                    onClickAction = {
                        actionHandler.handleAction(
                            customTabHelper = customTabHelper,
                            assistAction = it
                        )
                    },
                    onScanResult = { code ->
                        if (code is Code.Url) {
                            customTabHelper.mayLaunchUrl(code.uri)
                        }
                    },
                )
            }
        }
    }
}