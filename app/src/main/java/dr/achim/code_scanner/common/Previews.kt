package dr.achim.code_scanner.common

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers

@Preview(
    name = "light",
    group = "ui mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark",
    group = "ui mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
annotation class DefaultPreview


@Preview(
    name = "green",
    device = Devices.DEFAULT,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE,
)
annotation class DynamicPreview