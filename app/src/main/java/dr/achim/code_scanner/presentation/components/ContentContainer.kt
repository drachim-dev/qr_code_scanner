package dr.achim.code_scanner.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import dr.achim.code_scanner.common.Dimens
import dr.achim.code_scanner.presentation.theme.LocalSpacing

@Composable
fun ContentContainer(icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.s, Alignment.CenterVertically),
    ) {

        TintedImage(
            icon = icon,
            contentDescription = null,
            modifier = Modifier
                .height(Dimens.contentImageSize)
                .fillMaxWidth(),
        )

        Spacer(Modifier.height(LocalSpacing.current.m))

        content()
    }
}