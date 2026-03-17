package com.smarttoolfactory.animatedlist

import org.junit.Assert.assertEquals
import org.junit.Test

class AnimatedListMathTest {

    @Test
    fun clampVisibleItemCount_respectsListBounds() {
        assertEquals(0, clampVisibleItemCount(itemCount = 0, visibleItemCount = 5))
        assertEquals(1, clampVisibleItemCount(itemCount = 5, visibleItemCount = 0))
        assertEquals(3, clampVisibleItemCount(itemCount = 3, visibleItemCount = 5))
    }

    @Test
    fun resolveInitialSelectedIndex_clampsIntoFiniteList() {
        assertEquals(0, resolveInitialSelectedIndex(itemCount = 0, initialSelectedIndex = 5))
        assertEquals(0, resolveInitialSelectedIndex(itemCount = 5, initialSelectedIndex = -3))
        assertEquals(4, resolveInitialSelectedIndex(itemCount = 5, initialSelectedIndex = 9))
    }

    @Test
    fun resolveSelectorIndex_defaultsToCenteredFiniteSelector() {
        assertEquals(
            2,
            resolveSelectorIndex(
                visibleItemCount = 5,
                selectorIndex = -1,
                showPartialItem = false
            )
        )
        assertEquals(
            1,
            resolveSelectorIndex(
                visibleItemCount = 5,
                selectorIndex = -1,
                showPartialItem = true
            )
        )
        assertEquals(
            2,
            resolveSelectorIndex(
                visibleItemCount = 3,
                selectorIndex = 8,
                showPartialItem = false
            )
        )
    }

    @Test
    fun calculateFiniteAdapterLayout_addsLeadingAndTrailingPlaceholders() {
        val layout = calculateFiniteAdapterLayout(
            itemCount = 8,
            visibleItemCount = 5,
            selectorIndex = 2
        )

        assertEquals(2, layout.leadingPlaceholderCount)
        assertEquals(2, layout.trailingPlaceholderCount)
        assertEquals(12, layout.adapterItemCount)
    }

    @Test
    fun circularIndexMath_wrapsIntoFiniteRange() {
        assertEquals(4, positiveModulo(value = -1, modulus = 5))
        assertEquals(
            3,
            circularInitialFirstVisibleIndex(
                initialSelectedIndex = 0,
                selectorIndex = 2,
                itemCount = 5
            )
        )
    }

    @Test
    fun adapterIndexToItemIndex_mapsRealItemsOnly() {
        assertEquals(null, adapterIndexToItemIndex(adapterIndex = 0, itemCount = 8, leadingPlaceholderCount = 2))
        assertEquals(0, adapterIndexToItemIndex(adapterIndex = 2, itemCount = 8, leadingPlaceholderCount = 2))
        assertEquals(7, adapterIndexToItemIndex(adapterIndex = 9, itemCount = 8, leadingPlaceholderCount = 2))
        assertEquals(null, adapterIndexToItemIndex(adapterIndex = 10, itemCount = 8, leadingPlaceholderCount = 2))
    }

    @Test
    fun itemIndexToAdapterIndex_placesFirstAndLastItemsInSelectableRange() {
        assertEquals(2, itemIndexToAdapterIndex(itemIndex = 0, leadingPlaceholderCount = 2))
        assertEquals(9, itemIndexToAdapterIndex(itemIndex = 7, leadingPlaceholderCount = 2))
    }

    @Test
    fun centeredInitialSelection_usesRealItemIndexWithoutSyntheticOffset() {
        assertEquals(2, itemIndexToAdapterIndex(itemIndex = 2, leadingPlaceholderCount = 2) - 2)
    }
}
