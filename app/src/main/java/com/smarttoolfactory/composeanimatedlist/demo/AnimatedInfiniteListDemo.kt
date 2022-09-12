package com.smarttoolfactory.composeanimatedlist.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
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


@Composable
fun AnimatedInfiniteListDemo() {
    Column(
        modifier = Modifier.fillMaxSize().background(Color.DarkGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))


        var selectedItem by remember {
            mutableStateOf(aspectRatios.size / 2)
        }

        val listWidth = LocalDensity.current.run { 1000.toDp() }
        val spaceBetweenItems = LocalDensity.current.run { 30.toDp() }

        AnimatedInfiniteLazyRow(
            modifier = Modifier.width(listWidth),
            items = aspectRatios,
            visibleItemCount = 5,
            inactiveColor = InactiveColor,
            activeColor = ActiveColor,
            itemContent = { animationProgress, index, item, width ->

                val scale = animationProgress.scale
                val color = animationProgress.color

                selectedItem = animationProgress.itemIndex

                ShapeSelection(modifier = Modifier
                    .graphicsLayer {
                        scaleY = scale
                        alpha = scale
                    }
                    .width(width),
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
        Spacer(modifier = Modifier.height(20.dp))

        AnimatedInfiniteLazyRow(
            modifier = Modifier.width(300.dp),
            items = aspectRatios,
            visibleItemCount = 7,
            selectorIndex = 3,
            inactiveColor = InactiveColor,
            activeColor = ActiveColor,
            inactiveItemPercent = 70,
            itemContent = { animationProgress, index, item, size ->

                val color = animationProgress.color
                val scale = animationProgress.scale

                Box(
                    modifier = Modifier
                        .scale(scale)
                        .background(color, CircleShape)
                        .size(size),
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
        ) { animationProgress, index, item, size ->

            val scale = animationProgress.scale

            SnackCard(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        alpha = scale
                    }
                    .fillMaxWidth()
                    .width(size),
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
            itemContent = { animationProgress, index, item, height ->

                val color = animationProgress.color
                val scale = animationProgress.scale

                Box(
                    modifier = Modifier
                        .scale(scale)
                        .background(color, CircleShape)
                        .size(height),
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