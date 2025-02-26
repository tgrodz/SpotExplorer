package com.spotexplorer.presentation.view.screen.allocation

import androidx.compose.runtime.saveable.rememberSaveable
import android.graphics.Matrix
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.spotexplorer.model.SquareInfo
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpotAllocator(
    imageBitmap: ImageBitmap,
    squares: List<SquareInfo>,
    selectedSquare: SquareInfo? = null,
    markerSize: Float = 80f,
    useOverlay: Boolean = false,
    useVibration: Boolean = true,
    useAnimation: Boolean = true,
    animationOption: Int = 1,
    showText: Boolean = true,
    showDashRoute: Boolean = true,
    onSquareClick: (SquareInfo?, Offset?, String) -> Unit
) {
    var scale by rememberSaveable { mutableStateOf(1f) }
    var translationX by rememberSaveable { mutableStateOf(0f) }
    var translationY by rememberSaveable { mutableStateOf(0f) }
    var containerWidth by remember { mutableStateOf(0f) }
    var containerHeight by remember { mutableStateOf(0f) }
    val hapticFeedback = LocalHapticFeedback.current

    val transformableState = rememberTransformableState { zoomChange, panChange, _ ->
        val newScale = (scale * zoomChange).coerceIn(0.5f, 5f)
        translationX = restrictOffset(
            translationX + panChange.x, newScale, containerWidth, imageBitmap.width.toFloat()
        )
        translationY = restrictOffset(
            translationY + panChange.y, newScale, containerHeight, imageBitmap.height.toFloat()
        )
        scale = newScale
    }

    val animTranslationX = remember { Animatable(translationX) }
    val animTranslationY = remember { Animatable(translationY) }
    LaunchedEffect(selectedSquare, containerWidth, containerHeight, scale) {
        if (selectedSquare != null && containerWidth > 0f && containerHeight > 0f) {
            val (targetTranslationX, targetTranslationY) = calculateTargetTranslation(
                selectedSquare = selectedSquare,
                containerWidth = containerWidth,
                containerHeight = containerHeight,
                scale = scale,
                markerSize = markerSize,
                imageWidth = imageBitmap.width.toFloat(),
                imageHeight = imageBitmap.height.toFloat()
            )
            animTranslationX.animateTo(targetTranslationX, animationSpec = tween(500))
            animTranslationY.animateTo(targetTranslationY, animationSpec = tween(500))
            translationX = animTranslationX.value
            translationY = animTranslationY.value
        }
    }

    val markerAnimations = squares.map { sq -> rememberMarkerAnimation(
            markerSize = markerSize,
            isRed = (sq.color == Color.Red),
            animationOption = animationOption,
            enableAnimation = useAnimation
        )
    }

    val (rippleRadius, rippleAlpha) = rememberRippleAnimation(markerSize)

    val textPaint = remember {
        android.graphics.Paint().apply {
            color = Color.White.toArgb()
            textAlign = android.graphics.Paint.Align.CENTER
            textSize = 14.sp.value
            isAntiAlias = true
            setShadowLayer(4f, 2f, 2f, android.graphics.Color.BLACK)
        }
    }

    val markerCenters = remember(squares, markerSize) {
        squares.map { it.offset + Offset(markerSize / 2, markerSize / 2) }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .onSizeChanged { size ->
                containerWidth = size.width.toFloat()
                containerHeight = size.height.toFloat()
            }
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = transformableState)
                .pointerInput(squares) {
                    detectTapGestures(
                        onDoubleTap = { tapOffset ->
                            val (newScale, newTranslationX, newTranslationY) = calculateDoubleTapTransform(
                                tapOffset = tapOffset,
                                currentScale = scale,
                                translationX = translationX,
                                translationY = translationY,
                                containerWidth = containerWidth,
                                containerHeight = containerHeight,
                                imageWidth = imageBitmap.width.toFloat(),
                                imageHeight = imageBitmap.height.toFloat()
                            )
                            translationX = newTranslationX
                            translationY = newTranslationY
                            scale = newScale
                        },
                        onLongPress = { tapOffset ->
                            val localTap = convertToLocal(
                                    tapOffset,
                                    scale,
                                    translationX,
                                    translationY
                                )
                            onSquareClick(null, localTap, "long")
                        },
                        onTap = { tapOffset ->
                            handleTapGesture(
                                tapOffset = tapOffset,
                                scale = scale,
                                translationX = translationX,
                                translationY = translationY,
                                squares = squares,
                                markerSize = markerSize,
                                hapticFeedback = hapticFeedback,
                                useVibration = useVibration,
                                onSquareClick = onSquareClick
                            )
                        }
                    )
                }
        ) {
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.concat(
                    Matrix().apply {
                        postScale(scale, scale)
                        postTranslate(translationX, translationY)
                    }
                )

                drawImage(image = imageBitmap, topLeft = Offset.Zero)
                drawMarkers(
                    squares = squares,
                    markerCenters = markerCenters,
                    markerSize = markerSize,
                    selectedSquare = selectedSquare,
                    rippleAlpha = rippleAlpha,
                    rippleRadius = rippleRadius,
                    markerAnimations = markerAnimations,
                    showText = showText,
                    isDrawDashRoute = showDashRoute,
                    textPaint = textPaint
                )
            }

        }
        // Optionally darken the image with an overlay.
        if (useOverlay) {
            Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.2f)))
        }
    }
}


