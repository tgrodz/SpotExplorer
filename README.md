# SpotExplorer - Interactive Image Allocation in Jetpack Compose


![SpotAllocator Demo](https://i.postimg.cc/8zYkZDQq/photo-2025-02-26-21-41-43.jpg)

---

`SpotAllocator` is a Jetpack Compose Composable function that enables interactive image zooming, panning, and marker selection.  
It is designed for mapping locations, seat allocation, object marking, and other interactive UI components in Android applications.

## Features
- **Pinch-to-Zoom & Pan** – Smooth zooming and panning for image navigation.
- **Double-Tap Zoom** – Quickly zoom into a tapped location.
- **Marker Selection & Animation** – Select spots with visual feedback.
- **Haptic Feedback** – Optional vibration for enhanced user interaction.
- **Dashed Routes** – Connect markers with dashed lines.
- **Customizable Overlay & Labels** – Optional UI elements for better visibility.

---

## Getting Started

### Usage Example
```kotlin
@Composable
fun MyScreen() {
    val imageBitmap = loadImage() // Load your image here
    val squares = listOf(
        SquareInfo(Offset(100f, 200f), Color.Red, "A1"),
        SquareInfo(Offset(300f, 500f), Color.Blue, "B2")
    )

    SpotAllocator(
        imageBitmap = imageBitmap,
        squares = squares,
        selectedSquare = null,
        onSquareClick = { square, position, type ->
            println("Clicked: $square at $position ($type)")
        }
    )
}
```

---

## Customization
`SpotAllocator` provides various options to customize behavior and visuals:

| Parameter        | Type             | Default  | Description |
|-----------------|-----------------|----------|-------------|
| `imageBitmap`   | `ImageBitmap`    | Required | The image displayed in the background. |
| `markerSize`    | `Float`          | `80f`    | The size of markers. |
| `useOverlay`    | `Boolean`        | `false`  | Dark overlay on the image. |
| `useVibration`  | `Boolean`        | `true`   | Enables haptic feedback on selection. |
| `useAnimation`  | `Boolean`        | `true`   | Enables marker animations. |
| `animationOption` | `Int`          | `1`      | Animation style (1, 2, or 3). |
| `showText`      | `Boolean`        | `true`   | Shows text labels for markers. |
| `showDashRoute` | `Boolean`        | `true`   | Draws dashed lines between markers. |
| `onSquareClick` | `(SquareInfo?, Offset?, String) -> Unit` | - | Callback for taps and selections. |

---

## How It Works
1. Loads an image (`ImageBitmap`) and displays it as the background.
2. Enables user interaction via zoom, pan, double-tap zoom, and tap gestures.
3. Displays selectable markers (`SquareInfo`) that can animate and change state.
4. Handles marker selection and triggers a callback with tap position and selection state.
5. Supports overlays and route drawing between multiple selected markers.

---

## License
This project is licensed under the **MIT License** – free for personal and commercial use.
```
