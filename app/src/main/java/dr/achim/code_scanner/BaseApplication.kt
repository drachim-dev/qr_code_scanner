package dr.achim.code_scanner

import android.app.Application
import com.revenuecat.purchases.LogLevel
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesConfiguration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // revenue cat
        Purchases.apply {
            logLevel = LogLevel.DEBUG
            configure(
                PurchasesConfiguration.Builder(
                    this@BaseApplication,
                    BuildConfig.REVENUECAT_KEY
                ).build()
            )
        }
    }
}