private  fun calculateTargetTranslation(
    selectedSquare: SquareInfo,
    containerWidth: Float,
    containerHeight: Float,
    scale: Float,
    markerSize: Float,
    imageWidth: Float,
    imageHeight: Float
): Pair<Float, Float> {
    val markerCenter = selectedSquare.offset + Offset(markerSize / 2, markerSize / 2)
    val rawTargetTranslationX = containerWidth / 2 - markerCenter.x * scale
    val rawTargetTranslationY = containerHeight / 2 - markerCenter.y * scale
    val targetTranslationX = restrictOffset(rawTargetTranslationX, scale, containerWidth, imageWidth)
    val targetTranslationY = restrictOffset(rawTargetTranslationY, scale, containerHeight, imageHeight)
    return Pair(targetTranslationX, targetTranslationY)
}


private  fun DrawScope.drawMarkers(
    squares: List<SquareInfo>,
    markerCenters: List<Offset>,
    markerSize: Float,
    selectedSquare: SquareInfo?,
    rippleAlpha: Float,
    rippleRadius: Float,
    markerAnimations: List<Pair<Float, Float>>,
    showText: Boolean,
    isDrawDashRoute: Boolean,
    textPaint: android.graphics.Paint
) {
    if (isDrawDashRoute) {
        drawDashedRoute(squares, markerSize)
    }
    squares.forEachIndexed { index, sq ->
        val circleCenter = markerCenters[index]
        if (selectedSquare != null &&
            selectedSquare.xPosition == sq.xPosition &&
            selectedSquare.yPosition == sq.yPosition
        ) {
            drawGlobalRippleEffect(sq, circleCenter, rippleAlpha, rippleRadius)
        }
        val (dynamicRadius, dynamicAlpha) = markerAnimations[index]
        drawCircle(
            color = sq.color.copy(alpha = dynamicAlpha),
            radius = dynamicRadius,
            center = circleCenter
        )
        drawMarkerTriangle(circleCenter, dynamicRadius)
        if (showText) {
            val labelText = if (sq.name.isEmpty()) "Marker" else sq.name
            drawMarkerLabel(labelText, circleCenter, markerSize, textPaint)
        }
    }
}


private  fun DrawScope.drawDashedRoute(squares: List<SquareInfo>, markerSize: Float) {
    if (squares.size > 1) {
        squares.zipWithNext().forEach { (sq1, sq2) ->
            val start = sq1.offset + Offset(markerSize / 2, markerSize / 2)
            val end = sq2.offset + Offset(markerSize / 2, markerSize / 2)
            drawLine(
                color = Color.Blue,
                start = start,
                end = end,
                strokeWidth = 16f,
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 40f), 0f)
            )
        }
    }
}


