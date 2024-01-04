package dr.achim.code_scanner.service.customtab

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.browser.customtabs.EngagementSignalsCallback
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
    private val engagementSignalsCallback: EngagementSignalsCallback? = null,
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
                customTabsSession = createSession()
            }
            return field
        }

    init {
        lifecycle.addObserver(this)
    }

    private fun createSession(): CustomTabsSession? {
        val session = client?.newSession(callback)
        return session?.apply {
            engagementSignalsCallback?.let {
                try {
                    if (isEngagementSignalsApiAvailable(Bundle.EMPTY)) {
                        setEngagementSignalsCallback(engagementSignalsCallback, Bundle.EMPTY)
                    }
                } catch (_: RemoteException) {
                } catch (_: UnsupportedOperationException) {
                }
            }
        }
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
        Log.d(this::class.java.simpleName, "mayLaunchUrl called")

        return customTabsSession?.run {
            mayLaunchUrl(uri, extras, otherLikelyBundles)
        } ?: false
    }

    fun launchUrl(uri: Uri): Boolean {

        // Custom Tabs are not supported by any browser on the device
        CustomTabsClient.getPackageName(context, emptyList())
            ?: return false

        CustomTabsIntent.Builder(customTabsSession)
            .build()
            .launchUrl(context, uri)

        return true
    }
}
