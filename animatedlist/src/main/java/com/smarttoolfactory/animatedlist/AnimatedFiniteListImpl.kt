package com.smarttoolfactory.animatedlist

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.animatedlist.model.AnimationProgress
import kotlin.math.roundToInt

private data class FinitePlaceholderKey(
    val adapterIndex: Int
)

private data object FinitePlaceholderContentType

@Composable
internal fun <T> AnimatedFiniteList(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialSelectedIndex: Int = 0,
    visibleItemCount: Int = 5,
    activeItemSize: Dp,
    inactiveItemSize: Dp,
    spaceBetweenItems: Dp = 16.dp,
    selectorIndex: Int = -1,
    itemScaleRange: Int = 1,
    showPartialItem: Boolean = false,
    activeColor: Color = ActiveColor,
    inactiveColor: Color = InactiveColor,
    orientation: Orientation = Orientation.Horizontal,
    key: ((index: Int) -> Any)? = null,
    contentType: (index: Int) -> Any? = { null },
    itemContent: @Composable LazyItemScope.(
        animationProgress: AnimationProgress,
        index: Int,
        item: T,
        size: Dp,
        lazyListState: LazyListState
    ) -> Unit
) {
    if (items.isEmpty()) return

    val effectiveVisibleItemCount = clampVisibleItemCount(items.size, visibleItemCount)
    val disableScroll = shouldDisableScrollForShortList(items.size, visibleItemCount)
    val clampedInitialSelectedIndex = resolveInitialSelectedIndex(items.size, initialSelectedIndex)
    val inactiveItemScale = inactiveItemSize.value / activeItemSize.value
    val adapterLayout = if (disableScroll) {
        null
    } else {
        calculateFiniteAdapterLayout(
            itemCount = items.size,
            visibleItemCount = effectiveVisibleItemCount,
            selectorIndex = selectorIndex
        )
    }

    val listDimension =
        activeItemSize * effectiveVisibleItemCount + spaceBetweenItems * (effectiveVisibleItemCount - 1)

    val listModifier = if (orientation == Orientation.Horizontal) {
        Modifier.width(listDimension)
    } else {
        Modifier.height(listDimension)
    }

    val density = LocalDensity.current
    val availableSpace = density.run { listDimension.toPx() }
    val itemSizePx = density.run { activeItemSize.toPx() }
    val spaceBetweenItemsPx = density.run { spaceBetweenItems.toPx() }
    val isRangeOfSelectionOdd = isRangeOfSelectionOdd(itemScaleRange, showPartialItem)
    val selectorPosition = getSelectorPosition(
        isRangeOfSelectionOdd = isRangeOfSelectionOdd,
        indexOfSelector = selectorIndex,
        itemSize = itemSizePx,
        spaceBetweenItems = spaceBetweenItemsPx
    )

    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = if (disableScroll) 0 else clampedInitialSelectedIndex
    )

    val flingBehavior = if (disableScroll) {
        ScrollableDefaults.flingBehavior()
    } else {
        rememberSnapFlingBehavior(
            lazyListState = listState,
            snapPosition = SnapPosition.Start
        )
    }

    AnimatedFiniteListContent(
        modifier = modifier.then(listModifier),
        items = items,
        lazyListState = listState,
        flingBehavior = flingBehavior,
        adapterLayout = adapterLayout,
        availableSpace = availableSpace,
        itemSize = activeItemSize,
        itemSizePx = itemSizePx,
        spaceBetweenItems = spaceBetweenItems,
        spaceBetweenItemsPx = spaceBetweenItemsPx,
        selectorPosition = selectorPosition,
        itemScaleRange = itemScaleRange,
        showPartialItem = showPartialItem,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        inactiveItemFraction = inactiveItemScale,
        isRangeOfSelectionOdd = isRangeOfSelectionOdd,
        orientation = orientation,
        key = key,
        contentType = contentType,
        disableScroll = disableScroll,
        itemContent = itemContent
    )
}

