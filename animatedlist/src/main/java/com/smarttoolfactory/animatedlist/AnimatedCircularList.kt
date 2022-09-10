@file:OptIn(ExperimentalSnapperApi::class)

package com.smarttoolfactory.animatedlist

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.animatedlist.model.AnimationProgress
import dev.chrisbanes.snapper.ExperimentalSnapperApi
import dev.chrisbanes.snapper.SnapOffsets
import dev.chrisbanes.snapper.rememberSnapperFlingBehavior
import kotlin.math.absoluteValue

/**
 *  Infinite list with color and scale animation for selecting aspect ratio
 *
 * @param items the data list
 * @param visibleItemCount count of items that are visible at any time
 * @param activeItemSize width or height of selected item
 * @param inactiveItemSize width or height of unselected items
 * @param spaceBetweenItems padding between 2 items
 * @param selectorIndex index of selector. When [itemScaleRange] is odd number it's center of
 * selected item, when [itemScaleRange] is even number it's center of item with selecter index
 * and the one next to it
 * @param itemScaleRange range of area of scaling. When this value is odd
 * any item that enters half of item size range subject to  being scaled. When this value is even
 * any item in 2 item size range is subject to being scaled
 * @param activeColor color of selected item
 * @param inactiveColor color of items are not selected
 * @param key a factory of stable and unique keys representing the item. Using the same key
 * for multiple items in the list is not allowed. Type of the key should be saveable
 * via Bundle on Android. If null is passed the position in the list will represent the key.
 * When you specify the key the scroll position will be maintained based on the key, which
 * means if you add/remove items before the current visible item the item with the given key
 * will be kept as the first visible one.
 * @param contentType a factory of the content types for the item. The item compositions of
 * the same type could be reused more efficiently. Note that null is a valid type and items of such
 * type will be considered compatible.
 * @param itemContent the content displayed by a single item
 */
@Composable
fun <T> AnimatedCircularList(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialFistVisibleIndex: Int = Int.MAX_VALUE / 2,
    lazyListState: LazyListState = rememberLazyListState(initialFistVisibleIndex),
    visibleItemCount: Int = 5,
    activeItemSize: Dp,
    inactiveItemSize: Dp,
    spaceBetweenItems: Dp = 4.dp,
    selectorIndex: Int = visibleItemCount / 2,
    itemScaleRange: Int = 1,
    activeColor: Color = Color.Cyan,
    inactiveColor: Color = Color.Gray,
    orientation: Orientation = Orientation.Horizontal,
    key: ((index: Int) -> Any)? = null,
    contentType: (index: Int) -> Any? = { null },
    itemContent: @Composable LazyItemScope.(
        animationProgress: AnimationProgress, index: Int, size: Dp
    ) -> Unit
) {
    val inactiveItemScale = inactiveItemSize.value / activeItemSize.value

    val listDimension =
        activeItemSize * visibleItemCount + spaceBetweenItems * (visibleItemCount - 1)

    val listModifier = if (orientation == Orientation.Horizontal) {
        Modifier.width(listDimension)
    } else {
        Modifier.height(listDimension)
    }

    val visibleItemCountMod = visibleItemCount % 2 == 1

    val flingBehavior = rememberSnapperFlingBehavior(
        lazyListState = lazyListState,
        snapOffsetForItem = if (visibleItemCountMod) SnapOffsets.Center else SnapOffsets.Start
    )

    // Index of selector(item that is selected)  in circular list
    val indexOfSelector = selectorIndex.coerceIn(0, visibleItemCount - 1)

    // number of items
    val totalItemCount = items.size

    val availableSpace = LocalDensity.current.run { listDimension.toPx() }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        AnimatedCircularListImpl(
            modifier = listModifier,
            lazyListState = lazyListState,
            flingBehavior = flingBehavior,
            initialFistVisibleIndex = initialFistVisibleIndex,
            visibleItemCount = visibleItemCount,
            availableSpace = availableSpace,
            itemSize = activeItemSize,
            spaceBetweenItems = spaceBetweenItems,
            totalItemCount = totalItemCount,
            indexOfSelector = indexOfSelector,
            rangeOfSelection = itemScaleRange,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            inactiveItemFraction = inactiveItemScale,
            orientation = orientation,
            key = key,
            contentType = contentType,
            itemContent = itemContent
        )
    }
}

