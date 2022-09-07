package com.smarttoolfactory.composeanimatedlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size

@Immutable
data class Snack(
    val id: Long,
    val name: String,
    val imageUrl: String,
    val price: Long,
    val tagline: String = "",
    val tags: Set<String> = emptySet()
)

@Composable
fun SnackCard(
    modifier: Modifier,
    snack: Snack) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(snack.imageUrl)
            .placeholder(R.drawable.placeholder)
            .size(Size.ORIGINAL) // Set the target size to load the image at.
            .build()
    )


    ElevatedCard(
        modifier = modifier
    ) {
        Image(
            modifier=Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            painter = painter,
            contentDescription = null
        )
    }

}

val snacks = listOf(
    Snack(
        id = 1L,
        name = "Cupcake",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/pGM4sjt_BdQ",
        price = 299
    ),
    Snack(
        id = 2L,
        name = "Donut",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/Yc5sL-ejk6U",
        price = 299
    ),
    Snack(
        id = 3L,
        name = "Eclair",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/-LojFX9NfPY",
        price = 299
    ),
    Snack(
        id = 4L,
        name = "Froyo",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/3U2V5WqK1PQ",
        price = 299
    ),
    Snack(
        id = 5L,
        name = "Gingerbread",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/Y4YR9OjdIMk",
        price = 499
    ),
    Snack(
        id = 6L,
        name = "Honeycomb",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/bELvIg_KZGU",
        price = 299
    ),
    Snack(
        id = 7L,
        name = "Ice Cream Sandwich",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/YgYJsFDd4AU",
        price = 1299
    ),
    Snack(
        id = 8L,
        name = "Jellybean",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/0u_vbeOkMpk",
        price = 299
    ),
    Snack(
        id = 9L,
        name = "KitKat",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/yb16pT5F_jE",
        price = 549
    ),
    Snack(
        id = 10L,
        name = "Lollipop",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/AHF_ZktTL6Q",
        price = 299
    ),
    Snack(
        id = 11L,
        name = "Marshmallow",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/rqFm0IgMVYY",
        price = 299
    ),
    Snack(
        id = 12L,
        name = "Nougat",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/qRE_OpbVPR8",
        price = 299
    ),
    Snack(
        id = 13L,
        name = "Oreo",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/33fWPnyN6tU",
        price = 299
    ),
    Snack(
        id = 14L,
        name = "Pie",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/aX_ljOOyWJY",
        price = 299
    ),
    Snack(
        id = 15L,
        name = "Chips",
        imageUrl = "https://source.unsplash.com/UsSdMZ78Q3E",
        price = 299
    ),
    Snack(
        id = 16L,
        name = "Pretzels",
        imageUrl = "https://source.unsplash.com/7meCnGCJ5Ms",
        price = 299
    ),
    Snack(
        id = 17L,
        name = "Smoothies",
        imageUrl = "https://source.unsplash.com/m741tj4Cz7M",
        price = 299
    ),
    Snack(
        id = 18L,
        name = "Popcorn",
        imageUrl = "https://source.unsplash.com/iuwMdNq0-s4",
        price = 299
    ),
    Snack(
        id = 19L,
        name = "Almonds",
        imageUrl = "https://source.unsplash.com/qgWWQU1SzqM",
        price = 299
    ),
    Snack(
        id = 20L,
        name = "Cheese",
        imageUrl = "https://source.unsplash.com/9MzCd76xLGk",
        price = 299
    ),
    Snack(
        id = 21L,
        name = "Apples",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/1d9xXWMtQzQ",
        price = 299
    ),
    Snack(
        id = 22L,
        name = "Apple sauce",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/wZxpOw84QTU",
        price = 299
    ),
    Snack(
        id = 23L,
        name = "Apple chips",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/okzeRxm_GPo",
        price = 299
    ),
    Snack(
        id = 24L,
        name = "Apple juice",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/l7imGdupuhU",
        price = 299
    ),
    Snack(
        id = 25L,
        name = "Apple pie",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/bkXzABDt08Q",
        price = 299
    ),
    Snack(
        id = 26L,
        name = "Grapes",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/y2MeW00BdBo",
        price = 299
    ),
    Snack(
        id = 27L,
        name = "Kiwi",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/1oMGgHn-M8k",
        price = 299
    ),
    Snack(
        id = 28L,
        name = "Mango",
        tagline = "A tag line",
        imageUrl = "https://source.unsplash.com/TIGDsyy0TK4",
        price = 299
    )
)