@Composable
internal fun <T> AnimatedFiniteList(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialSelectedIndex: Int = 0,
    visibleItemCount: Int = 5,
    inactiveItemPercent: Int = 85,
    spaceBetweenItems: Dp = 16.dp,
    selectorIndex: Int = -1,
    itemScaleRange: Int = 1,
    showPartialItem: Boolean = false,
    activeColor: Color = ActiveColor,
    inactiveColor: Color = InactiveColor,
    orientation: Orientation = Orientation.Horizontal,
    key: ((index: Int) -> Any)? = null,
    contentType: (index: Int) -> Any? = { null },
    itemContent: @Composable LazyItemScope.(
        animationProgress: AnimationProgress,
        index: Int,
        item: T,
        size: Dp,
        lazyListState: LazyListState
    ) -> Unit
) {
    if (items.isEmpty()) return

    val requestedVisibleItemCount = visibleItemCount.coerceAtLeast(1)
    val effectiveVisibleItemCount = clampVisibleItemCount(items.size, requestedVisibleItemCount)
    val clampedInitialSelectedIndex = resolveInitialSelectedIndex(items.size, initialSelectedIndex)

    val listModifier = if (orientation == Orientation.Horizontal) {
        modifier.fillMaxWidth()
    } else {
        modifier.fillMaxHeight()
    }

    ListSizeSubcomposeLayout(
        modifier = listModifier,
        mainContent = {
            Box(modifier = listModifier)
        }
    ) {
        val width = it.width
        val height = it.height
        val availableSpace = if (orientation == Orientation.Horizontal) width else height
        val density = LocalDensity.current
        val spaceBetweenItemsPx = density.run { spaceBetweenItems.toPx() }
        val itemSize =
            (availableSpace - spaceBetweenItemsPx * (requestedVisibleItemCount - 1)) / requestedVisibleItemCount
        val itemSizeDp = density.run { itemSize.toDp() }
        val inactiveItemSizeDp = density.run { (itemSize * inactiveItemPercent / 100).toDp() }

        AnimatedFiniteList(
            modifier = density.run {
                if (orientation == Orientation.Horizontal) Modifier.width(width.toDp())
                else Modifier.height(height.toDp())
            },
            items = items,
            initialSelectedIndex = clampedInitialSelectedIndex,
            visibleItemCount = effectiveVisibleItemCount,
            activeItemSize = itemSizeDp,
            inactiveItemSize = inactiveItemSizeDp,
            spaceBetweenItems = spaceBetweenItems,
            selectorIndex = selectorIndex,
            itemScaleRange = itemScaleRange,
            showPartialItem = showPartialItem,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            orientation = orientation,
            key = key,
            contentType = contentType,
            itemContent = itemContent
        )
    }
}

