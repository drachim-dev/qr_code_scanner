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
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import dr.achim.code_scanner.presentation.components.DefaultAppBar
import dr.achim.code_scanner.presentation.theme.AppTheme
import dr.achim.code_scanner.presentation.theme.LocalSpacing
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.receiveAsFlow
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.compose.OnParticleSystemUpdateListener
import nl.dionsegijn.konfetti.core.Angle
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.PartySystem
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.Rotation
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import nl.dionsegijn.konfetti.core.models.Size
import java.util.concurrent.TimeUnit

@Composable
fun InfoDialog(
    viewModel: InfoDialogViewModel = hiltViewModel(),
    onNavigateToLibraries: () -> Unit,
    onDismissRequest: () -> Unit,
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    InfoDialogContent(
        onNavigateToLibraries = onNavigateToLibraries,
        isLoadingProducts = viewState.loading,
        products = viewState.productList,
        onPurchase = {
            viewState.onPurchase(context.findActivity(), it)
        },
        eventsFlow = viewModel.eventsFlow,
        onDismissRequest = onDismissRequest
    )
}

@Composable
fun InfoDialogContent(
    onNavigateToLibraries: () -> Unit,
    isLoadingProducts: Boolean,
    products: List<StoreProduct>,
    onPurchase: (product: StoreProduct) -> Unit,
    eventsFlow: Flow<PurchaseEvent>,
    onDismissRequest: () -> Unit,
) {

    val context = LocalContext.current
    var showConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        eventsFlow.collectLatest {
            when (it) {
                PurchaseEvent.PurchaseComplete -> {
                    showConfetti = true

                    // show in app review
                }

                PurchaseEvent.PurchaseAborted -> {
                    // show snack bar error
                }
            }
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(shape = RoundedCornerShape(Dimens.cornerSize)) {
            Box {
                Column(
                    modifier = Modifier.padding(LocalSpacing.current.s),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.xs)
                ) {
                    DefaultAppBar(title = R.string.screen_about, actions = {
                        IconButton(onClick = onDismissRequest) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.label_close),
                            )
                        }
                    })

                    Divider(Modifier.padding(vertical = LocalSpacing.current.s))

                    AboutProfile()

                    StoreProductList(
                        isLoading = isLoadingProducts,
                        products = products,
                        onPurchase = onPurchase
                    )

                    ListItem(
                        modifier = Modifier.clickable {
                            launchPlayStoreEntry(context)
                        },
                        headlineContent = { Text(stringResource(R.string.screen_about_rate_app)) },
                        trailingContent = { Icon(Icons.Default.OpenInNew, null) }
                    )

                    Divider(modifier = Modifier.padding(vertical = LocalSpacing.current.s))

                    ListItem(
                        modifier = Modifier.clickable {
                            onNavigateToLibraries()
                            onDismissRequest()
                        },
                        headlineContent = { Text(stringResource(R.string.screen_about_libraries)) },
                        trailingContent = { Icon(Icons.Default.ChevronRight, null) }
                    )
                }
                if (showConfetti) {
                    Confetti { showConfetti = false }
                }
            }
        }
    }
}

@Composable
private fun BoxScope.Confetti(onAnimationCompleted: () -> Unit) {
    KonfettiView(
        modifier = Modifier.Companion
            .matchParentSize()
            .align(Alignment.BottomCenter),
        parties = listOf(
            Party(
                speed = 30f,
                maxSpeed = 50f,
                damping = 0.9f,
                angle = Angle.TOP,
                spread = 45,
                size = listOf(Size.SMALL, Size.LARGE, Size.LARGE),
                shapes = listOf(Shape.Square, Shape.Circle),
                timeToLive = 3000L,
                rotation = Rotation(),
                colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(30),
                position = Position.Relative(0.5, 1.0)
            )
        ),
        updateListener = object : OnParticleSystemUpdateListener {
            override fun onParticleSystemEnded(
                system: PartySystem,
                activeSystems: Int,
            ) {
                if (activeSystems == 0) {
                    onAnimationCompleted()
                }
            }
        },
    )
}

@Composable
private fun ColumnScope.StoreProductList(
    isLoading: Boolean,
    products: List<StoreProduct>,
    onPurchase: (product: StoreProduct) -> Unit
) {
    AnimatedVisibility(visible = !isLoading) {
        products.map {
            ListItem(
                modifier = Modifier.clickable { onPurchase(it) },
                headlineContent = {
                    Text(it.title.replace(Regex("\\(.*?\\)"), ""))
                },
                trailingContent = { Text(it.price.formatted) }
            )
        }
    }
}

@Composable
private fun AboutProfile() {
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
        text = stringResource(R.string.screen_about_profile_description),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.secondary,
    )
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

    val purchaseFlow = Channel<PurchaseEvent>()

    AppTheme {
        InfoDialogContent(
            onNavigateToLibraries = {},
            isLoadingProducts = false,
            products = emptyList(),
            onPurchase = {
                purchaseFlow.trySend(PurchaseEvent.PurchaseComplete)
            },
            eventsFlow = purchaseFlow.receiveAsFlow(),
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
            products = emptyList(),
            onPurchase = {},
            eventsFlow = emptyFlow(),
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
            products = emptyList(),
            onPurchase = {},
            eventsFlow = emptyFlow(),
            onDismissRequest = {})
    }
}

