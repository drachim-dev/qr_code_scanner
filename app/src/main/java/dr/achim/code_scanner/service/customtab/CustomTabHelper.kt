package dr.achim.code_scanner.service.customtab

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import dr.achim.code_scanner.service.customtab.internal.CustomTabsHelper
import dr.achim.code_scanner.service.customtab.internal.ServiceConnection
import dr.achim.code_scanner.service.customtab.internal.ServiceConnectionCallback


/**
 * This is a helper class to manage the connection to the Custom Tabs Service.
 */
class CustomTabHelper(
    private val context: Context,
    lifecycle: Lifecycle,
    private val callback: CustomTabsCallback? = null,
) : ServiceConnectionCallback, DefaultLifecycleObserver {

    private var client: CustomTabsClient? = null
    private var connection: CustomTabsServiceConnection? = null

    /**
     * Creates or retrieves an exiting CustomTabsSession.
     *
     * @return a CustomTabsSession.
     */
    private var customTabsSession: CustomTabsSession? = null
        get() {
            if (client == null) {
                customTabsSession = null
            } else if (field == null) {
                customTabsSession = client?.newSession(callback)
            }
            return field
        }

    init {
        lifecycle.addObserver(this)
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)

        Log.d(this::class.java.simpleName, "onPause called")

        // Unbinds the Activity from the Custom Tabs Service.
        connection?.let {
            context.unbindService(it)
            client = null
            customTabsSession = null
            connection = null
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        Log.d(this::class.java.simpleName, "onResume called")

        // Binds the Activity to the Custom Tabs Service.
        if (client == null) {
            val packageName = CustomTabsHelper.getPackageNameToUse(context) ?: return

            connection = ServiceConnection(this).also {
                CustomTabsClient.bindCustomTabsService(context, packageName, it)
            }
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)

        Log.d(this::class.java.simpleName, "onDestroy called")
    }

    override fun onServiceConnected(client: CustomTabsClient) {
        this.client = client.also { it.warmup(0L) }
    }

    override fun onServiceDisconnected() {
        client = null
        customTabsSession = null
    }

    /**
     * @see {@link CustomTabsSession.mayLaunchUrl
     * @return true if call to mayLaunchUrl was accepted.
     */
    fun mayLaunchUrl(
        uri: Uri,
        extras: Bundle? = null,
        otherLikelyBundles: List<Bundle>? = null
    ): Boolean {
        return customTabsSession?.mayLaunchUrl(uri, extras, otherLikelyBundles) == true
    }

    fun getLaunchUrlIntent(uri: Uri): Intent {
        val customTabsIntent = CustomTabsIntent.Builder(customTabsSession)
            .setSendToExternalDefaultHandlerEnabled(true)
            .build()

        return customTabsIntent.intent.apply { data = uri }
    }
}
