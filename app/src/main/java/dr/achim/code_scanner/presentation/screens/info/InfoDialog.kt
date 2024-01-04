package dr.achim.code_scanner.presentation.screens.info

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.revenuecat.purchases.models.StoreProduct
import dr.achim.code_scanner.R
import dr.achim.code_scanner.common.DefaultPreview
import dr.achim.code_scanner.common.Dimens
import dr.achim.code_scanner.common.findActivity
import dr.achim.code_scanner.presentation.theme.AppTheme
import dr.achim.code_scanner.presentation.theme.LocalSpacing
import kotlinx.coroutines.flow.collectLatest

@Composable
fun InfoDialog(
    viewModel: InfoDialogViewModel = hiltViewModel(),
    onNavigateToLibraries: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.eventsFlow.collectLatest {
            when(it) {
                PurchaseEvent.PurchaseComplete -> {
                    // show in app review
                    // show nice animation
                }
                PurchaseEvent.PurchaseAborted -> {
                    // show snack bar error
                }
            }
        }
    }

    InfoDialogContent(
        onNavigateToLibraries = onNavigateToLibraries,
        isLoadingProducts = viewState.loading,
        productList = viewState.productList,
        onBuyProduct = {
            viewState.onBuyProduct(context.findActivity(), it)
        },
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun InfoDialogContent(
    onNavigateToLibraries: () -> Unit,
    isLoadingProducts: Boolean,
    productList: List<StoreProduct>,
    onBuyProduct: (product: StoreProduct) -> Unit,
    onDismissRequest: () -> Unit,
) {

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(shape = RoundedCornerShape(Dimens.cornerSize)) {
            Column(
                modifier = Modifier.padding(LocalSpacing.current.s),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.xs)
            ) {

                Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        modifier = Modifier
                            .padding(vertical = LocalSpacing.current.s)
                            .align(Alignment.Center),
                        text = "About the app",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                    )

                    IconButton(
                        modifier = Modifier.align(Alignment.TopEnd),
                        onClick = onDismissRequest
                    ) {
                        Icon(
                            modifier = Modifier.size(28.dp),
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = LocalSpacing.current.s))

                Image(
                    painter = painterResource(R.drawable.about_me),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.onBackground, CircleShape)
                )

                Text(
                    modifier = Modifier
                        .padding(
                            horizontal = LocalSpacing.current.m,
                            vertical = LocalSpacing.current.s
                        ),
                    text = "Behind the scenes - me and my purr-fect assistant Samira! " +
                            "Her speciality: spontaneous keyboard walks \uD83D\uDC3E \n" +
                            "If you like our work, consider supporting our coding adventures " +
                            "donating a virtual treat or by rating the app ✨",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )

                // products
                AnimatedVisibility(visible = !isLoadingProducts) {
                    productList.map {
                        ListItem(
                            modifier = Modifier.clickable { onBuyProduct(it) },
                            headlineContent = { Text(it.title) },
                            trailingContent = { Text(it.price.formatted) }
                        )
                    }
                }

                ListItem(
                    modifier = Modifier.clickable {
                        launchPlayStoreEntry(context)
                    },
                    headlineContent = { Text("Rate the app") },
                    trailingContent = { Icon(Icons.Default.OpenInNew, null) }
                )

                Divider(modifier = Modifier.padding(vertical = LocalSpacing.current.s))

                ListItem(
                    modifier = Modifier.clickable {
                        onNavigateToLibraries()
                    },
                    headlineContent = { Text("Libraries used") },
                    trailingContent = { Icon(Icons.Default.ChevronRight, null) }
                )
            }
        }
    }
}

private fun launchPlayStoreEntry(context: Context) {
    val packageName = context.packageName
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
        setPackage("com.android.vending")
    }

    context.startActivity(intent)
}

@DefaultPreview
@Composable
fun InfoScreenContentLoadedPreview() {
    AppTheme {
        InfoDialogContent(
            onNavigateToLibraries = {},
            isLoadingProducts = false,
            productList = emptyList(),
            onBuyProduct = {},
            onDismissRequest = {})
    }
}

@Preview
@Composable
fun InfoScreenContentLoadedEmptyPreview() {
    AppTheme {
        InfoDialogContent(
            onNavigateToLibraries = {},
            isLoadingProducts = false,
            productList = emptyList(),
            onBuyProduct = {},
            onDismissRequest = {})
    }
}


@Preview
@Composable
fun InfoScreenContentLoadingPreview() {
    AppTheme {
        InfoDialogContent(
            onNavigateToLibraries = {},
            isLoadingProducts = true,
            productList = emptyList(),
            onBuyProduct = {},
            onDismissRequest = {})
    }
}

