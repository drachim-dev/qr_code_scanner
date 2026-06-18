package dr.achim.code_scanner

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import android.provider.ContactsContract
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.google.mlkit.vision.barcode.common.Barcode
import dr.achim.code_scanner.domain.model.AssistAction
import dr.achim.code_scanner.service.customtab.CustomTabHelper

class ActionHandler(
    private val context: ComponentActivity,
    private val resultLauncher: ActivityResultLauncher<Intent>,
) {

    fun handleAction(
        customTabHelper: CustomTabHelper,
        assistAction: AssistAction
    ): Boolean {
        var success = false
        val intent = assistAction.run {
            when (this) {
                is AssistAction.AddContact -> getAddContactIntent(name, phoneNumber, email)
                is AssistAction.AddEsim -> getAddEsimIntent(activationCode)
                is AssistAction.AddOtp -> getAddOtpIntent(uri)
                is AssistAction.Call -> getDialIntent(phoneNumber)
                is AssistAction.Connect -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        getWifiAddNetworksIntent(ssid, password ?: "", encryptionType)
                    } else {
                        getWifiConnectLegacyIntent(ssid, password ?: "", encryptionType)
                    }
                }
                is AssistAction.Copy -> {
                    copyToClipboard(content)
                    success = true
                    null
                }

                is AssistAction.LaunchUrl -> customTabHelper.getLaunchUrlIntent(uri)
                is AssistAction.LoginWithPasskey -> getPasskeyIntent(uri)
                is AssistAction.Search -> getSearchIntent(query)
            }
        }

        intent?.let {
            try {
                resultLauncher.launch(it)
                success = true
            } catch (_: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    context.getString(R.string.action_error_no_compatible_app_installed),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return success
    }

    private fun getAddEsimIntent(activationCode: String): Intent {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = activationCode.toUri()
        }
        return Intent.createChooser(intent, null)
    }

    private fun getAddOtpIntent(uri: Uri): Intent {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        return Intent.createChooser(intent, context.getString(R.string.chooser_title_add_otp))
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

    @RequiresApi(Build.VERSION_CODES.R)
    private fun getWifiAddNetworksIntent(ssid: String, password: String, encryptionType: Int?): Intent? {
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

    @Suppress("DEPRECATION")
    private fun getWifiConnectLegacyIntent(ssid: String, password: String, encryptionType: Int?): Intent {
        val wifiConfig = WifiConfiguration().apply {
            SSID = "\"$ssid\""
        }

        when (encryptionType) {
            Barcode.WiFi.TYPE_WPA -> {
                wifiConfig.preSharedKey = "\"$password\""
            }

            Barcode.WiFi.TYPE_WEP -> {
                wifiConfig.wepKeys[0] = "\"$password\""
                wifiConfig.wepTxKeyIndex = 0
                wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
                wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40)
            }

            else -> {
                wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE)
            }
        }

        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val netId = wifiManager.addNetwork(wifiConfig)
        if (netId != -1) {
            wifiManager.disconnect()
            wifiManager.enableNetwork(netId, true)
            wifiManager.reconnect()
        }

        // Return intent to open WiFi settings so the user can see the connection progress/status
        return Intent(Settings.ACTION_WIFI_SETTINGS)
    }

    private fun getDialIntent(phoneNumber: String): Intent {
        val uri = "tel:$phoneNumber".toUri()
        return Intent(Intent.ACTION_DIAL, uri)
    }

    private fun getPasskeyIntent(uri: Uri): Intent {
        val intent = Intent(Intent.ACTION_VIEW, uri)
        return Intent.createChooser(intent, context.getString(R.string.chooser_title_passkey))
    }

    private fun getSearchIntent(query: String) : Intent {
        return Intent(Intent.ACTION_WEB_SEARCH).apply {
            putExtra(SearchManager.QUERY, query)
        }
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