private  fun DrawScope.drawGlobalRippleEffect(
    sq: SquareInfo,
    circleCenter: Offset,
    rippleAlpha: Float,
    rippleRadius: Float
) {
    drawCircle(
        color = sq.color.copy(alpha = rippleAlpha),
        radius = rippleRadius,
        center = circleCenter,
        style = Stroke(width = 12f)
    )
}


private  fun DrawScope.drawMarkerTriangle(
    circleCenter: Offset,
    dynamicRadius: Float
) {
    val trianglePath = Path().apply {
        moveTo(circleCenter.x, circleCenter.y + (dynamicRadius / 3))
        lineTo(circleCenter.x - (dynamicRadius / 3), circleCenter.y - (dynamicRadius / 4))
        lineTo(circleCenter.x + (dynamicRadius / 3), circleCenter.y - (dynamicRadius / 4))
        close()
    }
    drawPath(path = trianglePath, color = Color.White)
}


private  fun DrawScope.drawMarkerLabel(
    labelText: String,
    circleCenter: Offset,
    markerSize: Float,
    textPaint: android.graphics.Paint
) {
    val textWidth = textPaint.measureText(labelText)
    val fm = textPaint.fontMetrics
    val textHeight = fm.descent - fm.ascent
    val horizontalPadding = 8.dp.toPx()
    val verticalPadding = 4.dp.toPx()
    val rectWidth = textWidth + horizontalPadding * 2
    val rectHeight = textHeight + verticalPadding * 2
    val spacingAboveCircle = 6.dp.toPx()
    val rectCenterX = circleCenter.x
    val rectCenterY = circleCenter.y - (markerSize / 2) - spacingAboveCircle - (rectHeight / 2)
    val backgroundLeft = rectCenterX - (rectWidth / 2)
    val backgroundTop = rectCenterY - (rectHeight / 2)
    drawRoundRect(
        color = Color(0xFF2196F3).copy(alpha = 0.7f),
        topLeft = Offset(backgroundLeft, backgroundTop),
        size = Size(rectWidth, rectHeight),
        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
    )
    val centerY = backgroundTop + rectHeight / 2
    val textBaseline = centerY - (fm.ascent + fm.descent) / 2f
    drawContext.canvas.nativeCanvas.drawText(labelText, rectCenterX, textBaseline, textPaint)
}


