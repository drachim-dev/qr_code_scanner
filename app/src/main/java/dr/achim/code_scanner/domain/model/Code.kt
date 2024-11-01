package dr.achim.code_scanner.domain.model

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Wifi
import dr.achim.code_scanner.R
import java.time.OffsetDateTime

sealed class Code : ContentType {
    abstract val id: String
    val created: OffsetDateTime = OffsetDateTime.now()
    abstract val rawValue: String?
    abstract val displayValue: String?

    data class Phone(
        override val id: String,
        override val rawValue: String?,
        override val displayValue: String?,
        val phoneNumber: String,
    ) : Code() {
        override val actions =
            listOf(AssistAction.Call(phoneNumber), AssistAction.Copy(phoneNumber))
        override val rawContent = phoneNumber
        override val icon = Icons.Default.Phone
    }

    data class Url(
        override val id: String,
        override val rawValue: String?,
        override val displayValue: String?,
        val uri: Uri,
    ) : Code() {
        override val actions = listOf(AssistAction.LaunchUrl(uri))
        override val rawContent = "${uri.host}${uri.path}".removePrefix("www.")
        override val icon = Icons.Default.Link
    }

    data class Text(
        override val id: String,
        override val rawValue: String?,
        override val displayValue: String?,
        val text: String,
    ) : Code() {
        override val actions = listOf(AssistAction.Copy(text))
        override val rawContent = text
        override val icon = Icons.AutoMirrored.Default.TextSnippet
    }

    data class Contact(
        override val id: String,
        override val rawValue: String?,
        override val displayValue: String?,
        val name: String?,
        val phone: String?,
        val emailAddress: String?,
        val address: String?,
        val url: String?,
        val organization: String?,
    ) : Code() {
        override val actions = buildList {
            add(AssistAction.AddContact(name, phone, emailAddress))
            phone?.let { add(AssistAction.Call(it)) }
        }.toList()

        override val rawContent = name ?: phone
        override val icon = Icons.Default.Phone
    }

    data class Wifi(
        override val id: String,
        override val rawValue: String?,
        override val displayValue: String?,
        val ssid: String,
        val password: String,
        val encryptionType: Int?
    ) : Code() {
        override val actions = buildList {
            add(AssistAction.Connect(ssid, password, encryptionType))
            if (password.isNotEmpty()) {
                add(
                    AssistAction.Copy(
                        content = password,
                        label = R.string.action_copy_password
                    )
                )
            }
        }
        override val rawContent = ssid
        override val additionalContent = if (password.isNotEmpty()) {
            "Password: $password"
        } else null
        override val icon = Icons.Default.Wifi
    }

    data class NotSupported(
        override val id: String,
        override val rawValue: String?,
        override val displayValue: String?,
    ) : Code() {
        override val actions =
            (displayValue ?: rawValue)?.let { listOf(AssistAction.Copy(it)) } ?: emptyList()
        override val rawContent = displayValue ?: rawValue
        override val icon = Icons.Filled.CameraAlt
    }
}