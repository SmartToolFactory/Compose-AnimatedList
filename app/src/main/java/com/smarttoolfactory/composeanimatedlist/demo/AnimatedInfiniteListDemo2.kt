package com.smarttoolfactory.composeanimatedlist.demo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Slider
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.animatedlist.ActiveColor
import com.smarttoolfactory.animatedlist.AnimatedInfiniteLazyRow
import com.smarttoolfactory.animatedlist.InactiveColor
import com.smarttoolfactory.composeanimatedlist.SnackCard
import com.smarttoolfactory.composeanimatedlist.snacks

@Composable
fun AnimatedInfiniteListDemo2() {

    Spacer(modifier = Modifier.height(40.dp))

    var visibleIteCount by remember { mutableStateOf(5f) }
    var selectorIndex by remember { mutableStateOf(2f) }
    var itemScaleRange by remember { mutableStateOf(1f) }
    var showPartialItem by remember { mutableStateOf(false) }


    Text("Visible Item Count: $visibleIteCount")
    Slider(
        value = visibleIteCount,
        onValueChange = {
            visibleIteCount = it
        },
        steps = 9,
        valueRange = 3f..11f
    )

    Text("Selector Index: $selectorIndex")
    Slider(
        value = selectorIndex,
        onValueChange = {
            selectorIndex = it
        },
        steps = (visibleIteCount - 1).toInt(),
        valueRange = 0f..(visibleIteCount - 1)
    )

    Text("Scale Range: $itemScaleRange")
    Slider(
        value = itemScaleRange,
        onValueChange = {
            selectorIndex = it
        },
        steps = (visibleIteCount - 2).toInt(),
        valueRange = 1f..(visibleIteCount - 1)
    )


    Row {
        Text("Show Partially")
        Spacer(modifier = Modifier.width(8.dp))
        Switch(checked = showPartialItem, onCheckedChange = { showPartialItem = it })
    }

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
        inactiveItemPercent = 40,
        inactiveColor = InactiveColor,
        activeColor = ActiveColor
    ) { animationProgress, index, item, size ->

        val scale = animationProgress.scale

        SnackCard(
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    alpha = scale
                }
                .clip(CircleShape)
                .size(size),
            snack = item
        )

    }
}