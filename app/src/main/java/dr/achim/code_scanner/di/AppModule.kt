package dr.achim.code_scanner.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dr.achim.code_scanner.common.DateTimeFormatter
import dr.achim.code_scanner.data.database.CodeDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideContext(application: Application): Context =
        application.applicationContext

    @Provides
    fun providePrefs(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)

    @Provides
    fun provideBarCodeOptions() =
        GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .enableAutoZoom()
            .build()

    @Provides
    fun provideBarCodeScanner(
        context: Context,
        options: GmsBarcodeScannerOptions
    ) = GmsBarcodeScanning.getClient(context, options)

    @Provides
    @Singleton
    fun provideDatabase(app: Application): CodeDatabase {
        return Room.databaseBuilder(
            context = app,
            klass = CodeDatabase::class.java,
            name = "AppDatabase"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideDateFormatter(@ApplicationContext context: Context): DateTimeFormatter {
        return DateTimeFormatter(context)
    }
}