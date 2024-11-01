package dr.achim.code_scanner.presentation.components.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import dr.achim.code_scanner.R

@Composable
fun NavigateUpButton(navigateUp: () -> Unit) {
    IconButton(onClick = navigateUp) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.access_icon_back)
        )
    }
}