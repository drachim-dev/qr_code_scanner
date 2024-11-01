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
            displayValue = "https://stackoverflow.com/questions/71927791/android-jetpack-compose-how-to-make-text-utilize-complete-row-space-and-break-t",
            uri = Uri.parse("https://stackoverflow.com/questions/71927791/android-jetpack-compose-how-to-make-text-utilize-complete-row-space-and-break-t")
        ),
        Code.Phone(
            id = "3",
            rawValue = "049123456789",
            displayValue = "+49(0) 123/456789",
            phoneNumber = "+49(0) 123/456789"
        )
    )
}