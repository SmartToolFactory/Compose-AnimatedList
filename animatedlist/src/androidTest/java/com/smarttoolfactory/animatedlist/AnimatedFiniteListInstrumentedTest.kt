package com.smarttoolfactory.animatedlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger

class AnimatedFiniteListInstrumentedTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun boundedRow_stopsAtBothEnds() {
        val selectedIndex = AtomicInteger(-1)

        rule.setFiniteListContent(
            mode = FiniteListMode.Bounded,
            orientation = Orientation.Horizontal,
            initialSelectedIndex = 0,
            selectedIndex = selectedIndex
        )

        rule.waitForSelectedIndex(selectedIndex, expected = 0)
        rule.swipeList(SwipeDirection.Right)
        assertEquals(0, selectedIndex.get())

        rule.swipeUntilSelected(
            selectedIndex = selectedIndex,
            expected = 4,
            direction = SwipeDirection.Left
        )

        rule.swipeList(SwipeDirection.Left)
        assertEquals(4, selectedIndex.get())
    }

    @Test
    fun circularRow_wrapsAcrossFiniteEnds() {
        val selectedIndex = AtomicInteger(-1)

        rule.setFiniteListContent(
            mode = FiniteListMode.Circular,
            orientation = Orientation.Horizontal,
            initialSelectedIndex = 4,
            selectedIndex = selectedIndex
        )

        rule.waitForSelectedIndex(selectedIndex, expected = 4)
        rule.swipeUntilSelected(
            selectedIndex = selectedIndex,
            expected = 0,
            direction = SwipeDirection.Left,
            maxAttempts = 2
        )
        rule.swipeUntilSelected(
            selectedIndex = selectedIndex,
            expected = 4,
            direction = SwipeDirection.Right,
            maxAttempts = 2
        )
    }

    @Test
    fun boundedColumn_stopsAtBothEnds() {
        val selectedIndex = AtomicInteger(-1)

        rule.setFiniteListContent(
            mode = FiniteListMode.Bounded,
            orientation = Orientation.Vertical,
            initialSelectedIndex = 0,
            selectedIndex = selectedIndex
        )

        rule.waitForSelectedIndex(selectedIndex, expected = 0)
        rule.swipeList(SwipeDirection.Down)
        assertEquals(0, selectedIndex.get())

        rule.swipeUntilSelected(
            selectedIndex = selectedIndex,
            expected = 4,
            direction = SwipeDirection.Up
        )

        rule.swipeList(SwipeDirection.Up)
        assertEquals(4, selectedIndex.get())
    }

    @Test
    fun shortCircularList_disablesScrollAndKeepsFiniteSelection() {
        val selectedIndex = AtomicInteger(-1)

        rule.setFiniteListContent(
            mode = FiniteListMode.Circular,
            orientation = Orientation.Horizontal,
            items = listOf(0, 1),
            visibleItemCount = 5,
            initialSelectedIndex = 1,
            selectedIndex = selectedIndex
        )

        rule.waitForSelectedIndex(selectedIndex, expected = 1)
        rule.swipeList(SwipeDirection.Left)
        rule.swipeList(SwipeDirection.Right)
        assertEquals(1, selectedIndex.get())
    }

    @Test
    fun boundedRow_withCenteredSelector_keepsAllItemsSelectable() {
        val selectedIndex = AtomicInteger(-1)

        rule.setFiniteListContent(
            mode = FiniteListMode.Bounded,
            orientation = Orientation.Horizontal,
            initialSelectedIndex = 2,
            selectedIndex = selectedIndex,
            items = (0..7).toList(),
            visibleItemCount = 5,
            selectorIndex = 2
        )

        rule.waitForSelectedIndex(selectedIndex, expected = 2)

        rule.swipeUntilSelected(
            selectedIndex = selectedIndex,
            expected = 7,
            direction = SwipeDirection.Left,
            maxAttempts = 8
        )
        rule.onNodeWithTag(itemTag(7)).assertIsDisplayed()

        rule.swipeUntilSelected(
            selectedIndex = selectedIndex,
            expected = 0,
            direction = SwipeDirection.Right,
            maxAttempts = 8
        )
        rule.onNodeWithTag(itemTag(0)).assertIsDisplayed()
    }

    @Test
    fun boundedRow_tapSelectsFirstMiddleAndLastItems() {
        val selectedIndex = AtomicInteger(-1)

        rule.setFiniteListContent(
            mode = FiniteListMode.Bounded,
            orientation = Orientation.Horizontal,
            initialSelectedIndex = 2,
            selectedIndex = selectedIndex,
            items = (0..7).toList(),
            visibleItemCount = 5,
            selectorIndex = 2
        )

        rule.waitForSelectedIndex(selectedIndex, expected = 2)

        rule.onNodeWithTag(itemTag(0)).performClick()
        rule.waitForSelectedIndex(selectedIndex, expected = 0)
        rule.onNodeWithTag(itemTag(0)).assertIsDisplayed()

        rule.onNodeWithTag(itemTag(4)).performClick()
        rule.waitForSelectedIndex(selectedIndex, expected = 4)
        rule.onNodeWithTag(itemTag(4)).assertIsDisplayed()

        rule.swipeUntilItemVisible(item = 7, direction = SwipeDirection.Left)
        rule.onNodeWithTag(itemTag(7)).performClick()
        rule.waitForSelectedIndex(selectedIndex, expected = 7)
        rule.onNodeWithTag(itemTag(7)).assertIsDisplayed()
    }
}

