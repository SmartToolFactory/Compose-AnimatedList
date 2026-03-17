package com.smarttoolfactory.composeanimatedlist.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttoolfactory.animatedlist.ActiveColor
import com.smarttoolfactory.animatedlist.AnimatedFiniteLazyRow
import com.smarttoolfactory.animatedlist.FiniteListMode
import com.smarttoolfactory.animatedlist.InactiveColor
import com.smarttoolfactory.composeanimatedlist.ShapeSelection
import com.smarttoolfactory.composeanimatedlist.aspectRatios
import com.smarttoolfactory.composeanimatedlist.ui.theme.ComposeAnimatedListTheme
import kotlinx.coroutines.launch

@Preview(
    name = "Finite Params Preview",
    widthDp = 420,
    heightDp = 760,
    showBackground = true,
    backgroundColor = 0xFF202020
)
@Composable
private fun AnimatedFiniteListParamsPreview() {
    AnimatedFiniteListParamsDemo()
}

@Composable
fun AnimatedFiniteListParamsDemo() {
    ComposeAnimatedListTheme {
        AnimatedFiniteListParamsLayout()
    }
}

@Composable
private fun AnimatedFiniteListParamsLayout() {
    val coroutineScope = rememberCoroutineScope()
    val controlsScrollState = rememberScrollState()

    var selectedIndex by remember { mutableIntStateOf(2) }
    var initialSelectedIndex by remember { mutableFloatStateOf(2f) }
    var visibleItemCount by remember { mutableFloatStateOf(5f) }
    var selectorIndex by remember { mutableFloatStateOf(2f) }
    var itemScaleRange by remember { mutableFloatStateOf(1f) }
    var inactiveItemPercent by remember { mutableFloatStateOf(72f) }
    var circularMode by remember { mutableStateOf(false) }
    var showPartialItem by remember { mutableStateOf(false) }

    val maxVisibleCount = (aspectRatios.size - 1).coerceAtLeast(3)
    val visibleCount = visibleItemCount.toInt().coerceIn(3, maxVisibleCount)
    val resolvedInitialSelectedIndex = initialSelectedIndex.toInt().coerceIn(0, aspectRatios.lastIndex)
    val resolvedSelectorIndex = selectorIndex.toInt().coerceIn(0, visibleCount - 1)
    val resolvedItemScaleRange = itemScaleRange.toInt().coerceIn(1, visibleCount)
    val mode = if (circularMode) FiniteListMode.Circular else FiniteListMode.Bounded

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Finite List Params",
            color = ActiveColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Selected: ${aspectRatios[selectedIndex].title}",
            color = ActiveColor,
            fontSize = 18.sp
        )

        key(
            resolvedInitialSelectedIndex,
            visibleCount,
            resolvedSelectorIndex,
            resolvedItemScaleRange,
            inactiveItemPercent.toInt(),
            circularMode,
            showPartialItem
        ) {
            AnimatedFiniteLazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                items = aspectRatios,
                initialSelectedIndex = resolvedInitialSelectedIndex,
                visibleItemCount = visibleCount,
                inactiveItemPercent = inactiveItemPercent.toInt(),
                mode = mode,
                selectorIndex = resolvedSelectorIndex,
                itemScaleRange = resolvedItemScaleRange,
                showPartialItem = showPartialItem,
                spaceBetweenItems = 12.dp,
                inactiveColor = InactiveColor,
                activeColor = ActiveColor
            ) { animationProgress, _, item, size, lazyListState ->
                val scale = animationProgress.scale
                val color = animationProgress.color

                selectedIndex = animationProgress.itemIndex

                ShapeSelection(
                    modifier = Modifier
                        .graphicsLayer {
                            scaleY = scale
                            alpha = scale
                        }
                        .width(size)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            coroutineScope.launch {
                                lazyListState.animateScrollBy(animationProgress.distanceToSelector)
                            }
                        },
                    color = color,
                    shapeModel = item
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(controlsScrollState),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "This demo keeps the live list visible above while the controls scroll below.",
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 14.sp
            )

            Text(
                text = "Mode: ${mode.name}",
                color = MaterialTheme.colorScheme.inversePrimary,
                fontSize = 18.sp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Circular",
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = circularMode,
                    onCheckedChange = { circularMode = it }
                )
            }

            PreviewSlider(
                label = "Initial Selected Index",
                value = initialSelectedIndex,
                valueText = resolvedInitialSelectedIndex.toString(),
                onValueChange = { initialSelectedIndex = it },
                valueRange = 0f..aspectRatios.lastIndex.toFloat(),
                steps = (aspectRatios.lastIndex - 1).coerceAtLeast(0)
            )

            PreviewSlider(
                label = "Visible Item Count",
                value = visibleItemCount,
                valueText = visibleCount.toString(),
                onValueChange = { visibleItemCount = it },
                valueRange = 3f..maxVisibleCount.toFloat(),
                steps = (maxVisibleCount - 4).coerceAtLeast(0)
            )

            PreviewSlider(
                label = "Selector Index",
                value = selectorIndex.coerceIn(0f, (visibleCount - 1).toFloat()),
                valueText = resolvedSelectorIndex.toString(),
                onValueChange = { selectorIndex = it },
                valueRange = 0f..(visibleCount - 1).toFloat(),
                steps = (visibleCount - 2).coerceAtLeast(0)
            )

            PreviewSlider(
                label = "Scale Range",
                value = itemScaleRange.coerceIn(1f, visibleCount.toFloat()),
                valueText = resolvedItemScaleRange.toString(),
                onValueChange = { itemScaleRange = it },
                valueRange = 1f..visibleCount.toFloat(),
                steps = (visibleCount - 2).coerceAtLeast(0)
            )

            PreviewSlider(
                label = "Inactive Item Percent",
                value = inactiveItemPercent,
                valueText = inactiveItemPercent.toInt().toString(),
                onValueChange = { inactiveItemPercent = it },
                valueRange = 50f..100f
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Partial Items",
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = showPartialItem,
                    onCheckedChange = { showPartialItem = it }
                )
            }
        }
    }
}

@Composable
private fun PreviewSlider(
    label: String,
    value: Float,
    valueText: String,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int = 0
) {
    Text(
        text = "$label: $valueText",
        color = MaterialTheme.colorScheme.inversePrimary,
        fontSize = 18.sp
    )

    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        steps = steps
    )
}
