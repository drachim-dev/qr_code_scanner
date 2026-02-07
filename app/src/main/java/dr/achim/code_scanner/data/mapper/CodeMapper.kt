package dr.achim.code_scanner.data.mapper

import android.webkit.URLUtil
import androidx.core.net.toUri
import com.google.mlkit.vision.barcode.common.Barcode
import dr.achim.code_scanner.data.entity.CodeEntity
import dr.achim.code_scanner.domain.model.Code
import java.util.UUID

fun Barcode.toModel(): Code {
    val id = UUID.randomUUID().toString()
    val code = when (valueType) {
        Barcode.TYPE_PHONE -> {
            val phoneNumber = phone?.number
            if (phoneNumber != null) {
                Code.Phone(
                    id = id,
                    rawValue = rawValue,
                    displayValue = displayValue,
                    phoneNumber = phoneNumber
                )
            } else null
        }

        Barcode.TYPE_CONTACT_INFO -> {
            contactInfo?.run {
                Code.Contact(
                    id = id,
                    rawValue = rawValue,
                    displayValue = displayValue,
                    name = name?.formattedName,
                    phone = phones.firstOrNull()?.number,
                    emailAddress = emails.firstOrNull()?.address,
                    address = addresses.firstOrNull()?.addressLines?.joinToString(separator = " "),
                    url = urls.firstOrNull(),
                    organization = organization
                )
            }
        }

        // esim profile
        Barcode.TYPE_TEXT if rawValue?.startsWith("LPA:") == true -> {
            Code.Esim(
                id = id,
                rawValue = rawValue ?: "",
            )
        }

        Barcode.TYPE_TEXT -> {
            val text = displayValue ?: rawValue
            if (text != null) {
                Code.Text(
                    id = id,
                    rawValue = rawValue,
                    displayValue = displayValue,
                    text = text
                )
            } else null
        }

        Barcode.TYPE_URL -> {
            val url = url?.url
            if (url != null && URLUtil.isValidUrl(url)) {
                Code.Url(
                    id = id,
                    rawValue = rawValue,
                    displayValue = displayValue,
                    uri = url.toUri()
                )
            } else null
        }

        Barcode.TYPE_WIFI -> {
            val wifiData = wifi
            val ssid = wifiData?.ssid
            if (wifiData != null && ssid != null) {
                Code.Wifi(
                    id = id,
                    rawValue = rawValue,
                    displayValue = displayValue,
                    ssid = ssid,
                    password = wifiData.password ?: "",
                    encryptionType = wifiData.encryptionType
                )
            } else null
        }

        else -> null
    }

    return code ?: Code.NotSupported(id = id, rawValue = rawValue, displayValue = displayValue)
}

fun CodeEntity.toModel(): Code {
    val code = when (type) {
        Barcode.TYPE_PHONE -> {
            if (phoneNumber != null) {
                Code.Phone(
                    id = id,
                    rawValue = rawValue,
                    displayValue = displayValue,
                    phoneNumber = phoneNumber
                )
            } else null
        }

        Barcode.TYPE_CONTACT_INFO -> {
            Code.Contact(
                id = id,
                rawValue = rawValue,
                displayValue = displayValue,
                name = contactName,
                phone = phoneNumber,
                emailAddress = emailAddress,
                address = contactAddress,
                url = contactUrl,
                organization = contactOrganization
            )
        }

        Barcode.TYPE_TEXT -> {
            val text = displayValue ?: rawValue
            if (text != null) {
                Code.Text(
                    id = id,
                    rawValue = rawValue,
                    displayValue = displayValue,
                    text = text
                )
            } else null
        }

        Barcode.TYPE_URL -> {
            if (urlLink != null && URLUtil.isValidUrl(urlLink)) {
                Code.Url(
                    id = id,
                    rawValue = rawValue,
                    displayValue = displayValue,
                    uri = urlLink.toUri()
                )
            } else null
        }

        Barcode.TYPE_WIFI -> {
            if (wifiSsid != null) {
                Code.Wifi(
                    id = id,
                    rawValue = rawValue,
                    displayValue = displayValue,
                    ssid = wifiSsid,
                    password = wifiPassword ?: "",
                    encryptionType = wifiEncryptionType
                )
            } else null
        }

        else -> null
    }

    return code ?: Code.NotSupported(id = id, rawValue = rawValue, displayValue = displayValue)
}

fun Code.toEntity(): CodeEntity {
    val baseEntity = CodeEntity(
        id = id,
        created = created,
        rawValue = rawValue,
        displayValue = displayValue,
        format = 0, // TODO: map format
        type = Barcode.TYPE_UNKNOWN,
    )

    return when (this) {
        is Code.Contact -> baseEntity.copy(
            type = Barcode.TYPE_CONTACT_INFO,
            contactName = name,
            phoneNumber = phone,
            emailAddress = emailAddress,
            contactAddress = address,
            contactUrl = url,
        )

        is Code.Esim -> baseEntity.copy(
            type = Barcode.TYPE_TEXT
        )

        is Code.NotSupported -> baseEntity.copy(
            type = Barcode.TYPE_UNKNOWN,
        )

        is Code.Phone -> baseEntity.copy(
            type = Barcode.TYPE_PHONE,
            phoneNumber = phoneNumber,
        )

        is Code.Text -> baseEntity.copy(
            type = Barcode.TYPE_TEXT
        )

        is Code.Url -> baseEntity.copy(
            type = Barcode.TYPE_URL,
            urlLink = uri.toString(),
        )

        is Code.Wifi -> baseEntity.copy(
            type = Barcode.TYPE_WIFI,
            wifiSsid = ssid,
            wifiPassword = password,
            wifiEncryptionType = encryptionType,
        )
    }
}