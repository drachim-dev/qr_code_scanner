package dr.achim.code_scanner.di

import android.app.Application
import android.content.Context
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
object AppModule {

    @ViewModelScoped
    @Provides
    fun provideContext(application: Application): Context =
        application.applicationContext

    @ViewModelScoped
    @Provides
    fun provideBarCodeOptions() =
        GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .enableAutoZoom()
            .build()

    @ViewModelScoped
    @Provides
    fun provideBarCodeScanner(
        context: Context,
        options: GmsBarcodeScannerOptions
    ) = GmsBarcodeScanning.getClient(context, options)
}