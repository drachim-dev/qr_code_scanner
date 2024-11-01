package dr.achim.code_scanner.presentation.components.appbar

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import dr.achim.code_scanner.presentation.navigation.NavigateUp
import dr.achim.code_scanner.presentation.theme.AppTheme

object DefaultAppBar {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun colors(
        containerColor: Color = Color.Transparent,
        scrolledContainerColor: Color = Color.Unspecified,
        navigationIconContentColor: Color = Color.Unspecified,
        titleContentColor: Color = AppTheme.colorScheme.primary,
        actionIconContentColor: Color = Color.Unspecified,
    ): TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = containerColor,
        scrolledContainerColor = scrolledContainerColor,
        navigationIconContentColor = navigationIconContentColor,
        titleContentColor = titleContentColor,
        actionIconContentColor = actionIconContentColor
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(
    title: String,
    modifier: Modifier = Modifier,
    navigateUp: NavigateUp? = null,
    leadingIcon: @Composable () -> Unit = {
        navigateUp?.let {
            NavigateUpButton(it)
        }
    },
    actions: @Composable RowScope.() -> Unit = {},
    colors: TopAppBarColors = DefaultAppBar.colors(),
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        modifier = modifier,
        navigationIcon = leadingIcon,
        actions = actions,
        colors = colors,
    )
}