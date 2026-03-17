package com.smarttoolfactory.composeanimatedlist.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttoolfactory.animatedlist.ActiveColor
import com.smarttoolfactory.animatedlist.AnimatedFiniteLazyColumn
import com.smarttoolfactory.animatedlist.AnimatedFiniteLazyRow
import com.smarttoolfactory.animatedlist.FiniteListMode
import com.smarttoolfactory.animatedlist.InactiveColor
import com.smarttoolfactory.composeanimatedlist.ShapeSelection
import com.smarttoolfactory.composeanimatedlist.aspectRatios
import com.smarttoolfactory.composeanimatedlist.ui.theme.ComposeAnimatedListTheme
import kotlinx.coroutines.launch

@Composable
fun AnimatedFiniteListDemo() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        var boundedRowSelectedIndex by remember { mutableIntStateOf(0) }
        var circularRowSelectedIndex by remember { mutableIntStateOf(0) }
        var boundedColumnSelectedIndex by remember { mutableIntStateOf(0) }
        var circularColumnSelectedIndex by remember { mutableIntStateOf(0) }

        Text(
            text = "Finite Lists",
            color = ActiveColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        FiniteSectionTitle(
            title = "Bounded Row",
            selectedIndex = boundedRowSelectedIndex
        )
        FiniteShapeRow(
            mode = FiniteListMode.Bounded,
            onSelectedIndexChanged = { boundedRowSelectedIndex = it }
        )

        FiniteSectionTitle(
            title = "Circular Row",
            selectedIndex = circularRowSelectedIndex
        )
        FiniteShapeRow(
            mode = FiniteListMode.Circular,
            onSelectedIndexChanged = { circularRowSelectedIndex = it }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FiniteSectionTitle(
                    title = "Bounded Column",
                    selectedIndex = boundedColumnSelectedIndex
                )
                FiniteShapeColumn(
                    mode = FiniteListMode.Bounded,
                    onSelectedIndexChanged = { boundedColumnSelectedIndex = it }
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FiniteSectionTitle(
                    title = "Circular Column",
                    selectedIndex = circularColumnSelectedIndex
                )
                FiniteShapeColumn(
                    mode = FiniteListMode.Circular,
                    onSelectedIndexChanged = { circularColumnSelectedIndex = it }
                )
            }
        }
    }
}

@Preview(
    name = "Finite Bounded Row",
    widthDp = 420,
    heightDp = 180,
    showBackground = true,
    backgroundColor = 0xFF202020
)
@Composable
private fun AnimatedFiniteBoundedRowPreview() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    FinitePreviewContainer {
        FiniteSectionTitle(
            title = "Bounded Row",
            selectedIndex = selectedIndex
        )
        FiniteShapeRow(
            mode = FiniteListMode.Bounded,
            onSelectedIndexChanged = { selectedIndex = it }
        )
    }
}

@Preview(
    name = "Finite Circular Row",
    widthDp = 420,
    heightDp = 180,
    showBackground = true,
    backgroundColor = 0xFF202020
)
@Composable
private fun AnimatedFiniteCircularRowPreview() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    FinitePreviewContainer {
        FiniteSectionTitle(
            title = "Circular Row",
            selectedIndex = selectedIndex
        )
        FiniteShapeRow(
            mode = FiniteListMode.Circular,
            onSelectedIndexChanged = { selectedIndex = it }
        )
    }
}

@Preview(
    name = "Finite Bounded Column",
    widthDp = 180,
    heightDp = 420,
    showBackground = true,
    backgroundColor = 0xFF202020
)
@Composable
private fun AnimatedFiniteBoundedColumnPreview() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    FinitePreviewContainer {
        FiniteSectionTitle(
            title = "Bounded Column",
            selectedIndex = selectedIndex
        )
        FiniteShapeColumn(
            mode = FiniteListMode.Bounded,
            onSelectedIndexChanged = { selectedIndex = it }
        )
    }
}

@Preview(
    name = "Finite Circular Column",
    widthDp = 180,
    heightDp = 420,
    showBackground = true,
    backgroundColor = 0xFF202020
)
@Composable
private fun AnimatedFiniteCircularColumnPreview() {
    var selectedIndex by remember { mutableIntStateOf(0) }

    FinitePreviewContainer {
        FiniteSectionTitle(
            title = "Circular Column",
            selectedIndex = selectedIndex
        )
        FiniteShapeColumn(
            mode = FiniteListMode.Circular,
            onSelectedIndexChanged = { selectedIndex = it }
        )
    }
}

@Composable
private fun FinitePreviewContainer(
    content: @Composable () -> Unit
) {
    ComposeAnimatedListTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            content()
        }
    }
}

@Composable
private fun FiniteSectionTitle(
    title: String,
    selectedIndex: Int
) {
    Text(
        text = "$title  Selected: ${aspectRatios[selectedIndex].title}",
        color = MaterialTheme.colorScheme.inversePrimary,
        fontSize = 18.sp
    )
}

@Composable
private fun FiniteShapeRow(
    mode: FiniteListMode,
    onSelectedIndexChanged: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    AnimatedFiniteLazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        items = aspectRatios,
        initialSelectedIndex = 2,
        visibleItemCount = 5,
        inactiveItemPercent = 72,
        mode = mode,
        spaceBetweenItems = 12.dp,
        inactiveColor = InactiveColor,
        activeColor = ActiveColor
    ) { animationProgress, index, item, size, lazyListState ->
        val scale = animationProgress.scale
        val color = animationProgress.color

        onSelectedIndexChanged(animationProgress.itemIndex)

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

@Composable
private fun FiniteShapeColumn(
    mode: FiniteListMode,
    onSelectedIndexChanged: (Int) -> Unit
) {
    val columnItems = aspectRatios.take(5)
    val coroutineScope = rememberCoroutineScope()

    AnimatedFiniteLazyColumn(
        modifier = Modifier
            .width(88.dp)
            .height(320.dp),
        items = columnItems,
        initialSelectedIndex = 0,
        visibleItemCount = 5,
        inactiveItemPercent = 70,
        mode = mode,
        selectorIndex = 0,
        spaceBetweenItems = 12.dp,
        inactiveColor = InactiveColor,
        activeColor = ActiveColor
    ) { animationProgress, index, item, size, lazyListState ->
        val scale = animationProgress.scale
        val color = animationProgress.color

        onSelectedIndexChanged(animationProgress.itemIndex)

        Box(
            modifier = Modifier
                .scale(scale)
                .background(color, CircleShape)
                .size(size)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    coroutineScope.launch {
                        lazyListState.animateScrollBy(animationProgress.distanceToSelector)
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.title,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