/**
 *  Infinite list with color and scale animation for selecting aspect ratio
 *
 * @param items the data list
 * @param visibleItemCount count of items that are visible at any time
 * @param inactiveItemPercent percentage of scale that items inside [itemScaleRange]
 * can be scaled down to. This is a number between 0 and 100
 * @param spaceBetweenItems padding between 2 items
 * @param selectorIndex index of selector. When [itemScaleRange] is odd number it's center of
 * selected item, when [itemScaleRange] is even number it's center of item with selecter index
 * and the one next to it
 * @param itemScaleRange range of area of scaling. When this value is odd
 * any item that enters half of item size range subject to  being scaled. When this value is even
 * any item in 2 item size range is subject to being scaled
 * @param activeColor color of selected item
 * @param inactiveColor color of items are not selected
 * @param key a factory of stable and unique keys representing the item. Using the same key
 * for multiple items in the list is not allowed. Type of the key should be saveable
 * via Bundle on Android. If null is passed the position in the list will represent the key.
 * When you specify the key the scroll position will be maintained based on the key, which
 * means if you add/remove items before the current visible item the item with the given key
 * will be kept as the first visible one.
 * @param contentType a factory of the content types for the item. The item compositions of
 * the same type could be reused more efficiently. Note that null is a valid type and items of such
 * type will be considered compatible.
 * @param itemContent the content displayed by a single item
 */
@OptIn(ExperimentalSnapperApi::class)
@Composable
fun <T> AnimatedCircularList(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialFistVisibleIndex: Int = Int.MAX_VALUE / 2,
    lazyListState: LazyListState = rememberLazyListState(initialFistVisibleIndex),
    visibleItemCount: Int = 5,
    inactiveItemPercent: Int = 85,
    spaceBetweenItems: Dp = 4.dp,
    selectorIndex: Int = visibleItemCount / 2,
    itemScaleRange: Int = 1,
    activeColor: Color = ActiveColor,
    inactiveColor: Color = InactiveColor,
    orientation: Orientation = Orientation.Horizontal,
    key: ((index: Int) -> Any)? = null,
    contentType: (index: Int) -> Any? = { null },
    itemContent: @Composable LazyItemScope.(
        animationProgress: AnimationProgress, index: Int, size: Dp
    ) -> Unit
) {

    val visibleItemCountMod = visibleItemCount % 2 == 1

    val flingBehavior = rememberSnapperFlingBehavior(
        lazyListState = lazyListState,
        snapOffsetForItem = if (visibleItemCountMod) SnapOffsets.Center else SnapOffsets.Start
    )

    // Index of selector(item that is selected)  in circular list
    val indexOfSelector = selectorIndex.coerceIn(0, visibleItemCount - 1)

    // number of items
    val totalItemCount = items.size

    val listModifier = if (orientation == Orientation.Horizontal) {
        modifier.fillMaxWidth()
    } else {
        modifier.fillMaxHeight()
    }.border(1.dp, Color.Green)

    BoxWithConstraints(modifier = listModifier) {

        val availableSpace =
            if (orientation == Orientation.Horizontal) constraints.maxWidth.toFloat() else
                constraints.maxHeight.toFloat()

        val density = LocalDensity.current
        val spaceBetweenItemsPx = density.run { spaceBetweenItems.toPx() }

        val itemSize =
            (availableSpace - spaceBetweenItemsPx * (visibleItemCount - 1)) / visibleItemCount
        val itemSizeDp = density.run { itemSize.toDp() }

        AnimatedCircularListImpl(
            modifier = listModifier,
            lazyListState = lazyListState,
            flingBehavior = flingBehavior,
            initialFistVisibleIndex = initialFistVisibleIndex,
            visibleItemCount = visibleItemCount,
            availableSpace = availableSpace,
            itemSize = itemSizeDp,
            spaceBetweenItems = spaceBetweenItems,
            totalItemCount = totalItemCount,
            indexOfSelector = indexOfSelector,
            rangeOfSelection = itemScaleRange.coerceAtMost(visibleItemCount),
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            inactiveItemFraction = inactiveItemPercent.coerceIn(0, 100) / 100f,
            orientation = orientation,
            key = key,
            contentType = contentType,
            itemContent = itemContent
        )
    }
}