@Composable
private fun <T> AnimatedFiniteListContent(
    modifier: Modifier,
    items: List<T>,
    lazyListState: LazyListState,
    flingBehavior: FlingBehavior,
    adapterLayout: FiniteAdapterLayout?,
    availableSpace: Float,
    itemSize: Dp,
    itemSizePx: Float,
    spaceBetweenItems: Dp,
    spaceBetweenItemsPx: Float,
    selectorPosition: Float,
    itemScaleRange: Int,
    showPartialItem: Boolean,
    activeColor: Color,
    inactiveColor: Color,
    inactiveItemFraction: Float,
    isRangeOfSelectionOdd: Boolean,
    orientation: Orientation,
    key: ((index: Int) -> Any)?,
    contentType: (index: Int) -> Any?,
    disableScroll: Boolean,
    itemContent: @Composable LazyItemScope.(
        animationProgress: AnimationProgress,
        index: Int,
        item: T,
        size: Dp,
        lazyListState: LazyListState
    ) -> Unit
) {
    val adapterKey = remember(key, adapterLayout, items.size) {
        if (adapterLayout == null) {
            key
        } else if (key == null) {
            null
        } else {
            { adapterIndex: Int ->
                adapterIndexToItemIndex(
                    adapterIndex = adapterIndex,
                    itemCount = items.size,
                    leadingPlaceholderCount = adapterLayout.leadingPlaceholderCount
                )?.let(key) ?: FinitePlaceholderKey(adapterIndex)
            }
        }
    }

    val adapterContentType = remember(contentType, adapterLayout, items.size) {
        if (adapterLayout == null) {
            contentType
        } else {
            { adapterIndex: Int ->
                adapterIndexToItemIndex(
                    adapterIndex = adapterIndex,
                    itemCount = items.size,
                    leadingPlaceholderCount = adapterLayout.leadingPlaceholderCount
                )?.let(contentType) ?: FinitePlaceholderContentType
            }
        }
    }

    val content: LazyListScope.() -> Unit = {
        items(
            count = adapterLayout?.adapterItemCount ?: items.size,
            key = adapterKey,
            contentType = adapterContentType
        ) { adapterIndex ->
            val itemIndex = if (adapterLayout == null) {
                adapterIndex
            } else {
                adapterIndexToItemIndex(
                    adapterIndex = adapterIndex,
                    itemCount = items.size,
                    leadingPlaceholderCount = adapterLayout.leadingPlaceholderCount
                )
            }

            if (itemIndex == null) {
                FinitePlaceholder(
                    orientation = orientation,
                    itemSize = itemSize
                )
            } else {
                AnimatedFiniteItem(
                    lazyListState = lazyListState,
                    adapterIndex = adapterIndex,
                    itemIndex = itemIndex,
                    itemCount = items.size,
                    leadingPlaceholderCount = adapterLayout?.leadingPlaceholderCount ?: 0,
                    availableSpace = availableSpace,
                    itemSize = itemSize,
                    itemSizePx = itemSizePx,
                    spaceBetweenItemsPx = spaceBetweenItemsPx,
                    selectorPosition = selectorPosition,
                    itemScaleRange = itemScaleRange,
                    showPartialItem = showPartialItem,
                    inactiveItemScale = inactiveItemFraction,
                    inactiveColor = inactiveColor,
                    activeColor = activeColor,
                    isRangeOfSelectionOdd = isRangeOfSelectionOdd,
                    disableScroll = disableScroll
                ) { animationProgress, size ->
                    itemContent(
                        animationProgress,
                        itemIndex,
                        items[itemIndex],
                        size,
                        lazyListState
                    )
                }
            }
        }
    }

    if (orientation == Orientation.Horizontal) {
        LazyRow(
            modifier = modifier,
            state = lazyListState,
            userScrollEnabled = !disableScroll,
            flingBehavior = flingBehavior,
            horizontalArrangement = Arrangement.spacedBy(spaceBetweenItems)
        ) {
            content()
        }
    } else {
        LazyColumn(
            modifier = modifier,
            state = lazyListState,
            userScrollEnabled = !disableScroll,
            flingBehavior = flingBehavior,
            verticalArrangement = Arrangement.spacedBy(spaceBetweenItems)
        ) {
            content()
        }
    }
}

@Composable
private fun LazyItemScope.FinitePlaceholder(
    orientation: Orientation,
    itemSize: Dp
) {
    if (orientation == Orientation.Horizontal) {
        Box(modifier = Modifier.width(itemSize))
    } else {
        Box(modifier = Modifier.height(itemSize))
    }
}

