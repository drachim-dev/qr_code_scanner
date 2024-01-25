package dr.achim.code_scanner

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.google.mlkit.vision.barcode.common.Barcode
import dr.achim.code_scanner.common.TAG
import dr.achim.code_scanner.domain.model.ContentType
import dr.achim.code_scanner.service.customtab.CustomTabHelper
import kotlin.reflect.KProperty

class ActionHandler(
    private val activity: ComponentActivity,
    private val resultLauncher: ActivityResultLauncher<Intent>
) {

    operator fun getValue(mainActivity: MainActivity, property: KProperty<*>): ActionHandler {
        return ActionHandler(activity, resultLauncher)
    }

    fun handleAction(
        customTabHelper: CustomTabHelper,
        assistAction: ContentType.AssistAction,
        onCompleted: () -> Unit,
    ): Boolean {
        val result = assistAction.run {
            when (this) {
                is ContentType.AssistAction.AddContact -> addContactIntent(name, phoneNumber, email)
                is ContentType.AssistAction.Call -> openDialer(phoneNumber)
                is ContentType.AssistAction.Connect -> connectToWifi(wifiData)
                is ContentType.AssistAction.Copy -> copyToClipboard(content)
                is ContentType.AssistAction.LaunchUrl -> {
                    // If action is successful, the activity is in background
                    // and we should not show review until resumed
                    customTabHelper.launchUrl(uri)
                }
            }
        }

        if (result) {
            Log.d(TAG, "handleAction complete")
            onCompleted()
        }

        return result
    }

    private fun addContactIntent(name: String?, phoneNumber: String?, email: String?): Boolean {
        val intent = Intent(ContactsContract.Intents.Insert.ACTION).apply {
            type = ContactsContract.RawContacts.CONTENT_TYPE

            name?.let {
                putExtra(ContactsContract.Intents.Insert.NAME, it)
            }

            phoneNumber?.let {
                putExtra(ContactsContract.Intents.Insert.PHONE, it)
            }

            email?.let {
                putExtra(ContactsContract.Intents.Insert.EMAIL, email)
            }
        }

        return try {
            activity.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            false
        }
    }

    private fun connectToWifi(wifiData: Barcode.WiFi): Boolean {

        val ssid = wifiData.ssid ?: return false
        val password = wifiData.password ?: ""

        val networkSuggestions = buildList {
            when (wifiData.encryptionType) {
                Barcode.WiFi.TYPE_WPA -> {
                    WifiNetworkSuggestion.Builder()
                        .setSsid(ssid)
                        .setWpa2Passphrase(password)
                        .build()
                        .also { add(it) }

                    WifiNetworkSuggestion.Builder()
                        .setSsid(ssid)
                        .setWpa3Passphrase(password)
                        .build()
                        .also { add(it) }
                }

                Barcode.WiFi.TYPE_WEP -> {}
                else -> {
                    // open network
                    WifiNetworkSuggestion.Builder()
                        .setSsid(ssid)
                        .build()
                        .also { add(it) }
                }
            }
        }

        if (networkSuggestions.isEmpty()) {
            return false
        }

        val intent = Intent(Settings.ACTION_WIFI_ADD_NETWORKS).apply {
            putParcelableArrayListExtra(
                Settings.EXTRA_WIFI_NETWORK_LIST,
                ArrayList(networkSuggestions)
            )
        }

        return try {
            resultLauncher.launch(intent)
            true
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "Could not save wifi", Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun openDialer(phoneNumber: String): Boolean {
        val uri = Uri.parse("tel:$phoneNumber")
        val intent = Intent(Intent.ACTION_DIAL, uri)

        return try {
            activity.startActivity(intent)
            true
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(activity, "Could not open dialer", Toast.LENGTH_LONG).show()
            false
        }
    }

    private fun copyToClipboard(text: String): Boolean {
        val clipboardManager =
            activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("scanned code", text)
        clipboardManager.setPrimaryClip(clip)

        // Only show a toast for Android 12 and lower.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
            Toast.makeText(activity, "Copied", Toast.LENGTH_SHORT).show()

        return true
    }
}