@Composable
private fun rememberMarkerAnimation(
    markerSize: Float,
    isRed: Boolean,
    animationOption: Int,
    enableAnimation: Boolean
): Pair<Float, Float> {
    val infiniteTransition = rememberInfiniteTransition(label = "Marker Animation")
    if (!enableAnimation) {
        return markerSize to 1f
    }
    return when (animationOption) {
        1 -> {
            val dynamicRadius = if (isRed) {
                infiniteTransition.animateFloat(
                    initialValue = markerSize * 0.8f,
                    targetValue = markerSize / 2,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1200, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = "Animation Option 1"
                ).value
            } else {
                infiniteTransition.animateFloat(
                    initialValue = markerSize / 2,
                    targetValue = markerSize * 0.8f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1200, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = "Animation Option 1"
                ).value
            }
            val dynamicAlpha = if (isRed) {
                infiniteTransition.animateFloat(
                    initialValue = 0.6f,
                    targetValue = 0.9f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1500, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = "Animation  Option 1"
                ).value
            } else {
                infiniteTransition.animateFloat(
                    initialValue = 0.9f,
                    targetValue = 0.5f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(1500, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = "Animation  Option 1"
                ).value
            }
            dynamicRadius to dynamicAlpha
        }
        2 -> {
            val dynamicRadius = if (isRed) {
                infiniteTransition.animateFloat(
                    initialValue = markerSize * 1.5f,
                    targetValue = markerSize / 2,
                    animationSpec = infiniteRepeatable(
                        animation = tween(750, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = "Animation  Option 2"
                ).value
            } else {
                infiniteTransition.animateFloat(
                    initialValue = markerSize / 2,
                    targetValue = markerSize * 1.5f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(750, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    ), label = "Animation Option 2"
                ).value
            }
            val dynamicAlpha = infiniteTransition.animateFloat(
                initialValue = 0.9f,
                targetValue = 0.1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(750, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ), label = "Animation Option 2"
            ).value
            dynamicRadius to dynamicAlpha
        }
        3 -> {
            val dynamicRadius = infiniteTransition.animateFloat(
                initialValue = if (isRed) markerSize * 0.99f else markerSize / 2,
                targetValue = if (isRed) markerSize / 2 else markerSize * 0.99f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1150, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ), label = "Animation Option 3"
            ).value
            dynamicRadius to 0.8f
        }
        else -> markerSize to 1f
    }
}


private fun getTappedImagePointHelper(
    tapOffset: Offset,
    translationX: Float,
    translationY: Float,
    scale: Float
): Offset {
    return (tapOffset - Offset(translationX, translationY)) / scale
}


private fun calculateDoubleTapTransform(
    tapOffset: Offset,
    currentScale: Float,
    translationX: Float,
    translationY: Float,
    containerWidth: Float,
    containerHeight: Float,
    imageWidth: Float,
    imageHeight: Float
): Triple<Float, Float, Float> {
    val tappedImagePoint = getTappedImagePointHelper(tapOffset, translationX, translationY, currentScale)
    // Decide on the new scale.
    val newScale = if (currentScale == 1f) 2f else 1f
    // Calculate new translations.
    var newTranslationX = tapOffset.x - tappedImagePoint.x * newScale
    var newTranslationY = tapOffset.y - tappedImagePoint.y * newScale
    // Restrict the translation values.
    newTranslationX = restrictOffset(newTranslationX, newScale, containerWidth, imageWidth)
    newTranslationY = restrictOffset(newTranslationY, newScale, containerHeight, imageHeight)

    return Triple(newScale, newTranslationX, newTranslationY)
}


private fun  convertToLocal(
    tapOffset: Offset,
    scale: Float,
    translationX: Float,
    translationY: Float
): Offset {
    val matrix = Matrix().apply {
        postScale(scale, scale)
        postTranslate(translationX, translationY)
    }
    val inverse = Matrix().apply { matrix.invert(this) }
    val pts = floatArrayOf(tapOffset.x, tapOffset.y)
    inverse.mapPoints(pts)
    return Offset(pts[0], pts[1])
}


private inline fun handleTapGesture(
    tapOffset: Offset,
    scale: Float,
    translationX: Float,
    translationY: Float,
    squares: List<SquareInfo>,
    markerSize: Float,
    hapticFeedback: HapticFeedback,
    useVibration: Boolean,
    onSquareClick: (SquareInfo?, Offset?, String) -> Unit
) {
    val localTap = convertToLocal(
        tapOffset,
        scale,
        translationX,
        translationY
    )
    val clickedMarker = squares.find { sq ->
        val left = sq.offset.x
        val top = sq.offset.y
        val right = left + markerSize
        val bottom = top + markerSize
        localTap.x in left..right && localTap.y in top..bottom
    }
    if (useVibration) {
        if (clickedMarker != null)
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        else
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }
    onSquareClick(clickedMarker, if (clickedMarker == null) localTap else null, "tap")
}

@Composable
private fun rememberRippleAnimation(markerSize: Float): Pair<Float, Float> {
    val rippleTransition = rememberInfiniteTransition(label = "Ripple Animation")
    val rippleRadius by rippleTransition.animateFloat(
        initialValue = markerSize / 2,
        targetValue = markerSize * 2,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Linear Easing Ripple Animation"
    )
    val rippleAlpha by rippleTransition.animateFloat(
        initialValue = 0.99f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Linear Easing Ripple Animation"
    )
    return rippleRadius to rippleAlpha
}


private fun restrictOffset(
    currentOffset: Float,
    newScale: Float,
    containerSize: Float,
    imageSizePx: Float
): Float {
    val scaledImageSize = imageSizePx * newScale
    return if (scaledImageSize < containerSize) {
        (containerSize - scaledImageSize) / 2f
    } else {
        val minOffset = containerSize - scaledImageSize
        currentOffset.coerceIn(minOffset, 0f)
    }
}
