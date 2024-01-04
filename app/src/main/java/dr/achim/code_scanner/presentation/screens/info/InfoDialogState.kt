package dr.achim.code_scanner.presentation.screens.info

import android.app.Activity
import com.revenuecat.purchases.models.StoreProduct

data class InfoDialogState(
    val loading: Boolean,
    val productList: List<StoreProduct>,
    val onBuyProduct: (activity: Activity, product: StoreProduct) -> Unit
)