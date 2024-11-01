package dr.achim.code_scanner.common

import android.content.Context
import androidx.annotation.StringRes
import dr.achim.code_scanner.R
import java.time.OffsetDateTime
import java.time.format.FormatStyle
import java.util.Locale
import javax.inject.Inject
import java.time.format.DateTimeFormatter as JavaDateTimeFormatter

class DateTimeFormatter @Inject constructor(private val context: Context) {
    private val formatterCache = mutableMapOf<Pair<DateTimeFormat, Locale>, JavaDateTimeFormatter>()

    private fun getJavaDateTimeFormatter(
        dateFormat: DateTimeFormat,
        locale: Locale
    ): JavaDateTimeFormatter {
        val key = Pair(dateFormat, locale)
        return formatterCache.getOrPut(key) {
            when (dateFormat) {
                is DateTimeFormat.Pattern -> JavaDateTimeFormatter.ofPattern(
                    context.getString(dateFormat.resourceId),
                    locale
                )

                is DateTimeFormat.Style -> JavaDateTimeFormatter.ofLocalizedDateTime(dateFormat.style)
            }
        }
    }

    fun format(dateTime: OffsetDateTime, params: DateTimeFormatParams): String {
        val javaDateTimeFormatter = getJavaDateTimeFormatter(params.dateFormat, params.locale)
        val formattedDate = dateTime.format(javaDateTimeFormatter)
        val suffix = params.dateFormat.suffixResourceId?.let { context.getString(it) }

        return if (suffix == null) {
            formattedDate
        } else {
            "$formattedDate $suffix"
        }
    }
}

class DateTimeFormatBuilder {
    var format: DateTimeFormat = DateTimeFormat.Pattern.MonthYear
    var locale: Locale = Locale.getDefault()

    fun build(): DateTimeFormatParams = DateTimeFormatParams(format, locale)
}

data class DateTimeFormatParams(
    val dateFormat: DateTimeFormat,
    val locale: Locale = Locale.getDefault()
)

fun OffsetDateTime.formatWith(
    formatter: DateTimeFormatter,
    block: DateTimeFormatBuilder.() -> Unit
): String {
    val params = DateTimeFormatBuilder()
        .apply(block)
        .build()

    return formatter.format(this, params)
}

sealed interface DateTimeFormat {

    @get:StringRes
    val suffixResourceId: Int?
        get() = null

    sealed class Pattern(@StringRes val resourceId: Int) : DateTimeFormat {
        data object MonthYear : Pattern(R.string.common_date_month_year)
    }

    sealed class Style(val style: FormatStyle, override val suffixResourceId: Int?) :
        DateTimeFormat {
        data object DateTimeShort : Style(FormatStyle.SHORT, R.string.common_date_time_short_suffix)
    }
}