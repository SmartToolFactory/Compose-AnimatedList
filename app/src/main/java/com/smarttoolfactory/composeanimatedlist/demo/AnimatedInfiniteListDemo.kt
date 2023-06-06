package com.smarttoolfactory.composeanimatedlist.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smarttoolfactory.animatedlist.ActiveColor
import com.smarttoolfactory.animatedlist.AnimatedInfiniteLazyColumn
import com.smarttoolfactory.animatedlist.AnimatedInfiniteLazyRow
import com.smarttoolfactory.animatedlist.InactiveColor
import com.smarttoolfactory.composeanimatedlist.ShapeSelection
import com.smarttoolfactory.composeanimatedlist.SnackCard
import com.smarttoolfactory.composeanimatedlist.aspectRatios
import com.smarttoolfactory.composeanimatedlist.snacks
import kotlinx.coroutines.launch

@Preview
@Composable
fun AnimatedInfiniteListDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val coroutineScope = rememberCoroutineScope()

        Spacer(modifier = Modifier.height(40.dp))

        val listWidth = LocalDensity.current.run { 1000.toDp() }
        val spaceBetweenItems = LocalDensity.current.run { 30.toDp() }

        // Demonstrating for setting first visible item of list if we want to
        // make last second item from the end and last item as initial selected item
        val initialVisibleItem = 0
        val visibleItemCount = 5
        val initialSelectedItem = 2

        var selectedItem by remember {
            mutableIntStateOf(initialSelectedItem)
        }

        AnimatedInfiniteLazyRow(
            modifier = Modifier.width(listWidth),
            items = aspectRatios,
            visibleItemCount = visibleItemCount,
            initialFirstVisibleIndex = initialVisibleItem,
            inactiveColor = InactiveColor,
            activeColor = ActiveColor,
            itemContent = { animationProgress, index, item, width, lazyListState ->

                val scale = animationProgress.scale
                val color = animationProgress.color

                selectedItem = animationProgress.itemIndex

                ShapeSelection(modifier = Modifier
                    .graphicsLayer {
                        scaleY = scale
                        alpha = scale
                    }
                    .width(width)
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
                    color = color,
                    shapeModel = item
                )
            }
        )

        Text(
            text = "Selected item ${aspectRatios[selectedItem].title}",
            fontSize = 20.sp,
            color = ActiveColor
        )

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedInfiniteLazyRow(
            modifier = Modifier
                .width(listWidth)
                .padding(10.dp),
            items = aspectRatios,
            visibleItemCount = 5,
            activeItemWidth = 40.dp,
            inactiveItemWidth = 30.dp,
            selectorIndex = 1,
            spaceBetweenItems = 0.dp,
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
        Spacer(modifier = Modifier.height(20.dp))

        AnimatedInfiniteLazyRow(
            modifier = Modifier.width(300.dp),
            items = aspectRatios,
            visibleItemCount = 7,
            selectorIndex = 3,
            inactiveColor = InactiveColor,
            activeColor = ActiveColor,
            inactiveItemPercent = 70,
            itemContent = { animationProgress, index, item, size, lazyListState ->

                val color = animationProgress.color
                val scale = animationProgress.scale

                Box(
                    modifier = Modifier
                        .scale(scale)
                        .background(color, CircleShape)
                        .size(size)
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

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedInfiniteLazyRow(
            modifier = Modifier
                .width(listWidth)
                .height(150.dp),
            items = snacks,
            visibleItemCount = 3,
            spaceBetweenItems = spaceBetweenItems,
            inactiveItemPercent = 70,
            inactiveColor = InactiveColor,
            activeColor = ActiveColor
        ) { animationProgress, index, item, size, lazyListState ->

            val scale = animationProgress.scale

            SnackCard(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        alpha = scale
                    }
                    .fillMaxWidth()
                    .width(size)
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

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedInfiniteLazyColumn(
            items = aspectRatios,
            visibleItemCount = 5,
            inactiveColor = InactiveColor,
            activeColor = ActiveColor,
            selectorIndex = 0,
            inactiveItemPercent = 70,
            itemContent = { animationProgress, index, item, height, lazyListState ->

                val color = animationProgress.color
                val scale = animationProgress.scale

                Box(
                    modifier = Modifier
                        .scale(scale)
                        .background(color, CircleShape)
                        .size(height)
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