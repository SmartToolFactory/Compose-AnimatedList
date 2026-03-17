package com.smarttoolfactory.animatedlist.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

/**
 * Class that contains animated list animation properties
 * @param scale scale of the current item
 * @param color of the current item
 * @param itemOffset offset in px of current item from start of list
 * @param itemFraction offset in percent of current item from start of list relative to
 * width or height minus padding values in this axis
 * @param globalItemIndex selected item's adapter index. This can be a virtual index for
 * infinite/circular modes and the actual selected index for bounded finite lists
 * @param itemIndex selected item's real data index in range of 0..item count -1
 * @param distanceToSelector distance of this item to selector in pixels. This value
 * can be used in a click listener to scroll this item to selection position with animation.
 *
 * Current item identity is delivered separately via the itemContent lambda's index parameter.
 */
@Immutable
data class AnimationProgress(
    val scale: Float,
    val color: Color,
    val itemOffset: Int,
    val itemFraction: Float,
    val globalItemIndex: Int,
    val itemIndex: Int,
    val distanceToSelector: Float
)
