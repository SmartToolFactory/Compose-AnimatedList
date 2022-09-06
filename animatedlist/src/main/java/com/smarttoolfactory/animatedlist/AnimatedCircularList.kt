package com.smarttoolfactory.animatedlist

import androidx.compose.foundation.lazy.LazyListLayoutInfo
import androidx.compose.foundation.lazy.LazyListState
import kotlin.math.absoluteValue


/**
 * get scale based on whether it's initial run of list,
 * [LazyListState]'S [LazyListLayoutInfo.visibleItemsInfo]  list is empty,
 * or current scroll state of list.
 *
 * @param selectedIndex global index of currently selected item. This index changes only
 * when selected item is changed due to scroll
 * @param globalIndex index of current item. This index changes for every item in list
 * that calls this function
 * @param initialFistVisibleIndex index of item that is at the beginning of the list initially
 * @param indexOfSelector global index of element of selector of infinite items. Item with
 * this index is selected item
 * @param inactiveScale lower scale that items can be scaled to. It should be less than 1f
 * @param itemSize width/height of each item
 * @param spaceBetweenItems space between each item
 * @param selectorPosition position of selector or selected item
 * @param itemOffset offset of item from start of the list's x or y zero position
 */
private fun getScale(
    selectedIndex: Int,
    globalIndex: Int,
    initialFistVisibleIndex: Int,
    indexOfSelector: Int,
    inactiveScale: Float,
    itemSize: Float,
    spaceBetweenItems: Float,
    selectorPosition: Float,
    itemOffset: Int
): Float {
    // Current item is not close to center item or one half of left or right / item
    // visible items are not initialized and it's selector index
    return if (selectedIndex == -1 && globalIndex == initialFistVisibleIndex + indexOfSelector) {
        1f
        // visible items are not initialized and any item other than selector
    } else if (selectedIndex == -1) {
        inactiveScale
    } else {

        // Check how far this item is to selector index.
        val distanceToSelector = (selectorPosition - itemOffset).absoluteValue
        val scaleRegionWidth = (itemSize + spaceBetweenItems)

        calculateScale(
            distanceToSelector,
            scaleRegionWidth,
            inactiveScale
        )
    }
}

/**
 * Calculate scale that is inside scale region based on [minimum]
 */
private fun calculateScale(
    distanceToSelector: Float,
    scaleRegionWidth: Float,
    minimum: Float
): Float {
    return if (distanceToSelector < scaleRegionWidth) {
        // Now item is in scale region. Check where exactly it is in this region for animation
        val fraction = (scaleRegionWidth - distanceToSelector) / scaleRegionWidth
        // scale based on lower bound and 1f.
        // If lower bound .9f and fraction is 50% our scale is .9f + .1f*50/100 = .95f
        minimum + fraction * (1 - minimum)
    } else {
        minimum

    }.coerceIn(minimum, 1f)
}