private const val ListTag = "finite-list"

private enum class SwipeDirection {
    Left,
    Right,
    Up,
    Down
}

private fun itemTag(item: Int): String = "item-$item"

private fun ComposeContentTestRule.setFiniteListContent(
    mode: FiniteListMode,
    orientation: Orientation,
    initialSelectedIndex: Int,
    selectedIndex: AtomicInteger,
    items: List<Int> = (0..4).toList(),
    visibleItemCount: Int = 3,
    selectorIndex: Int = -1
) {
    setContent {
        val coroutineScope = rememberCoroutineScope()

        if (orientation == Orientation.Horizontal) {
            AnimatedFiniteLazyRow(
                modifier = Modifier.testTag(ListTag),
                items = items,
                initialSelectedIndex = initialSelectedIndex,
                activeItemWidth = 96.dp,
                inactiveItemWidth = 96.dp,
                visibleItemCount = visibleItemCount,
                mode = mode,
                selectorIndex = selectorIndex,
                spaceBetweenItems = 0.dp
            ) { animationProgress, index, item, size, lazyListState ->
                if (index == animationProgress.itemIndex) {
                    SideEffect {
                        selectedIndex.set(animationProgress.itemIndex)
                    }
                }

                Box(
                    modifier = Modifier
                        .testTag(itemTag(item))
                        .width(size)
                        .height(56.dp)
                        .clickable {
                            coroutineScope.launch {
                                lazyListState.animateScrollBy(animationProgress.distanceToSelector)
                            }
                        }
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(text = item.toString())
                }
            }
        } else {
            AnimatedFiniteLazyColumn(
                modifier = Modifier.testTag(ListTag),
                items = items,
                initialSelectedIndex = initialSelectedIndex,
                activeItemHeight = 96.dp,
                inactiveItemHeight = 96.dp,
                visibleItemCount = visibleItemCount,
                mode = mode,
                selectorIndex = selectorIndex,
                spaceBetweenItems = 0.dp
            ) { animationProgress, index, item, size, lazyListState ->
                if (index == animationProgress.itemIndex) {
                    SideEffect {
                        selectedIndex.set(animationProgress.itemIndex)
                    }
                }

                Box(
                    modifier = Modifier
                        .testTag(itemTag(item))
                        .height(size)
                        .width(96.dp)
                        .clickable {
                            coroutineScope.launch {
                                lazyListState.animateScrollBy(animationProgress.distanceToSelector)
                            }
                        }
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    BasicText(text = item.toString())
                }
            }
        }
    }
}

private fun ComposeContentTestRule.waitForSelectedIndex(
    selectedIndex: AtomicInteger,
    expected: Int
) {
    waitUntil(timeoutMillis = 2_000) {
        selectedIndex.get() == expected
    }
}

private fun ComposeContentTestRule.swipeUntilSelected(
    selectedIndex: AtomicInteger,
    expected: Int,
    direction: SwipeDirection,
    maxAttempts: Int = 6
) {
    repeat(maxAttempts) {
        if (selectedIndex.get() == expected) return
        swipeList(direction)
    }

    assertEquals(expected, selectedIndex.get())
}

private fun ComposeContentTestRule.swipeUntilItemVisible(
    item: Int,
    direction: SwipeDirection,
    maxAttempts: Int = 8
) {
    repeat(maxAttempts) {
        if (onAllNodesWithTag(itemTag(item)).fetchSemanticsNodes().isNotEmpty()) return
        swipeList(direction)
    }

    onNodeWithTag(itemTag(item)).assertIsDisplayed()
}

private fun ComposeContentTestRule.swipeList(direction: SwipeDirection) {
    onNodeWithTag(ListTag).performTouchInput {
        val horizontalStart = right * 0.72f
        val horizontalEnd = right * 0.37f
        val verticalStart = bottom * 0.72f
        val verticalEnd = bottom * 0.37f

        when (direction) {
            SwipeDirection.Left -> swipe(
                start = Offset(horizontalStart, centerY),
                end = Offset(horizontalEnd, centerY),
                durationMillis = 300
            )

            SwipeDirection.Right -> swipe(
                start = Offset(horizontalEnd, centerY),
                end = Offset(horizontalStart, centerY),
                durationMillis = 300
            )

            SwipeDirection.Up -> swipe(
                start = Offset(centerX, verticalStart),
                end = Offset(centerX, verticalEnd),
                durationMillis = 300
            )

            SwipeDirection.Down -> swipe(
                start = Offset(centerX, verticalEnd),
                end = Offset(centerX, verticalStart),
                durationMillis = 300
            )
        }
    }

    waitForIdle()
}
