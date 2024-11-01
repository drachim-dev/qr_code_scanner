package dr.achim.code_scanner

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.provider.ContactsContract
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import com.google.mlkit.vision.barcode.common.Barcode
import dr.achim.code_scanner.domain.model.AssistAction
import dr.achim.code_scanner.service.customtab.CustomTabHelper

class ActionHandler(
    private val context: ComponentActivity,
    private val resultLauncher: ActivityResultLauncher<Intent>
) {

    fun handleAction(
        customTabHelper: CustomTabHelper,
        assistAction: AssistAction
    ): Boolean {
        var success = false
        val intent = assistAction.run {
            when (this) {
                is AssistAction.AddContact -> getAddContactIntent(name, phoneNumber, email)
                is AssistAction.Call -> getDialIntent(phoneNumber)
                is AssistAction.Connect -> getWifiIntent(ssid, password ?: "", encryptionType)
                is AssistAction.Copy -> {
                    copyToClipboard(content)
                    success = true
                    null
                }

                is AssistAction.LaunchUrl -> {
                    getAppIntent(uri) ?: customTabHelper.getLaunchUrlIntent(uri)
                }
            }
        }

        intent?.let {
            try {
                resultLauncher.launch(it)
                success = true
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    context.getString(R.string.action_error_no_compatible_app_installed),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return success
    }

    /**
     * Creates an intent to open the given URI and handles app selection.
     * @return the intent if a suitable activity is found, otherwise null.
     */
    private fun getAppIntent(uri: Uri): Intent? {
        // handle app links for installed apps
        val intent = Intent(Intent.ACTION_VIEW, uri)
        val pm = context.packageManager

        return if (pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
            Intent.createChooser(intent, context.getString(R.string.action_open_chooser_title))
        } else null
    }

    private fun getAddContactIntent(name: String?, phoneNumber: String?, email: String?): Intent {
        return Intent(ContactsContract.Intents.Insert.ACTION).apply {
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
    }

    private fun getWifiIntent(ssid: String, password: String, encryptionType: Int?): Intent? {

        val networkSuggestions = buildList {
            when (encryptionType) {
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
            return null
        }

        return Intent(Settings.ACTION_WIFI_ADD_NETWORKS).apply {
            putParcelableArrayListExtra(
                Settings.EXTRA_WIFI_NETWORK_LIST,
                ArrayList(networkSuggestions)
            )
        }
    }

    private fun getDialIntent(phoneNumber: String): Intent {
        val uri = Uri.parse("tel:$phoneNumber")
        return Intent(Intent.ACTION_DIAL, uri)
    }

    private fun copyToClipboard(text: String) {
        val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val label = context.getString(R.string.clipboard_label_scanned_code)
        val clip = ClipData.newPlainText(label, text)
        manager.setPrimaryClip(clip)

        // Only show a toast for Android 12 and lower.
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            val message = context.getString(R.string.action_copy_success_toast_title)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}
