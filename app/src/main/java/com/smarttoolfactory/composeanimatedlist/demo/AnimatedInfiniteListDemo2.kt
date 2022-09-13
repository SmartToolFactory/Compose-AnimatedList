package com.smarttoolfactory.composeanimatedlist.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttoolfactory.animatedlist.ActiveColor
import com.smarttoolfactory.animatedlist.AnimatedInfiniteLazyRow
import com.smarttoolfactory.animatedlist.InactiveColor
import com.smarttoolfactory.composeanimatedlist.SnackCard
import com.smarttoolfactory.composeanimatedlist.aspectRatios
import com.smarttoolfactory.composeanimatedlist.snacks

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

        var visibleIteCount by remember { mutableStateOf(5f) }
        var selectorIndex by remember { mutableStateOf(2f) }
        var itemScaleRange by remember { mutableStateOf(1f) }
        var inactiveItemFraction by remember { mutableStateOf(70f) }

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
        ) { animationProgress, index, item, width ->

            val scale = animationProgress.scale

            SnackCard(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        alpha = scale
                    }
                    .clip(CircleShape)
                    .size(width),
                snack = item
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        AnimatedInfiniteLazyRow(
            modifier = Modifier.fillMaxWidth(),
            items = aspectRatios,
            visibleItemCount = visibleIteCount.toInt(),
            selectorIndex = selectorIndex.toInt(),
            itemScaleRange = itemScaleRange.toInt(),
            showPartialItem = showPartialItem,
            inactiveItemPercent = inactiveItemFraction.toInt(),
            inactiveColor = InactiveColor,
            activeColor = ActiveColor,
            itemContent = { animationProgress, index, item, width ->
                val color = animationProgress.color
                val scale = animationProgress.scale
                Box(
                    modifier = Modifier
                        .scale(scale)
                        .background(color, CircleShape)
                        .size(width),
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