@Composable
private fun AnimatedCircularListImpl(
    modifier: Modifier,
    lazyListState: LazyListState,
    flingBehavior: FlingBehavior,
    initialFistVisibleIndex: Int,
    visibleItemCount: Int,
    availableSpace: Float,
    itemSize: Dp,
    spaceBetweenItems: Dp,
    totalItemCount: Int,
    indexOfSelector: Int,
    rangeOfSelection: Int,
    activeColor: Color,
    inactiveColor: Color,
    inactiveItemFraction: Float,
    orientation: Orientation,
    key: ((index: Int) -> Any)?,
    contentType: (index: Int) -> Any?,
    itemContent: @Composable LazyItemScope.(
        animationProgress: AnimationProgress, index: Int, size: Dp
    ) -> Unit
) {

    val density = LocalDensity.current
    val spaceBetweenItemsPx = density.run { spaceBetweenItems.toPx() }

    val content: LazyListScope.() -> Unit = {
        items(
            count = Int.MAX_VALUE, key = key, contentType = contentType
        ) { globalIndex ->
            AnimatedItems(
                lazyListState = lazyListState,
                initialFistVisibleIndex = initialFistVisibleIndex,
                indexOfSelector = indexOfSelector,
                rangeOfSelection = rangeOfSelection,
                globalIndex = globalIndex,
                availableSpace = availableSpace,
                itemSize = itemSize,
                spaceBetweenItems = spaceBetweenItemsPx,
                visibleItemCount = visibleItemCount,
                totalItemCount = totalItemCount,
                inactiveItemScale = inactiveItemFraction,
                inactiveColor = inactiveColor,
                activeColor = activeColor
            ) { animationProgress: AnimationProgress, size: Dp ->
                itemContent(animationProgress, globalIndex % totalItemCount, size)
            }
        }
    }

    if (orientation == Orientation.Horizontal) {
        LazyRow(
            modifier = modifier,
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(spaceBetweenItems),
            flingBehavior = flingBehavior
        ) {
            content()
        }
    } else {
        LazyColumn(
            modifier = modifier,
            state = lazyListState,
            verticalArrangement = Arrangement.spacedBy(spaceBetweenItems),
            flingBehavior = flingBehavior
        ) {
            content()
        }
    }
}

@Composable
private fun LazyItemScope.AnimatedItems(
    lazyListState: LazyListState,
    initialFistVisibleIndex: Int,
    indexOfSelector: Int,
    rangeOfSelection: Int,
    globalIndex: Int,
    availableSpace: Float,
    itemSize: Dp,
    spaceBetweenItems: Float,
    visibleItemCount: Int,
    totalItemCount: Int,
    inactiveItemScale: Float,
    inactiveColor: Color,
    activeColor: Color,
    itemContent: @Composable LazyItemScope.(animationProgress: AnimationProgress, size: Dp) -> Unit

) {
    var selectedIndex by remember {
        mutableStateOf(-1)
    }

    val itemSizePx = LocalDensity.current.run { itemSize.toPx() }

    val animationData by remember {
        derivedStateOf {
            val animationData = getAnimationProgress(
                lazyListState = lazyListState,
                initialFistVisibleIndex = initialFistVisibleIndex,
                indexOfSelector = indexOfSelector,
                itemScaleRange = rangeOfSelection,
                globalIndex = globalIndex,
                selectedIndex = selectedIndex,
                availableSpace = availableSpace,
                itemSize = itemSizePx,
                spaceBetweenItems = spaceBetweenItems,
                visibleItemCount = visibleItemCount,
                totalItemCount = totalItemCount,
                inactiveScale = inactiveItemScale,
                inactiveColor = inactiveColor,
                activeColor = activeColor
            )

            selectedIndex = animationData.globalItemIndex
            animationData
        }
    }
    itemContent(animationData, itemSize)
}

