package dr.achim.code_scanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.EngagementSignalsCallback
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dr.achim.code_scanner.domain.model.Code
import dr.achim.code_scanner.presentation.navigation.Navigation
import dr.achim.code_scanner.presentation.theme.AppTheme
import dr.achim.code_scanner.service.InAppReviewService
import dr.achim.code_scanner.service.customtab.CustomTabHelper

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
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Navigation(
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