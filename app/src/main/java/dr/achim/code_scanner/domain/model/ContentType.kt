package dr.achim.code_scanner.domain.model

import android.net.Uri
import android.webkit.URLUtil
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Launch
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import com.google.mlkit.vision.barcode.common.Barcode

sealed interface ContentType {
    val actions: List<AssistAction>
    val rawContent: String?

    data class Text(val text: String) : ContentType {
        override val actions = listOf(AssistAction.Copy(text))
        override val rawContent = text
    }

    data class Url(val uri: android.net.Uri) : ContentType {
        override val actions = listOf(AssistAction.LaunchUrl(uri))
        override val rawContent = uri.toString()
    }

    data class Phone(val number: String) : ContentType {
        override val actions = listOf(AssistAction.Call(number), AssistAction.Copy(number))
        override val rawContent = number
    }

    data class Wifi(val wifiData: Barcode.WiFi) : ContentType {
        override val actions = buildList {
            add(AssistAction.Connect(wifiData))
            wifiData.password?.let { add(AssistAction.Copy(it, label = "Copy password")) }
        }
        override val rawContent = listOfNotNull(wifiData.ssid, wifiData.password)
            .joinToString(separator = "\n")
    }

    data class Unknown(val content: String) : ContentType {
        override val actions = listOf(AssistAction.Copy(content))
        override val rawContent = content
    }

    companion object {
        fun parse(barcode: Barcode): ContentType? {

            return when (barcode.valueType) {
                Barcode.TYPE_PHONE -> {
                    val number = barcode.phone?.number
                    if (number != null) Phone(number) else null
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
        abstract val label: String

        data class Call(val number: String) : AssistAction(Icons.Default.Call) {
            override val label = "Call number"
        }

        data class Connect(val wifiData: Barcode.WiFi) : AssistAction(Icons.Default.Wifi) {
            override val label = "Add wifi"
        }

        data class Copy(
            val content: String,
            override val label: String = "Copy text"
        ) : AssistAction(Icons.Default.ContentCopy)

        data class LaunchUrl(val uri: android.net.Uri) : AssistAction(Icons.Default.Launch) {
            override val label = "Open browser"
            val formattedUri = "${uri.host}${uri.path}".removePrefix("www.")
        }
    }
}
