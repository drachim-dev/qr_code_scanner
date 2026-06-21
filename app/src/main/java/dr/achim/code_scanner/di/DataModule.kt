package dr.achim.code_scanner.di

import android.content.Context
import androidx.room.Room
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import dr.achim.code_scanner.data.dao.CodeDao
import dr.achim.code_scanner.data.database.CodeDatabase
import dr.achim.code_scanner.data.repo.CodeRepositoryImpl
import dr.achim.code_scanner.data.repo.SettingsRepositoryImpl
import dr.achim.code_scanner.data.source.ImageStreamSource
import dr.achim.code_scanner.data.source.ImageStreamSourceImpl
import dr.achim.code_scanner.data.source.SettingsSource
import dr.achim.code_scanner.data.source.SettingsSourceImpl
import dr.achim.code_scanner.domain.repo.CodeRepository
import dr.achim.code_scanner.domain.repo.SettingsRepository
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.plugin.module.dsl.create
import org.koin.plugin.module.dsl.single

val dataModule = module {
    single<ImageStreamSourceImpl>() bind ImageStreamSource::class
    single<SettingsSourceImpl>() bind SettingsSource::class
    single<CodeRepositoryImpl>() bind CodeRepository::class
    single<SettingsRepositoryImpl>() bind SettingsRepository::class

    single<CodeDatabase> { create(::provideDatabase) }
    single<CodeDao> { create(::provideCodeDao) }
    single<GmsBarcodeScanner> { create(::provideGmsBarcodeScanner) }
}

private fun provideDatabase(context: Context): CodeDatabase =
    Room.databaseBuilder(
        context = context,
        klass = CodeDatabase::class.java,
        name = "AppDatabase"
    ).fallbackToDestructiveMigration(false).build()

private fun provideCodeDao(database: CodeDatabase): CodeDao = database.dao

private fun provideGmsBarcodeScanner(context: Context): GmsBarcodeScanner {
    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .enableAutoZoom()
        .build()

    return GmsBarcodeScanning.getClient(context, options)
}
