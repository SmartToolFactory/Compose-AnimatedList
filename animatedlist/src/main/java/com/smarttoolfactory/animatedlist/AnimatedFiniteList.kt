package com.smarttoolfactory.animatedlist

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.animatedlist.model.AnimationProgress

enum class FiniteListMode {
    Circular,
    Bounded
}

@Composable
fun <T> AnimatedFiniteLazyRow(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialSelectedIndex: Int = 0,
    activeItemWidth: Dp,
    inactiveItemWidth: Dp,
    visibleItemCount: Int = 5,
    mode: FiniteListMode = FiniteListMode.Bounded,
    spaceBetweenItems: Dp = 4.dp,
    selectorIndex: Int = -1,
    itemScaleRange: Int = 1,
    showPartialItem: Boolean = false,
    activeColor: Color = ActiveColor,
    inactiveColor: Color = InactiveColor,
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
    val effectiveVisibleItemCount = clampVisibleItemCount(items.size, visibleItemCount)
    val clampedInitialSelectedIndex = resolveInitialSelectedIndex(items.size, initialSelectedIndex)
    val resolvedSelectorIndex = resolveSelectorIndex(
        visibleItemCount = effectiveVisibleItemCount,
        selectorIndex = selectorIndex,
        showPartialItem = showPartialItem
    )

    if (mode == FiniteListMode.Circular && !shouldDisableScrollForShortList(items.size, visibleItemCount)) {
        val initialFirstVisibleIndex = circularInitialFirstVisibleIndex(
            initialSelectedIndex = clampedInitialSelectedIndex,
            selectorIndex = resolvedSelectorIndex,
            itemCount = items.size
        )

        AnimatedInfiniteList(
            modifier = modifier,
            items = items,
            initialFirstVisibleIndex = initialFirstVisibleIndex,
            visibleItemCount = effectiveVisibleItemCount,
            activeItemSize = activeItemWidth,
            inactiveItemSize = inactiveItemWidth,
            spaceBetweenItems = spaceBetweenItems,
            selectorIndex = resolvedSelectorIndex,
            itemScaleRange = itemScaleRange,
            showPartialItem = showPartialItem,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            key = key,
            contentType = contentType,
            itemContent = { animationProgress, index, item, size, lazyListState ->
                itemContent(
                    animationProgress.copy(globalItemIndex = animationProgress.itemIndex),
                    index,
                    item,
                    size,
                    lazyListState
                )
            }
        )
    } else {
        AnimatedFiniteList(
            modifier = modifier,
            items = items,
            initialSelectedIndex = clampedInitialSelectedIndex,
            visibleItemCount = effectiveVisibleItemCount,
            activeItemSize = activeItemWidth,
            inactiveItemSize = inactiveItemWidth,
            spaceBetweenItems = spaceBetweenItems,
            selectorIndex = resolvedSelectorIndex,
            itemScaleRange = itemScaleRange,
            showPartialItem = showPartialItem,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            key = key,
            contentType = contentType,
            itemContent = itemContent
        )
    }
}

@Composable
fun <T> AnimatedFiniteLazyColumn(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialSelectedIndex: Int = 0,
    activeItemHeight: Dp,
    inactiveItemHeight: Dp,
    visibleItemCount: Int = 5,
    mode: FiniteListMode = FiniteListMode.Bounded,
    spaceBetweenItems: Dp = 4.dp,
    selectorIndex: Int = -1,
    itemScaleRange: Int = 1,
    showPartialItem: Boolean = false,
    activeColor: Color = ActiveColor,
    inactiveColor: Color = InactiveColor,
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
    val effectiveVisibleItemCount = clampVisibleItemCount(items.size, visibleItemCount)
    val clampedInitialSelectedIndex = resolveInitialSelectedIndex(items.size, initialSelectedIndex)
    val resolvedSelectorIndex = resolveSelectorIndex(
        visibleItemCount = effectiveVisibleItemCount,
        selectorIndex = selectorIndex,
        showPartialItem = showPartialItem
    )

    if (mode == FiniteListMode.Circular && !shouldDisableScrollForShortList(items.size, visibleItemCount)) {
        val initialFirstVisibleIndex = circularInitialFirstVisibleIndex(
            initialSelectedIndex = clampedInitialSelectedIndex,
            selectorIndex = resolvedSelectorIndex,
            itemCount = items.size
        )

        AnimatedInfiniteList(
            modifier = modifier,
            items = items,
            initialFirstVisibleIndex = initialFirstVisibleIndex,
            visibleItemCount = effectiveVisibleItemCount,
            activeItemSize = activeItemHeight,
            inactiveItemSize = inactiveItemHeight,
            spaceBetweenItems = spaceBetweenItems,
            selectorIndex = resolvedSelectorIndex,
            itemScaleRange = itemScaleRange,
            showPartialItem = showPartialItem,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            orientation = Orientation.Vertical,
            key = key,
            contentType = contentType,
            itemContent = { animationProgress, index, item, size, lazyListState ->
                itemContent(
                    animationProgress.copy(globalItemIndex = animationProgress.itemIndex),
                    index,
                    item,
                    size,
                    lazyListState
                )
            }
        )
    } else {
        AnimatedFiniteList(
            modifier = modifier,
            items = items,
            initialSelectedIndex = clampedInitialSelectedIndex,
            visibleItemCount = effectiveVisibleItemCount,
            activeItemSize = activeItemHeight,
            inactiveItemSize = inactiveItemHeight,
            spaceBetweenItems = spaceBetweenItems,
            selectorIndex = resolvedSelectorIndex,
            itemScaleRange = itemScaleRange,
            showPartialItem = showPartialItem,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            orientation = Orientation.Vertical,
            key = key,
            contentType = contentType,
            itemContent = itemContent
        )
    }
}

