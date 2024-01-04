package dr.achim.code_scanner.data.repo

import android.util.Log
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import dr.achim.code_scanner.domain.model.ContentType
import dr.achim.code_scanner.domain.repo.MainRepo
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainRepoImpl @Inject constructor(private val scanner: GmsBarcodeScanner) : MainRepo {
    override fun startScanning(): Flow<ContentType?> {
        return callbackFlow {

            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    launch {
                        send(ContentType.parse(barcode))
                    }
                }
                .addOnFailureListener {
                    Log.e(
                        this@MainRepoImpl.javaClass.simpleName,
                        it.localizedMessage ?: it.toString()
                    )
                }

            awaitClose { }
        }
    }
}