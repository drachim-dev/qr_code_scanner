package dr.achim.code_scanner.domain.model

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.ui.graphics.vector.ImageVector
import dr.achim.code_scanner.R

sealed interface ContentType {
    val actions: List<AssistAction>
    val rawContent: String?
    val icon: ImageVector
    val additionalContent: String?
        get() = null
}

sealed class AssistAction(val icon: ImageVector) {
    abstract val label: Int

    data class AddContact(
        val name: String?,
        val phoneNumber: String?,
        val email: String?
    ) : AssistAction(Icons.Default.PersonAdd) {
        override val label = R.string.action_add_contact
    }

    data class Call(val phoneNumber: String) : AssistAction(Icons.Default.Call) {
        override val label = R.string.action_call_number
    }

    data class Connect(val ssid: String, val password: String?, val encryptionType: Int?) :
        AssistAction(Icons.Default.Wifi) {
        override val label = R.string.action_add_wifi
    }

    data class Copy(
        val content: String,
        override val label: Int = R.string.action_copy_text
    ) : AssistAction(Icons.Default.ContentCopy)

    data class LaunchUrl(val uri: Uri) : AssistAction(Icons.AutoMirrored.Default.Launch) {
        override val label = R.string.action_open_browser
    }
}