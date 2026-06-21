package dr.achim.code_scanner

import android.app.Application
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import dr.achim.code_scanner.di.appModule
import dr.achim.code_scanner.di.dataModule
import dr.achim.code_scanner.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.DEBUG else Level.NONE)
            androidContext(this@MainApplication)
            modules(appModule, dataModule, domainModule)
        }

        // revenue cat
        Purchases.apply {
            logLevel = if (BuildConfig.DEBUG) LogLevel.DEBUG else LogLevel.ERROR
            configure(
                PurchasesConfiguration.Builder(
                    this@MainApplication,
                    BuildConfig.REVENUECAT_KEY
                ).build()
            )
        }
    }
}