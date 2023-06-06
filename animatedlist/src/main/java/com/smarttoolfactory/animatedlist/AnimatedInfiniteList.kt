package com.smarttoolfactory.animatedlist

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smarttoolfactory.animatedlist.model.AnimationProgress

/**
 *  [LazyColumn] with infinite items and color scale animation. When [showPartialItem] is
 *  set to true items closest to start and end of the list are partially displayed
 *
 * @param items the data list
 * @param visibleItemCount count of items that are visible at any time
 * @param activeItemWidth width of selected item
 * @param inactiveItemWidth  width of unselected items
 * @param spaceBetweenItems padding between 2 items
 * @param selectorIndex index of selector. When [itemScaleRange] is odd number it's center of
 * selected item, when [itemScaleRange] is even number it's center of item with selector index
 * and the one next to it
 * @param itemScaleRange range of area of scaling. When this value is odd
 * any item that enters half of item size range subject to  being scaled. When this value is even
 * any item in 2 item size range is subject to being scaled
 * @param showPartialItem show items partially that are at the start and end
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
fun <T> AnimatedInfiniteLazyRow(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialFistVisibleIndex: Int = 0,
    activeItemWidth: Dp,
    inactiveItemWidth: Dp,
    visibleItemCount: Int = 5,
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
    AnimatedInfiniteList(
        modifier = modifier,
        items = items,
        initialFirstVisibleIndex = initialFistVisibleIndex,
        visibleItemCount = visibleItemCount,
        activeItemSize = activeItemWidth,
        inactiveItemSize = inactiveItemWidth,
        spaceBetweenItems = spaceBetweenItems,
        selectorIndex = selectorIndex,
        itemScaleRange = itemScaleRange,
        showPartialItem = showPartialItem,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        key = key,
        contentType = contentType,
        itemContent = itemContent
    )
}

/**
 *  [LazyRow] with infinite items and color scale animation. When [showPartialItem] is
 *  set to true items closest to start and end of the list are partially displayed
 *
 * @param items the data list
 * @param visibleItemCount count of items that are visible at any time
 * @param activeItemHeight height of selected item
 * @param inactiveItemHeight  height of unselected items
 * @param spaceBetweenItems padding between 2 items
 * @param selectorIndex index of selector. When [itemScaleRange] is odd number it's center of
 * selected item, when [itemScaleRange] is even number it's center of item with selector index
 * and the one next to it
 * @param itemScaleRange range of area of scaling. When this value is odd
 * any item that enters half of item size range subject to  being scaled. When this value is even
 * any item in 2 item size range is subject to being scaled
 * @param showPartialItem show items partially that are at the start and end
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
fun <T> AnimatedInfiniteLazyColumn(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialFirstVisibleIndex: Int = 0,
    visibleItemCount: Int = 5,
    activeItemHeight: Dp,
    inactiveItemHeight: Dp,
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
    AnimatedInfiniteList(
        modifier = modifier,
        items = items,
        initialFirstVisibleIndex = initialFirstVisibleIndex,
        visibleItemCount = visibleItemCount,
        activeItemSize = activeItemHeight,
        inactiveItemSize = inactiveItemHeight,
        spaceBetweenItems = spaceBetweenItems,
        selectorIndex = selectorIndex,
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

/**
 *  [LazyRow] with infinite items and color scale animation. When [showPartialItem] is
 *  set to true items closest to start and end of the list are partially displayed
 *
 * @param items the data list
 * @param visibleItemCount count of items that are visible at any time
 * @param inactiveItemPercent percentage of scale that items inside [itemScaleRange]
 * can be scaled down to. This is a number between 0 and 100
 * @param spaceBetweenItems padding between 2 items
 * @param selectorIndex index of selector. When [itemScaleRange] is odd number it's center of
 * selected item, when [itemScaleRange] is even number it's center of item with selector index
 * and the one next to it
 * @param itemScaleRange range of area of scaling. When this value is odd
 * any item that enters half of item size range subject to  being scaled. When this value is even
 * any item in 2 item size range is subject to being scaled
 * @param showPartialItem show items partially that are at the start and end
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
fun <T> AnimatedInfiniteLazyRow(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialFirstVisibleIndex: Int = 0,
    visibleItemCount: Int = 5,
    inactiveItemPercent: Int = 85,
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
    AnimatedInfiniteList(
        modifier = modifier,
        items = items,
        initialFirstVisibleIndex = initialFirstVisibleIndex,
        visibleItemCount = visibleItemCount,
        inactiveItemPercent = inactiveItemPercent,
        spaceBetweenItems = spaceBetweenItems,
        selectorIndex = selectorIndex,
        itemScaleRange = itemScaleRange,
        showPartialItem = showPartialItem,
        activeColor = activeColor,
        inactiveColor = inactiveColor,
        key = key,
        contentType = contentType,
        itemContent = itemContent
    )
}

/**
 *  [LazyColumn] with infinite items and color scale animation. When [showPartialItem] is
 *  set to true items closest to start and end of the list are partially displayed
 *
 * @param items the data list
 * @param visibleItemCount count of items that are visible at any time
 * @param inactiveItemPercent percentage of scale that items inside [itemScaleRange]
 * can be scaled down to. This is a number between 0 and 100
 * @param spaceBetweenItems padding between 2 items
 * @param selectorIndex index of selector. When [itemScaleRange] is odd number it's center of
 * selected item, when [itemScaleRange] is even number it's center of item with selector index
 * and the one next to it
 * @param itemScaleRange range of area of scaling. When this value is odd
 * any item that enters half of item size range subject to  being scaled. When this value is even
 * any item in 2 item size range is subject to being scaled
 * @param showPartialItem show items partially that are at the start and end
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
fun <T> AnimatedInfiniteLazyColumn(
    modifier: Modifier = Modifier,
    items: List<T>,
    initialFistVisibleIndex: Int = 0,
    visibleItemCount: Int = 5,
    inactiveItemPercent: Int = 85,
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
    AnimatedInfiniteList(
        modifier = modifier,
        items = items,
        initialFirstVisibleIndex = initialFistVisibleIndex,
        visibleItemCount = visibleItemCount,
        inactiveItemPercent = inactiveItemPercent,
        spaceBetweenItems = spaceBetweenItems,
        selectorIndex = selectorIndex,
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