package com.spotexplorer.presentation.view.ui

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

fun getRandomColorFromList(): Color {
    return colorList.takeIf { it.isNotEmpty() }?.random() ?: Color(0xFF585D19)
}

fun getRandomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f
    )
}

val colorList = listOf(
    Color(0xFF1B1F3B), // Dark Blue
    Color(0xFF162447), // Midnight Blue
    Color(0xFF1F4068), // Deep Blue
    Color(0xFF1B98E0), // Bright Blue
    Color(0xFF006494), // Ocean Blue
    Color(0xFF247BA0), // Sky Blue
    Color(0xFF70C1B3), // Teal
    Color(0xFF00A896), // Deep Teal
    Color(0xFF028090), // Turquoise
    Color(0xFF05668D), // Dark Turquoise
    Color(0xFF02C39A), // Green Cyan
    Color(0xFF008F7A), // Deep Green Cyan
    Color(0xFF007F5F), // Forest Green
    Color(0xFF2B9348), // Leaf Green
    Color(0xFF80B918), // Fresh Green
    Color(0xFFBFD200), // Yellow Green
    Color(0xFFE6B400), // Mustard Yellow
    Color(0xFFF4A261), // Soft Orange
    Color(0xFFE76F51), // Coral
    Color(0xFFEB5E28), // Bright Orange
    Color(0xFF9A031E), // Dark Red
    Color(0xFF6A040F), // Deep Maroon
    Color(0xFF370617), // Dark Burgundy
    Color(0xFF540B0E), // Rich Burgundy
    Color(0xFF800F2F), // Wine Red
    Color(0xFFD00000), // Pure Red
    Color(0xFFEF233C), // Neon Red
    Color(0xFFBA181B), // Fire Red
    Color(0xFFFF595E), // Salmon Red
    Color(0xFFFF6F61), // Soft Coral
    Color(0xFFFF9F1C), // Bright Yellow-Orange
    Color(0xFFFFC300), // Golden Yellow
    Color(0xFFFFD166), // Warm Yellow
    Color(0xFFFFE066), // Soft Yellow
    Color(0xFFFFF3B0), // Light Cream
    Color(0xFFB5E48C), // Pastel Green
    Color(0xFF99D98C), // Light Green
    Color(0xFF76C893), // Mint Green
    Color(0xFF52B69A), // Aqua Green
    Color(0xFF34A0A4), // Cool Cyan
    Color(0xFF168AAD), // Blue Cyan
    Color(0xFF1A759F), // Soft Blue
    Color(0xFF1E6091), // Deep Blue
    Color(0xFF184E77), // Dark Blue
    Color(0xFF3D348B), // Royal Purple
    Color(0xFF5C4D7D), // Grape Purple
    Color(0xFF9C89B8), // Lavender Purple
    Color(0xFFC08497), // Rosewood
    Color(0xFFF4ACB7), // Blush Pink
    Color(0xFFFFC8DD), // Baby Pink
    Color(0xFFFFAFCC), // Light Pink
    Color(0xFFFFD6E0), // Soft Pink
    Color(0xFFF8AD9D), // Warm Peach
    Color(0xFFFFB4A2), // Pastel Peach
    Color(0xFFFFD7BA), // Creamy Peach
    Color(0xFFDDBEA9), // Muted Beige
    Color(0xFFB08968), // Coffee Brown
    Color(0xFF7F5539), // Mocha Brown
    Color(0xFF592E14), // Dark Chocolate
    Color(0xFF2D1B0E), // Espresso Brown
    Color(0xFF3F2E3E), // Deep Plum
    Color(0xFF5A3E36), // Rustic Brown
    Color(0xFF8A817C), // Muted Gray Brown
    Color(0xFFB5B5B5), // Soft Gray
    Color(0xFF8D99AE), // Slate Gray
    Color(0xFF2B2D42), // Charcoal Gray
    Color(0xFF3C3C3C), // Deep Gray
    Color(0xFF1F1F1F), // Almost Black
    Color(0xFFF8F9FA), // Off-White
    Color(0xFFE9ECEF), // Soft White
    Color(0xFFDEE2E6), // Cloud White
    Color(0xFFCED4DA), // Misty Gray
    Color(0xFFADB5BD), // Fog Gray
    Color(0xFF6C757D), // Ash Gray
    Color(0xFF495057), // Graphite Gray
    Color(0xFF343A40), // Shadow Gray
    Color(0xFF212529), // Midnight Gray
    Color(0xFF0A1128), // Deep Navy
    Color(0xFF001F54), // Dark Navy
    Color(0xFF034078), // Ocean Blue
    Color(0xFF1282A2), // Blue Lagoon
    Color(0xFF00A6A6), // Aqua
    Color(0xFF06D6A0), // Bright Green
    Color(0xFF1B998B), // Emerald Green
    Color(0xFF52B788), // Soft Green
    Color(0xFF74C69D), // Fresh Green
    Color(0xFF95D5B2), // Light Green
    Color(0xFFA7C957), // Olive Green
    Color(0xFF6A994E), // Earth Green
    Color(0xFF386641), // Forest Green
    Color(0xFF133C55), // Deep Teal
    Color(0xFF212D40), // Ink Blue
    Color(0xFF1B263B), // Deep Sea Blue
    Color(0xFF0B132B), // Cosmic Blue
    Color(0xFF3A506B), // Blue Slate
    Color(0xFF5BC0EB), // Sky Cyan
    Color(0xFF9BC53D), // Neon Green
    Color(0xFFE55934), // Vibrant Red
    Color(0xFFFFC857), // Bright Gold
    Color(0xFFFFD23F), // Soft Gold
    Color(0xFFFFE156), // Lemon Yellow
    Color(0xFFF4D35E), // Mellow Yellow
    Color(0xFF8D6A9F), // Lavender Gray
    Color(0xFF826AED)  // Soft Purple
)