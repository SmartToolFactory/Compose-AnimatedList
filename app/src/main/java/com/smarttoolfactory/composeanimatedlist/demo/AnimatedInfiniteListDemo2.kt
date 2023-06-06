package com.smarttoolfactory.composeanimatedlist.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttoolfactory.animatedlist.ActiveColor
import com.smarttoolfactory.animatedlist.AnimatedInfiniteLazyRow
import com.smarttoolfactory.animatedlist.InactiveColor
import com.smarttoolfactory.composeanimatedlist.SnackCard
import com.smarttoolfactory.composeanimatedlist.aspectRatios
import com.smarttoolfactory.composeanimatedlist.snacks
import kotlinx.coroutines.launch

@Preview
@Composable
fun AnimatedInfiniteListDemo2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        val coroutineScope = rememberCoroutineScope()

        var visibleIteCount by remember { mutableFloatStateOf(5f) }
        var selectorIndex by remember { mutableFloatStateOf(2f) }
        var itemScaleRange by remember { mutableFloatStateOf(1f) }
        var inactiveItemFraction by remember { mutableFloatStateOf(70f) }

        var showPartialItem by remember { mutableStateOf(false) }


        Text(
            "Visible Item Count: ${visibleIteCount.toInt()}",
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 18.sp
        )
        Slider(
            value = visibleIteCount,
            onValueChange = {
                visibleIteCount = it
            },
            steps = 7,
            valueRange = 3f..11f
        )

        Text(
            "Selector Index: ${selectorIndex.toInt()}",
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 18.sp
        )
        Slider(
            value = selectorIndex,
            onValueChange = {
                selectorIndex = it
            },
            steps = (visibleIteCount - 2).toInt(),
            valueRange = 0f..(visibleIteCount - 1)
        )

        Text(
            "Scale Range: ${itemScaleRange.toInt()}",
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 18.sp
        )
        Slider(
            value = itemScaleRange,
            onValueChange = {
                itemScaleRange = it
            },
            steps = (visibleIteCount - 2).toInt(),
            valueRange = 1f..visibleIteCount
        )

        Text(
            "Inactive Item Percent: ${inactiveItemFraction.toInt()}",
            color = MaterialTheme.colorScheme.inversePrimary,
            fontSize = 18.sp
        )
        Slider(
            value = inactiveItemFraction,
            onValueChange = {
                inactiveItemFraction = it
            },
            valueRange = 0f..100f
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Partial Items",
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.width(20.dp))
            Switch(checked = showPartialItem, onCheckedChange = { showPartialItem = it })
        }

        Spacer(modifier = Modifier.height(30.dp))

        AnimatedInfiniteLazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            items = snacks,
            visibleItemCount = visibleIteCount.toInt(),
            selectorIndex = selectorIndex.toInt(),
            itemScaleRange = itemScaleRange.toInt(),
            showPartialItem = showPartialItem,
            spaceBetweenItems = 2.dp,
            inactiveItemPercent = inactiveItemFraction.toInt(),
            inactiveColor = InactiveColor,
            activeColor = ActiveColor
        ) { animationProgress, index, item, width, lazyListState ->

            val scale = animationProgress.scale

            SnackCard(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        alpha = scale
                    }
                    .clip(CircleShape)
                    .size(width)
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        indication = null
                    ) {
                        coroutineScope.launch {
                            lazyListState.animateScrollBy(-animationProgress.distanceToSelector)
                        }
                    },
                snack = item
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        AnimatedInfiniteLazyRow(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .border(1.dp, Color.Green),
            items = aspectRatios,
            visibleItemCount = visibleIteCount.toInt(),
            selectorIndex = selectorIndex.toInt(),
            itemScaleRange = itemScaleRange.toInt(),
            showPartialItem = showPartialItem,
            inactiveItemPercent = inactiveItemFraction.toInt(),
            inactiveColor = InactiveColor,
            activeColor = ActiveColor,
            itemContent = { animationProgress, index, item, width, lazyListState ->
                val color = animationProgress.color
                val scale = animationProgress.scale
                Box(
                    modifier = Modifier
                        .scale(scale)
                        .background(color, CircleShape)
                        .size(width)
                        .clickable(
                            interactionSource = remember {
                                MutableInteractionSource()
                            },
                            indication = null
                        ) {
                            coroutineScope.launch {
                                lazyListState.animateScrollBy(-animationProgress.distanceToSelector)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        "$index",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }
}