@Composable
fun <T> AnimatedFiniteLazyRow(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialSelectedIndex: Int = 0,
    visibleItemCount: Int = 5,
    inactiveItemPercent: Int = 85,
    mode: FiniteListMode = FiniteListMode.Bounded,
    spaceBetweenItems: Dp = 16.dp,
    selectorIndex: Int = -1,
    itemScaleRange: Int = 1,
    showPartialItem: Boolean = false,
    activeColor: Color = ActiveColor,
    inactiveColor: Color = InactiveColor,
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
    val effectiveVisibleItemCount = clampVisibleItemCount(items.size, visibleItemCount)
    val clampedInitialSelectedIndex = resolveInitialSelectedIndex(items.size, initialSelectedIndex)
    val resolvedSelectorIndex = resolveSelectorIndex(
        visibleItemCount = effectiveVisibleItemCount,
        selectorIndex = selectorIndex,
        showPartialItem = showPartialItem
    )

    if (mode == FiniteListMode.Circular && !shouldDisableScrollForShortList(items.size, visibleItemCount)) {
        val initialFirstVisibleIndex = circularInitialFirstVisibleIndex(
            initialSelectedIndex = clampedInitialSelectedIndex,
            selectorIndex = resolvedSelectorIndex,
            itemCount = items.size
        )

        AnimatedInfiniteList(
            modifier = modifier,
            items = items,
            initialFirstVisibleIndex = initialFirstVisibleIndex,
            visibleItemCount = effectiveVisibleItemCount,
            inactiveItemPercent = inactiveItemPercent,
            spaceBetweenItems = spaceBetweenItems,
            selectorIndex = resolvedSelectorIndex,
            itemScaleRange = itemScaleRange,
            showPartialItem = showPartialItem,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            key = key,
            contentType = contentType,
            itemContent = { animationProgress, index, item, size, lazyListState ->
                itemContent(
                    animationProgress.copy(globalItemIndex = animationProgress.itemIndex),
                    index,
                    item,
                    size,
                    lazyListState
                )
            }
        )
    } else {
        AnimatedFiniteList(
            modifier = modifier,
            items = items,
            initialSelectedIndex = clampedInitialSelectedIndex,
            visibleItemCount = effectiveVisibleItemCount,
            inactiveItemPercent = inactiveItemPercent,
            spaceBetweenItems = spaceBetweenItems,
            selectorIndex = resolvedSelectorIndex,
            itemScaleRange = itemScaleRange,
            showPartialItem = showPartialItem,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            key = key,
            contentType = contentType,
            itemContent = itemContent
        )
    }
}

@Composable
fun <T> AnimatedFiniteLazyColumn(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialSelectedIndex: Int = 0,
    visibleItemCount: Int = 5,
    inactiveItemPercent: Int = 85,
    mode: FiniteListMode = FiniteListMode.Bounded,
    spaceBetweenItems: Dp = 4.dp,
    selectorIndex: Int = -1,
    itemScaleRange: Int = 1,
    showPartialItem: Boolean = false,
    activeColor: Color = Color.Cyan,
    inactiveColor: Color = Color.Gray,
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
    val effectiveVisibleItemCount = clampVisibleItemCount(items.size, visibleItemCount)
    val clampedInitialSelectedIndex = resolveInitialSelectedIndex(items.size, initialSelectedIndex)
    val resolvedSelectorIndex = resolveSelectorIndex(
        visibleItemCount = effectiveVisibleItemCount,
        selectorIndex = selectorIndex,
        showPartialItem = showPartialItem
    )

    if (mode == FiniteListMode.Circular && !shouldDisableScrollForShortList(items.size, visibleItemCount)) {
        val initialFirstVisibleIndex = circularInitialFirstVisibleIndex(
            initialSelectedIndex = clampedInitialSelectedIndex,
            selectorIndex = resolvedSelectorIndex,
            itemCount = items.size
        )

        AnimatedInfiniteList(
            modifier = modifier,
            items = items,
            initialFirstVisibleIndex = initialFirstVisibleIndex,
            visibleItemCount = effectiveVisibleItemCount,
            inactiveItemPercent = inactiveItemPercent,
            spaceBetweenItems = spaceBetweenItems,
            selectorIndex = resolvedSelectorIndex,
            itemScaleRange = itemScaleRange,
            showPartialItem = showPartialItem,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            orientation = Orientation.Vertical,
            key = key,
            contentType = contentType,
            itemContent = { animationProgress, index, item, size, lazyListState ->
                itemContent(
                    animationProgress.copy(globalItemIndex = animationProgress.itemIndex),
                    index,
                    item,
                    size,
                    lazyListState
                )
            }
        )
    } else {
        AnimatedFiniteList(
            modifier = modifier,
            items = items,
            initialSelectedIndex = clampedInitialSelectedIndex,
            visibleItemCount = effectiveVisibleItemCount,
            inactiveItemPercent = inactiveItemPercent,
            spaceBetweenItems = spaceBetweenItems,
            selectorIndex = resolvedSelectorIndex,
            itemScaleRange = itemScaleRange,
            showPartialItem = showPartialItem,
            activeColor = activeColor,
            inactiveColor = inactiveColor,
            orientation = Orientation.Vertical,
            key = key,
            contentType = contentType,
            itemContent = itemContent
        )
    }
}
