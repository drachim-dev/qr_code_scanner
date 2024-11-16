package dr.achim.code_scanner.common.preview

import android.net.Uri
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dr.achim.code_scanner.domain.model.Code

class CodeParameterProvider : PreviewParameterProvider<Code> {
    override val values = sequenceOf(
        Code.Wifi(
            id = "1",
            rawValue = null,
            displayValue = null,
            ssid = "The Ping in the North",
            password = "W!n73r_!5_c0m!nG",
            encryptionType = 0

        ),
        Code.Url(
            id = "2",
            rawValue = null,
            displayValue = "https://en.akinator.com",
            uri = Uri.parse("https://en.akinator.com")
        ),
        Code.Phone(
            id = "3",
            rawValue = "049123456789",
            displayValue = "+49(0) 123/456789",
            phoneNumber = "+49(0) 123/456789"
        )
    )
}