package dr.achim.code_scanner.presentation.screens.info

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.ProductType
import com.revenuecat.purchases.PurchaseParams
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.GetStoreProductsCallback
import com.revenuecat.purchases.interfaces.PurchaseCallback
import com.revenuecat.purchases.models.StoreProduct
import com.revenuecat.purchases.models.StoreTransaction
import dagger.hilt.android.lifecycle.HiltViewModel
import dr.achim.code_scanner.common.Products
import dr.achim.code_scanner.common.TAG
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InfoDialogViewModel @Inject constructor() : ViewModel() {

    private val _viewState = MutableStateFlow(InfoDialogState(true, emptyList(), ::buyProduct))
    val viewState = _viewState.asStateFlow()

    private val eventChannel = Channel<PurchaseEvent>(Channel.BUFFERED)
    val eventsFlow = eventChannel.receiveAsFlow()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        val productList = Products.entries.map { it.identifier }
        Purchases.sharedInstance.getProducts(
            productList,
            ProductType.INAPP,
            object : GetStoreProductsCallback {
                override fun onError(error: PurchasesError) {
                    updateProductList(emptyList())
                    Log.e(TAG, "Code ${error.code}: ${error.message}")
                }

                override fun onReceived(storeProducts: List<StoreProduct>) {
                    updateProductList(storeProducts)
                }
            })
    }

    private fun updateProductList(products: List<StoreProduct>) {
        _viewState.value = InfoDialogState(false, products, ::buyProduct)
    }

    private fun buyProduct(activity: Activity, product: StoreProduct) {
        Purchases.sharedInstance.purchase(
            PurchaseParams.Builder(activity, product).build(),
            object : PurchaseCallback {
                override fun onError(error: PurchasesError, userCancelled: Boolean) {
                    Log.e(TAG, "Code ${error.code}: ${error.message}")
                    sendEvent(PurchaseEvent.PurchaseAborted)
                }

                override fun onCompleted(
                    storeTransaction: StoreTransaction,
                    customerInfo: CustomerInfo
                ) {
                    sendEvent(PurchaseEvent.PurchaseComplete)
                }
            })
    }

    fun sendEvent(event: PurchaseEvent) {
        viewModelScope.launch {
            eventChannel.send(event)
        }
    }
}