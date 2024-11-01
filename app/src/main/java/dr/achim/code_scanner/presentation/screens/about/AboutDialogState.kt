package dr.achim.code_scanner.presentation.screens.about

import android.app.Activity
import com.revenuecat.purchases.models.StoreProduct

data class AboutDialogState(
    val loading: Boolean,
    val productList: List<StoreProduct>,
    val onPurchase: (activity: Activity, product: StoreProduct) -> Unit
)