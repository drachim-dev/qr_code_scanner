package dr.achim.code_scanner.common

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark

@PreviewFontScale
@PreviewLightDark
@Preview(showBackground = true, locale = "de")
@Preview(showBackground = true, locale = "en")
annotation class DefaultPreview

@PreviewLightDark
@PreviewDynamicColors
@Preview
annotation class DynamicPreview