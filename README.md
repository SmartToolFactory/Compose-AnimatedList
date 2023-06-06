# Compose AnimatedList

[![](https://jitpack.io/v/SmartToolFactory/Compose-AnimatedList.svg)](https://jitpack.io/#SmartToolFactory/Compose-AnimatedList)

Animated infinite and finite LazyRow and LazyColumn with scale and color animations on scroll change
based on how far they are to selector items

https://user-images.githubusercontent.com/35650605/190077938-89d9f79a-06df-4052-b1fa-eee5651bd861.mp4

## Gradle Setup

To get a Git project into your build:

* Step 1. Add the JitPack repository to your build file Add it in your root build.gradle at the end
  of repositories:

```
allprojects {
  repositories {
      ...
      maven { url 'https://jitpack.io' }
  }
}
```

* Step 2. Add the dependency

```
dependencies {
    implementation 'com.github.SmartToolFactory:Compose-AnimatedList:Tag'
}
```

## AnimatedInfiniteLazyRow/Column

### Declaration

```kotlin
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
```

```kotlin
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
) 
```

### Params

* **items** the data list
* **visibleItemCount** count of items that are visible at any time
* **inactiveItemPercent** percentage of scale that items inside **itemScaleRange**
* can be scaled down to. This is a number between 0 and 100
* **spaceBetweenItems** padding between 2 items
* **selectorIndex** index of selector. When **itemScaleRange** is odd number it's center of selected
  item, when **itemScaleRange** is even number it's center of item with selector index and the one
  next to it
* **itemScaleRange** range of area of scaling. When this value is odd any item that enters half of
  item size range subject to being scaled. When this value is even any item in 2 item size range is
  subject to being scaled
* **showPartialItem** show items partially that are at the start and end
* **activeColor** color of selected item
* **inactiveColor** color of items are not selected
* **key** a factory of stable and unique keys representing the item. Using the same key for multiple
  items in the list is not allowed. Type of the key should be savable via Bundle on Android. If
  null is passed the position in the list will represent the key. When you specify the key the
  scroll position will be maintained based on the key, which means if you add/remove items before
  the current visible item the item with the given key will be kept as the first visible one.
* **contentType** a factory of the content types for the item. The item compositions of the same
  type could be reused more efficiently. Note that null is a valid type and items of such type will
  be considered compatible.
* **itemContent** the content displayed by a single item

### Usage

itemContent returns `AnimationProgress` which contains `LazyListState` and
distance of each item to selector. By attaching a click modifier

```kotlin
AnimatedInfiniteLazyRow(
    modifier = Modifier
        .width(listWidth)
        .padding(10.dp),
    items = aspectRatios,
    visibleItemCount = 5,
    activeItemWidth = 40.dp,
    inactiveItemWidth = 30.dp,
    selectorIndex = 1,
    spaceBetweenItems = 0.dp,
    inactiveColor = InactiveColor,
    activeColor = ActiveColor,
    itemContent = { animationProgress, index, item, width, lazyListState ->
        val color = animationProgress.color
        val scale = animationProgress.scale
        Box(
            modifier = Modifier
                .scale(scale)
                .background(color, CircleShape)
                .size(width)
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = null
                ) {
                    coroutineScope.launch {
                        lazyListState.animateScrollBy(-animationProgress.distanceToSelector)
                    }
                },
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
```
