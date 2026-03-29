package dr.achim.code_scanner.service

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.coroutines.launch

private const val TAG = "InAppReviewService"
private const val kMinUsageReviewCount = 10

class InAppReviewService(
    private val context: Context,
    private val lifecycle: Lifecycle,
) : DefaultLifecycleObserver {

    private val reviewManager by lazy { ReviewManagerFactory.create(context) }
    private var reviewInfo: ReviewInfo? = null

    private var usageCount = 10

    init {
        lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)

        // Prepare in app review if not prepared
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            if (shouldPrepareReview() && reviewInfo == null) {
                prepareReviewInfo()
            }
        }
    }

    private fun shouldPrepareReview() = usageCount == kMinUsageReviewCount

    private fun prepareReviewInfo() {
        Log.d(TAG, "prepareReviewInfo called")
        lifecycle.coroutineScope.launch {
            try {
                reviewInfo = reviewManager.requestReview()
                Log.d(TAG, "ReviewInfo prepared")
            } catch (e: Exception) {
                Log.e(TAG, "Could not prepare In-App Review", e)
                reviewInfo = null
            }
        }
    }

    fun mayShowReview(activity: Activity) {
        reviewInfo?.let { info ->
            val flow = reviewManager.launchReviewFlow(activity, info)
            flow.addOnCompleteListener { _ ->
                // The flow has finished. The API does not indicate whether the user
                // reviewed or not, or even whether the review dialog was shown. Thus, no
                // matter the result, we continue our app flow.
                reviewInfo = null
                usageCount++
            }
        }
    }
}