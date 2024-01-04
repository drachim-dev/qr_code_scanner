package dr.achim.code_scanner

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.EngagementSignalsCallback
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import dagger.hilt.android.AndroidEntryPoint
import dr.achim.code_scanner.common.TAG
import dr.achim.code_scanner.domain.model.ContentType
import dr.achim.code_scanner.presentation.navigation.AppNavigation
import dr.achim.code_scanner.presentation.theme.AppTheme
import dr.achim.code_scanner.service.InAppReviewService
import dr.achim.code_scanner.service.customtab.CustomTabHelper

@AndroidEntryPoint
class MainActivity : ComponentActivity(), EngagementSignalsCallback, PurchasesUpdatedListener {

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ -> }
    private val actionHandler by lazy { ActionHandler(this, resultLauncher) }
    private val customTabHelper = CustomTabHelper(this, lifecycle, this)
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

                var contentType by remember { mutableStateOf<ContentType?>(null) }

                // TODO: This shouldn't be passed down here? Must be decoupled from activity
                AppNavigation(
                    onClickAction = {
                        actionHandler.handleAction(
                            customTabHelper = customTabHelper,
                            assistAction = it
                        ) {
                            inAppReviewService.mayShowReview(this)
                        }
                    },
                    onScanResult = {
                        contentType = it
                    },
                )

                // Prepare launch
                LaunchedEffect(contentType) {
                    contentType?.run {
                        when (this) {
                            is ContentType.Url -> customTabHelper.mayLaunchUrl(uri)
                            else -> {} // Do nothing
                        }
                    }
                }
            }
        }
    }

    /**
     * Called when the ChromeCustomTab session has ended.
     */
    override fun onSessionEnded(didUserInteract: Boolean, extras: Bundle) {
        Log.d(TAG, "onSessionEnded")
        super.onSessionEnded(didUserInteract, extras)

        inAppReviewService.mayShowReview(this)
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchaseList: MutableList<Purchase>?
    ) {
        Log.d(TAG, "onPurchasesUpdated")
    }
}