@Composable
private fun LazyItemScope.AnimatedFiniteItem(
    lazyListState: LazyListState,
    adapterIndex: Int,
    itemIndex: Int,
    itemCount: Int,
    leadingPlaceholderCount: Int,
    availableSpace: Float,
    itemSize: Dp,
    itemSizePx: Float,
    spaceBetweenItemsPx: Float,
    selectorPosition: Float,
    itemScaleRange: Int,
    showPartialItem: Boolean,
    inactiveItemScale: Float,
    inactiveColor: Color,
    activeColor: Color,
    isRangeOfSelectionOdd: Boolean,
    disableScroll: Boolean,
    itemContent: @Composable LazyItemScope.(animationProgress: AnimationProgress, size: Dp) -> Unit
) {
    val animationProgress by remember(
        adapterIndex,
        itemIndex,
        itemCount,
        leadingPlaceholderCount,
        itemScaleRange,
        showPartialItem,
        inactiveItemScale,
        disableScroll
    ) {
        derivedStateOf {
            getFiniteAnimationProgress(
                lazyListState = lazyListState,
                adapterIndex = adapterIndex,
                itemIndex = itemIndex,
                itemCount = itemCount,
                leadingPlaceholderCount = leadingPlaceholderCount,
                availableSpace = availableSpace,
                itemSize = itemSizePx,
                spaceBetweenItems = spaceBetweenItemsPx,
                selectorPosition = selectorPosition,
                itemScaleRange = itemScaleRange,
                inactiveScale = inactiveItemScale,
                inactiveColor = inactiveColor,
                activeColor = activeColor,
                isRangeOfSelectionOdd = isRangeOfSelectionOdd,
                disableScroll = disableScroll
            )
        }
    }

    itemContent(animationProgress, itemSize)
}

private fun getFiniteAnimationProgress(
    lazyListState: LazyListState,
    adapterIndex: Int,
    itemIndex: Int,
    itemCount: Int,
    leadingPlaceholderCount: Int,
    availableSpace: Float,
    itemSize: Float,
    spaceBetweenItems: Float,
    selectorPosition: Float,
    itemScaleRange: Int,
    inactiveScale: Float,
    inactiveColor: Color,
    activeColor: Color,
    isRangeOfSelectionOdd: Boolean,
    disableScroll: Boolean
): AnimationProgress {
    val visibleItems = lazyListState.layoutInfo.visibleItemsInfo
    val halfItemSize = itemSize / 2
    val currentItem = visibleItems.firstOrNull { it.index == adapterIndex }
    val visibleDataItems = visibleItems.filter {
        adapterIndexToItemIndex(
            adapterIndex = it.index,
            itemCount = itemCount,
            leadingPlaceholderCount = leadingPlaceholderCount
        ) != null
    }

    val selectedItemIndex = if (visibleDataItems.isNotEmpty()) {
        val selectedAdapterIndex = getIndexClosestToSelector(
            selectedIndex = adapterIndex,
            halfItemSize = halfItemSize,
            selectorPosition = selectorPosition,
            visibleItems = visibleDataItems
        )

        adapterIndexToItemIndex(
            adapterIndex = selectedAdapterIndex,
            itemCount = itemCount,
            leadingPlaceholderCount = leadingPlaceholderCount
        ) ?: itemIndex
    } else {
        itemIndex
    }

    val itemCenter = if (currentItem != null) {
        (halfItemSize + currentItem.offset).roundToInt()
    } else {
        val relativeIndex = adapterIndex - lazyListState.firstVisibleItemIndex
        (halfItemSize +
            relativeIndex * (itemSize + spaceBetweenItems) -
            lazyListState.firstVisibleItemScrollOffset).roundToInt()
    }

    val scale = getScale(
        rangeOfSelection = itemScaleRange,
        selectorPosition = selectorPosition,
        itemCenter = itemCenter,
        inactiveScale = inactiveScale,
        itemSize = itemSize,
        spaceBetweenItems = spaceBetweenItems
    ).coerceIn(0f, 1f)

    val scalingInterval = 1f - inactiveScale
    val colorScale = if (scalingInterval == 0f) 1f
    else ((scale - inactiveScale) / scalingInterval).coerceIn(0f, 1f)
    val color = lerp(inactiveColor, activeColor, colorScale)

    val distanceToSelector = if (disableScroll) {
        0f
    } else if (isRangeOfSelectionOdd) {
        itemCenter - selectorPosition
    } else {
        (itemCenter + itemSize / 2) - selectorPosition
    }

    return AnimationProgress(
        scale = scale,
        color = color,
        itemOffset = itemCenter,
        itemFraction = itemCenter / availableSpace,
        globalItemIndex = selectedItemIndex,
        itemIndex = selectedItemIndex,
        distanceToSelector = distanceToSelector
    )
}
