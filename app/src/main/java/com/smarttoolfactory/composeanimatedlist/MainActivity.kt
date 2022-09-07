package com.smarttoolfactory.composeanimatedlist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
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
import com.smarttoolfactory.animatedlist.AnimatedCircularList
import com.smarttoolfactory.animatedlist.InactiveColor
import com.smarttoolfactory.composeanimatedlist.ui.theme.ComposeAnimatedListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeAnimatedListTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.DarkGray)
                    ) {
                        AnimatedListSamples()
                    }
                }
            }
        }
    }
}

@Composable
private fun AnimatedListSamples() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))


        var selectedItem by remember {
            mutableStateOf(aspectRatios.size / 2)
        }

        val listWidth = LocalDensity.current.run { 1000.toDp() }
        val spaceBetweenItems = LocalDensity.current.run { 30.toDp() }

        AnimatedCircularList(
            modifier = Modifier.width(listWidth),
            items = aspectRatios,
            visibleItemCount = 5,
            inactiveColor = InactiveColor,
            activeColor = ActiveColor,
            itemContent = { animationProgress, index, width ->

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
                    shapeModel = aspectRatios[index]
                )
            }
        )

        Text(
            text = "Selected item ${aspectRatios[selectedItem].title}",
            fontSize = 20.sp,
            color = ActiveColor
        )

        Spacer(modifier = Modifier.height(20.dp))


        AnimatedCircularList(
            modifier = Modifier
                .width(listWidth)
                .padding(10.dp),
            items = aspectRatios,
            visibleItemCount = 5,
            activeItemSize = 40.dp,
            inactiveItemSize = 30.dp,
            selectorIndex = 1,
            spaceBetweenItems = 0.dp,
            inactiveColor = InactiveColor,
            activeColor = ActiveColor,
            itemContent = { animationProgress, index, size ->
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

        AnimatedCircularList(
            modifier = Modifier.width(300.dp),
            items = aspectRatios,
            visibleItemCount = 7,
            selectorIndex = 3,
            inactiveColor = InactiveColor,
            activeColor = ActiveColor,
            inactiveItemScale = .7f,
            itemContent = { animationProgress, index, size ->

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

        AnimatedCircularList(
            modifier = Modifier
                .width(listWidth)
                .height(150.dp),
            items = aspectRatios,
            visibleItemCount = 3,
            spaceBetweenItems = spaceBetweenItems,
            orientation = Orientation.Horizontal,
            inactiveItemScale = .7f,
            inactiveColor = InactiveColor,
            activeColor = ActiveColor
        ) { animationProgress, index, size ->

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
                snack = snacks[index]
            )

        }

        Spacer(modifier = Modifier.height(20.dp))

        AnimatedCircularList(
            items = aspectRatios,
            visibleItemCount = 5,
            inactiveColor = InactiveColor,
            activeColor = ActiveColor,
            orientation = Orientation.Vertical,
            selectorIndex = 0,
            inactiveItemScale = .7f,
            itemContent = { animationProgress, index, size ->

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
    }
}