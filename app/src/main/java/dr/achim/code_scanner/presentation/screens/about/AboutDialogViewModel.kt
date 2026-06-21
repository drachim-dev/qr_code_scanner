package dr.achim.code_scanner.presentation.screens.about

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revenuecat.purchases.ProductType
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.getProductsWith
import com.revenuecat.purchases.models.StoreProduct
import com.revenuecat.purchases.purchaseWith
import dr.achim.code_scanner.common.Product
import dr.achim.code_scanner.common.TAG
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AboutDialogViewModel : ViewModel() {

    private val _viewState = MutableStateFlow(AboutDialogState(true, emptyList(), ::purchaseProduct))
    val viewState = _viewState.asStateFlow()

    private val eventChannel = Channel<PurchaseEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        val productIdentifiers = Product.entries.map { it.identifier }
        Purchases.sharedInstance.getProductsWith(
            productIds = productIdentifiers,
            type = ProductType.INAPP,
            onError = { error ->
                updateProductList(emptyList())
                Log.e(TAG, "Code ${error.code}: ${error.message}")
            }
        ) { storeProducts ->
            updateProductList(storeProducts)
        }
    }

    private fun updateProductList(products: List<StoreProduct>) {
        _viewState.value = AboutDialogState(false, products, ::purchaseProduct)
    }

    private fun purchaseProduct(activity: Activity, product: StoreProduct) {
        Purchases.sharedInstance.purchaseWith(
            PurchaseParams.Builder(activity, product).build(),
            onError = { error, _ ->
                Log.e(TAG, "Code ${error.code}: ${error.message}")
                sendEvent(PurchaseEvent.PurchaseAborted)
            },
            onSuccess = { _, _ ->
                sendEvent(PurchaseEvent.PurchaseComplete)
            }
        )
    }

    private fun sendEvent(event: PurchaseEvent) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
}