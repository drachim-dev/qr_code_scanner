package dr.achim.code_scanner.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime

@Entity
data class CodeEntity(
    @PrimaryKey
    val id: String,
    val created: OffsetDateTime,

    // common fields
    val rawValue: String?,       // The raw value of the barcode
    val displayValue: String?,   // The display value of the barcode
    val format: Int,             // Barcode format (e.g., QR, Code128)
    val type: Int,               // Barcode type (e.g., URL, Contact, Email, Wi-Fi)

    // URL-specific fields
    val urlLink: String? = null,

    // Contact-specific fields
    val contactName: String? = null,
    val phoneNumber: String? = null,
    val emailAddress: String? = null,
    val contactAddress: String? = null,
    val contactUrl: String? = null,
    val contactOrganization: String? = null,

    // Wi-Fi-specific fields
    val wifiSsid: String? = null,
    val wifiPassword: String? = null,
    val wifiEncryptionType: Int? = null,
)