/**
 * get color, scale and selected index for scroll progress for infinite or circular list with
 * [Int.MAX_VALUE] global index count
 *
 * @param lazyListState A state object that can be hoisted to control and observe scrolling
 * @param initialFistVisibleIndex index of item that is at the beginning of the list initially
 * @param indexOfSelector global index of element of selector of infinite items. Item with
 * this index is selected item
 * @param globalIndex index of current item. This index changes for every item in list
 * that calls this function
 * @param selectedIndex global index of currently selected item. This index changes only
 * when selected item is changed due to scroll
 * @param availableSpace space that is reserved for items and space between items. This
 * param is list width/height minus padding values in respective axis.
 * @param itemSize width/height of each item
 * @param spaceBetweenItems space between each item
 * @param visibleItemCount count of visible items on screen
 * @param totalItemCount count of items that are displayed in circular list
 * @param inactiveScale lower scale that items can be scaled to. It should be less than 1f
 * @param inactiveColor color of items when they are not selected
 * @param activeColor color of item that is selected
 */
private fun getAnimationProgress(
    lazyListState: LazyListState,
    initialFistVisibleIndex: Int,
    indexOfSelector: Int,
    itemScaleRange: Int,
    globalIndex: Int,
    selectedIndex: Int,
    availableSpace: Float,
    itemSize: Float,
    spaceBetweenItems: Float,
    visibleItemCount: Int,
    totalItemCount: Int,
    inactiveScale: Float,
    inactiveColor: Color,
    activeColor: Color,
): AnimationProgress {

    val visibleItems = lazyListState.layoutInfo.visibleItemsInfo
    val currentItem: LazyListItemInfo? = visibleItems.firstOrNull { it.index == globalIndex }

    val isRangeOfSelectionOdd = itemScaleRange % 2 == 1

    // Position of center of selector item
    // Item that is closest to this position is returned as selected item
    val selectorPosition = if (isRangeOfSelectionOdd) {
        (indexOfSelector) * (itemSize + spaceBetweenItems) + itemSize / 2
    } else {
        // When items that can be scaled is even number selector position is
        // space between index of selector and next item
        (indexOfSelector) * (itemSize + spaceBetweenItems) + itemSize + spaceBetweenItems / 2
    }

    // Get offset of each item relative to start of list x=0 or y=0 position
    val itemCenter = (itemSize / 2).toInt() + if (currentItem != null) {
        currentItem.offset
    } else {
        // Convert global indexes to indexes in range of 0..visibleItemCount
        // when current item is null in initial run
        val localIndex =
            (visibleItemCount + globalIndex - initialFistVisibleIndex) % visibleItemCount
        (localIndex * itemSize + localIndex * spaceBetweenItems).toInt()
    }

    // get scale of current item based on distance between items center to selector
    val scale = getScale(
        rangeOfSelection = itemScaleRange,
        selectorPosition = selectorPosition,
        itemCenter = itemCenter,
        inactiveScale = inactiveScale,
        itemSize = itemSize,
        spaceBetweenItems = spaceBetweenItems
    )

    // This is the fraction between lower bound and 1f. If lower bound is .9f we have
    // range of 0.9f..1f for scale calculation
    val scalingInterval = 1f - inactiveScale

    // Scale for color when scale is at lower bound color scale is zero
    // when scale reaches upper bound(1f) color scale is 1f which is target color
    // when argEvaluator evaluates color
    val colorScale = (scale - inactiveScale) / scalingInterval

    var distance = Int.MAX_VALUE

    var globalSelectedIndex = selectedIndex

    visibleItems.forEach {
        val itemDistanceToSelector = ((it.offset + itemSize / 2) - selectorPosition).absoluteValue
        if (itemDistanceToSelector < distance) {
            distance = itemDistanceToSelector.toInt()
            globalSelectedIndex = it.index
        }
    }

    // Index of item in list. If list has 7 items initial item index is 3
    // When selector changes we get what it(in infinite list) corresponds to in item list
    val itemIndex = if (globalSelectedIndex > 0) {
        globalSelectedIndex % totalItemCount
    } else {
        indexOfSelector
    }

    // Interpolate color between start and end color based on color scale
    val color = lerp(inactiveColor, activeColor, colorScale)

    return AnimationProgress(
        scale = scale,
        color = color,
        itemOffset = itemCenter,
        itemFraction = itemCenter / availableSpace,
        globalItemIndex = globalSelectedIndex,
        itemIndex = itemIndex
    )
}

