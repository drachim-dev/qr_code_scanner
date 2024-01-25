package dr.achim.code_scanner.domain.model

import android.net.Uri
import android.webkit.URLUtil
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.TextSnippet
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.mlkit.vision.barcode.common.Barcode
import dr.achim.code_scanner.R

sealed interface ContentType {
    val actions: List<AssistAction>
    val rawContent: String?
    val icon: ImageVector
    val additionalContent: String?
        get() = null

    data class Text(val text: String) : ContentType {
        override val actions = listOf(AssistAction.Copy(text))
        override val rawContent = text
        override val icon = Icons.Default.TextSnippet
    }

    data class Url(val uri: Uri) : ContentType {
        override val actions = listOf(AssistAction.LaunchUrl(uri))
        override val rawContent = "${uri.host}${uri.path}".removePrefix("www.")
        override val icon = Icons.Default.Link
    }

    data class Phone(val phoneNumber: String) : ContentType {
        override val actions =
            listOf(AssistAction.Call(phoneNumber), AssistAction.Copy(phoneNumber))
        override val rawContent = phoneNumber
        override val icon = Icons.Default.Phone
    }

    data class Contact(
        val name: Barcode.PersonName?,
        val phone: String?,
        val email: Barcode.Email?,
        val address: Barcode.Address?,
        val url: String?,
        val organization: String?
    ) : ContentType {
        override val actions = buildList {
            add(AssistAction.AddContact(name?.formattedName, phone, email?.address))
            phone?.let { add(AssistAction.Call(it)) }
        }.toList()

        override val rawContent = name?.formattedName ?: phone
        override val icon = Icons.Default.Phone
    }

    data class Wifi(val wifiData: Barcode.WiFi) : ContentType {
        override val actions = buildList {
            add(AssistAction.Connect(wifiData))
            wifiData.password?.let {
                add(
                    AssistAction.Copy(
                        it,
                        label = R.string.action_copy_password
                    )
                )
            }
        }
        override val rawContent = wifiData.ssid
        override val additionalContent = wifiData.password?.let { "Password: $it" }
        override val icon = Icons.Default.Wifi
    }

    data class Unknown(val content: String) : ContentType {
        override val actions = listOf(AssistAction.Copy(content))
        override val rawContent = content
        override val icon = Icons.Filled.CameraAlt

    }

    companion object {
        fun parse(barcode: Barcode): ContentType? {

            return when (barcode.valueType) {
                Barcode.TYPE_PHONE -> {
                    val phoneNumber = barcode.phone?.number
                    if (phoneNumber != null) Phone(phoneNumber) else null
                }

                Barcode.TYPE_CONTACT_INFO -> {
                    barcode.contactInfo?.run {
                        Contact(
                            name,
                            phones.firstOrNull()?.number,
                            emails.firstOrNull(),
                            addresses.firstOrNull(),
                            urls.firstOrNull(),
                            organization
                        )
                    }
                }

                Barcode.TYPE_TEXT -> {
                    val text = barcode.displayValue ?: barcode.rawValue
                    if (text != null) Text(text) else null
                }

                Barcode.TYPE_URL -> {
                    val url = barcode.url?.url
                    if (URLUtil.isValidUrl(url)) {
                        Url(Uri.parse(url))
                    } else null
                }

                Barcode.TYPE_WIFI -> {
                    val wifiData = barcode.wifi
                    if (wifiData != null) Wifi(wifiData) else null
                }

                else -> {
                    val content = barcode.displayValue ?: barcode.rawValue
                    if (content != null) Unknown(content) else null
                }
            }
        }
    }

    sealed class AssistAction(val icon: ImageVector) {

        abstract val label: Int

        data class AddContact(
            val name: String?,
            val phoneNumber: String?,
            val email: String?
        ) : AssistAction(Icons.Default.ContactPhone) {
            override val label = R.string.action_add_contact
        }

        data class Call(val phoneNumber: String) : AssistAction(Icons.Default.Call) {
            override val label = R.string.action_call_number
        }

        data class Connect(val wifiData: Barcode.WiFi) : AssistAction(Icons.Default.Wifi) {
            override val label = R.string.action_add_wifi
        }

        data class Copy(
            val content: String,
            override val label: Int = R.string.action_copy_text
        ) : AssistAction(Icons.Default.ContentCopy)

        data class LaunchUrl(val uri: Uri) : AssistAction(Icons.Default.Launch) {
            override val label = R.string.action_open_browser
        }
    }
}
