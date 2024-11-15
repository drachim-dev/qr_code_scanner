package dr.achim.code_scanner.presentation.screens.about

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
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
import dr.achim.code_scanner.common.DynamicPreview
import dr.achim.code_scanner.common.findActivity
import dr.achim.code_scanner.presentation.theme.AppTheme
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
fun AboutDialog(
    viewModel: AboutDialogViewModel = hiltViewModel(),
    onDismissRequest: () -> Unit,
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    AboutDialogContent(
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
fun AboutDialogContent(
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
                    modifier = Modifier.padding(AppTheme.spacing.s),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        IconButton(
                            onClick = onDismissRequest,
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = stringResource(R.string.access_icon_close),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        AboutProfile()
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = AppTheme.spacing.m)
                            .padding(top = AppTheme.spacing.l, bottom = AppTheme.spacing.m),
                        text = stringResource(R.string.screen_about_us_support_description),
                        style = AppTheme.typography.titleSmall,
                        color = AppTheme.colorScheme.primary,
                    )

                    HorizontalDivider()

                    StoreProductList(
                        isLoading = isLoadingProducts,
                        products = products,
                        onPurchase = onPurchase
                    )

                    ListItem(
                        modifier = Modifier.clickable { launchPlayStoreEntry(context) },
                        headlineContent = { Text(stringResource(R.string.screen_about_us_rate_app)) },
                        trailingContent = { Icon(Icons.AutoMirrored.Default.OpenInNew, null) }
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
        Column {
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
}

@Composable
private fun AboutProfile() {
    Column(
        modifier = Modifier.padding(top = AppTheme.spacing.m),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.xs)
    ) {
        Image(
            painter = painterResource(R.drawable.cats_programming),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(156.dp)
                .clip(CircleShape)
                .border(2.dp, AppTheme.colorScheme.onBackground, CircleShape)
        )

        Text(
            modifier = Modifier
                .padding(
                    horizontal = AppTheme.spacing.m,
                    vertical = AppTheme.spacing.s
                ),
            textAlign = TextAlign.Center,
            text = stringResource(R.string.screen_about_us_profile_description),
            style = AppTheme.typography.bodyMedium,
            color = AppTheme.colorScheme.secondary,
        )
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

@DynamicPreview
@DefaultPreview
@Composable
private fun ContentLoadedPreview() {
    val purchaseFlow = Channel<PurchaseEvent>()

    AppTheme {
        AboutDialogContent(
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
private fun ContentLoadedEmptyPreview() {
    AppTheme {
        AboutDialogContent(
            isLoadingProducts = false,
            products = emptyList(),
            onPurchase = {},
            eventsFlow = emptyFlow(),
            onDismissRequest = {})
    }
}


@Preview
@Composable
private fun ContentLoadingPreview() {
    AppTheme {
        AboutDialogContent(
            isLoadingProducts = true,
            products = emptyList(),
            onPurchase = {},
            eventsFlow = emptyFlow(),
            onDismissRequest = {})
    }
}