/**
 * get scale based on whether it's initial run of list,
 * [LazyListState]'S [LazyListLayoutInfo.visibleItemsInfo]  list is empty,
 * or current scroll state of list.
 *
 * @param inactiveScale lower scale that items can be scaled to. It should be less than 1f
 * @param itemSize width/height of each item
 * @param spaceBetweenItems space between each item
 * @param selectorPosition position of selector or selected item
 * @param itemCenter offset of item from start of the list's x or y zero position
 */
private fun getScale(
    rangeOfSelection: Int,
    selectorPosition: Float,
    itemCenter: Int,
    inactiveScale: Float,
    itemSize: Float,
    spaceBetweenItems: Float,
): Float {

    // Check how far this item is to selector index.
    val distanceToSelector = (selectorPosition - itemCenter).absoluteValue
    // When offset of an item is in this region it gets scaled.
    // region size is calculated as
    // half space + (item width or height) + half space
    val scaleRegionSize = (itemSize + spaceBetweenItems) * (rangeOfSelection + 1) / 2

    println(
        "🔥rangeOfSelection: ${rangeOfSelection}, " +

                "selectorPosition: ${selectorPosition}, " +
                "itemOffset: ${itemCenter}, " +
                "distanceToSelector: ${distanceToSelector}, " +
                "scaleRegionSize: ${scaleRegionSize}"
    )

    return calculateScale(
        distanceToSelector,
        scaleRegionSize,
        inactiveScale
    )
}

/**
 * Calculate scale that is inside scale region based on [minimum]..1f range
 */
private fun calculateScale(
    distanceToSelector: Float,
    scaleRegionSize: Float,
    minimum: Float
): Float {
    return if (distanceToSelector < scaleRegionSize) {

        // item is in scale region. Check where exactly it is in this region
        val fraction = (scaleRegionSize - distanceToSelector) / scaleRegionSize
        println(
            "😜 calculateScale() " +
                    "scaleRegionSize: $scaleRegionSize, " +
                    "distanceToSelector: $distanceToSelector, " +
                    "fraction: $fraction"
        )

        // scale fraction between start and 1f.
        // If start is .9f and fraction is 50% our scale is .9f + .1f*50/100 = .95f
        lerp(start = minimum, stop = 1f, fraction = fraction)
    } else {
        minimum

    }.coerceIn(minimum, 1f)
}

/**
 * [Linear Interpolation](https://en.wikipedia.org/wiki/Linear_interpolation) function that moves
 * amount from it's current position to start and amount
 * @param start of interval
 * @param stop of interval
 * @param fraction closed unit interval [0f, 1f]
 */
private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}

