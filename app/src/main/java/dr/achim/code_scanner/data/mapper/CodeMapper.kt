package dr.achim.code_scanner.data.mapper

import android.net.Uri
import android.util.Patterns
import android.webkit.URLUtil
import androidx.core.net.toUri
import com.google.mlkit.vision.barcode.common.Barcode
import dr.achim.code_scanner.data.entity.CodeEntity
import dr.achim.code_scanner.domain.model.Code
import java.util.UUID

fun Barcode.toModel(): Code {
    val id = UUID.randomUUID().toString()

    val code = try {
        when {
            // esim profile
            rawValue?.startsWith("LPA:", ignoreCase = true) == true -> {
                Code.Esim(
                    id = id,
                    rawValue = rawValue!!,
                )
            }

            // passkey
            rawValue?.startsWith("FIDO:/", ignoreCase = true) == true -> {
                Code.Passkey(
                    id = id,
                    rawValue = rawValue!!,
                    uri = rawValue!!.toUri()
                )
            }

            valueType == Barcode.TYPE_PHONE -> {
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

            valueType == Barcode.TYPE_CONTACT_INFO -> {
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

            valueType == Barcode.TYPE_TEXT -> {
                val text = displayValue ?: rawValue
                when (text) {
                    null -> null

                    else if text.isValidUrl() -> {
                        // workaround for urls from type text
                        Code.Url(
                            id = id,
                            rawValue = rawValue,
                            displayValue = displayValue,
                            uri = text.toNetworkUri()
                        )
                    }

                    else -> {
                        Code.Text(
                            id = id,
                            rawValue = rawValue,
                            displayValue = displayValue,
                            text = text
                        )
                    }
                }
            }

            valueType == Barcode.TYPE_URL -> {
                val url = url?.url
                if (url != null && url.isValidUrl()) {
                    Code.Url(
                        id = id,
                        rawValue = rawValue,
                        displayValue = displayValue,
                        uri = url.toUri()
                    )
                } else null
            }

            valueType == Barcode.TYPE_WIFI -> {
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
    } catch (_: NullPointerException) {
        // TODO: Add logging
        null
    }

    return code ?: Code.NotSupported(id = id, rawValue = rawValue, displayValue = displayValue)
}

fun CodeEntity.toModel(): Code {
    val code = when {
        // esim profile
        rawValue?.startsWith("LPA:", ignoreCase = true) == true -> {
            Code.Esim(
                id = id,
                rawValue = rawValue,
            )
        }

        type == Barcode.TYPE_PHONE -> {
            if (phoneNumber != null) {
                Code.Phone(
                    id = id,
                    rawValue = rawValue,
                    displayValue = displayValue,
                    phoneNumber = phoneNumber
                )
            } else null
        }

        type == Barcode.TYPE_CONTACT_INFO -> {
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

        type == Barcode.TYPE_TEXT -> {
            val text = displayValue ?: rawValue
            when (text) {
                null -> null

                else if text.isValidUrl() -> {
                    // workaround for urls from type text
                    Code.Url(
                        id = id,
                        rawValue = rawValue,
                        displayValue = displayValue,
                        uri = text.toNetworkUri()
                    )
                }

                else -> {
                    Code.Text(
                        id = id,
                        rawValue = rawValue,
                        displayValue = displayValue,
                        text = text
                    )
                }
            }
        }

        type == Barcode.TYPE_URL -> {
            if (urlLink != null && urlLink.isValidUrl()) {
                Code.Url(
                    id = id,
                    rawValue = rawValue,
                    displayValue = displayValue,
                    uri = urlLink.toUri()
                )
            } else null
        }

        type == Barcode.TYPE_WIFI -> {
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

        is Code.Passkey -> baseEntity.copy(
            type = Barcode.TYPE_TEXT,
            urlLink = uri.toString(),
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

private fun String.isValidUrl() = Patterns.WEB_URL.matcher(this).matches()
private fun String.toNetworkUri(): Uri {
    return if (URLUtil.isNetworkUrl(this)) {
        this
    } else {
        "https://$this"
    }.toUri()
}