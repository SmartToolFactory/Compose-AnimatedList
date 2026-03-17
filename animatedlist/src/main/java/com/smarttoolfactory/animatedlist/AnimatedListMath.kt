package com.smarttoolfactory.animatedlist

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.ui.util.lerp
import kotlin.math.absoluteValue

internal data class FiniteAdapterLayout(
    val leadingPlaceholderCount: Int,
    val trailingPlaceholderCount: Int,
    val adapterItemCount: Int
)

internal fun clampVisibleItemCount(
    itemCount: Int,
    visibleItemCount: Int
): Int {
    if (itemCount <= 0) return 0
    return visibleItemCount.coerceAtLeast(1).coerceAtMost(itemCount)
}

internal fun resolveInitialSelectedIndex(
    itemCount: Int,
    initialSelectedIndex: Int
): Int {
    if (itemCount <= 0) return 0
    return initialSelectedIndex.coerceIn(0, itemCount - 1)
}

internal fun shouldDisableScrollForShortList(
    itemCount: Int,
    visibleItemCount: Int
): Boolean = itemCount <= visibleItemCount.coerceAtLeast(1)

internal fun resolveSelectorIndex(
    visibleItemCount: Int,
    selectorIndex: Int,
    showPartialItem: Boolean
): Int {
    if (visibleItemCount <= 0) return 0

    val oddNumberOfVisibleItems = visibleItemCount % 2 == 1
    return (if (selectorIndex == -1) {
        if (oddNumberOfVisibleItems && !showPartialItem) {
            visibleItemCount / 2
        } else {
            visibleItemCount / 2 - 1
        }
    } else {
        selectorIndex
    }).coerceIn(0, visibleItemCount - 1)
}

internal fun isRangeOfSelectionOdd(
    itemScaleRange: Int,
    showPartialItem: Boolean
): Boolean {
    var isOdd = itemScaleRange % 2 == 1
    if (showPartialItem) isOdd = !isOdd
    return isOdd
}

internal fun positiveModulo(
    value: Int,
    modulus: Int
): Int {
    if (modulus <= 0) return 0
    val remainder = value % modulus
    return if (remainder < 0) remainder + modulus else remainder
}

internal fun circularInitialFirstVisibleIndex(
    initialSelectedIndex: Int,
    selectorIndex: Int,
    itemCount: Int
): Int = positiveModulo(initialSelectedIndex - selectorIndex, itemCount)

internal fun calculateFiniteAdapterLayout(
    itemCount: Int,
    visibleItemCount: Int,
    selectorIndex: Int
): FiniteAdapterLayout {
    val leadingPlaceholderCount = selectorIndex.coerceAtLeast(0)
    val trailingPlaceholderCount = (visibleItemCount - selectorIndex - 1).coerceAtLeast(0)

    return FiniteAdapterLayout(
        leadingPlaceholderCount = leadingPlaceholderCount,
        trailingPlaceholderCount = trailingPlaceholderCount,
        adapterItemCount = itemCount + leadingPlaceholderCount + trailingPlaceholderCount
    )
}

internal fun adapterIndexToItemIndex(
    adapterIndex: Int,
    itemCount: Int,
    leadingPlaceholderCount: Int
): Int? {
    val itemIndex = adapterIndex - leadingPlaceholderCount
    return itemIndex.takeIf { it in 0 until itemCount }
}

internal fun itemIndexToAdapterIndex(
    itemIndex: Int,
    leadingPlaceholderCount: Int
): Int = itemIndex + leadingPlaceholderCount

internal fun getSelectorPosition(
    isRangeOfSelectionOdd: Boolean,
    indexOfSelector: Int,
    itemSize: Float,
    spaceBetweenItems: Float
): Float {
    val halfItemSize = itemSize / 2

    return if (isRangeOfSelectionOdd) {
        indexOfSelector * (itemSize + spaceBetweenItems) + halfItemSize
    } else {
        indexOfSelector * (itemSize + spaceBetweenItems) + itemSize + spaceBetweenItems / 2
    }
}

internal fun getIndexClosestToSelector(
    selectedIndex: Int,
    halfItemSize: Float,
    selectorPosition: Float,
    visibleItems: List<LazyListItemInfo>
): Int {
    var distance = Int.MAX_VALUE
    var resolvedSelectedIndex = selectedIndex

    visibleItems.forEach {
        val itemDistanceToSelector = ((it.offset + halfItemSize) - selectorPosition).absoluteValue
        if (itemDistanceToSelector < distance) {
            distance = itemDistanceToSelector.toInt()
            resolvedSelectedIndex = it.index
        }
    }

    return resolvedSelectedIndex
}

internal fun getScale(
    rangeOfSelection: Int,
    selectorPosition: Float,
    itemCenter: Int,
    inactiveScale: Float,
    itemSize: Float,
    spaceBetweenItems: Float,
): Float {
    val distanceToSelector = (selectorPosition - itemCenter).absoluteValue
    val scaleRegionSize = (itemSize + spaceBetweenItems) * (rangeOfSelection + 1) / 2

    return calculateScale(
        distanceToSelector = distanceToSelector,
        scaleRegionSize = scaleRegionSize,
        minimum = inactiveScale
    )
}

internal fun calculateScale(
    distanceToSelector: Float,
    scaleRegionSize: Float,
    minimum: Float
): Float {
    return if (distanceToSelector < scaleRegionSize) {
        val fraction = (scaleRegionSize - distanceToSelector) / scaleRegionSize
        lerp(start = minimum, stop = 1f, fraction = fraction)
    } else {
        minimum
    }.coerceIn(minimum, 1f)
}
