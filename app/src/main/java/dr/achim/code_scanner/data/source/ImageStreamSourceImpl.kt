package dr.achim.code_scanner.data.source

import android.util.Log
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import dr.achim.code_scanner.data.mapper.toModel
import dr.achim.code_scanner.domain.model.Code
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageStreamSourceImpl @Inject constructor(private val scanner: GmsBarcodeScanner) :
    ImageStreamSource {

    override fun startScanning(): Flow<Code?> {
        return callbackFlow {
            scanner.startScan()
                .addOnSuccessListener { barcode ->
                    launch {
                        send(barcode.toModel())
                    }
                }
                .addOnFailureListener {
                    Log.e(
                        this@ImageStreamSourceImpl.javaClass.simpleName,
                        it.localizedMessage ?: it.toString()
                    )
                }

            awaitClose { }
        }
    }
}