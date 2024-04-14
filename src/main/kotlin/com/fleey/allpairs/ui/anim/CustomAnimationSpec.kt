package com.fleey.allpairs.ui.anim

import androidx.compose.animation.*
import androidx.compose.animation.core.tween

const val durationMillis = 360
const val smallScale = 0.9f
const val bigScale = 1.1f

val fromLeftToRightAnim: AnimatedContentTransitionScope<String>.() -> ContentTransform = {
  slideInHorizontally(
    initialOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(durationMillis)
  ) + scaleIn(
    initialScale = smallScale,
    animationSpec = tween(durationMillis)
  ) + fadeIn() togetherWith slideOutHorizontally(
    targetOffsetX = { fullWidth -> -fullWidth },
    animationSpec = tween(durationMillis)
  ) + scaleOut(
    targetScale = bigScale,
    animationSpec = tween(durationMillis)
  ) + fadeOut()
}

val fromRightToLeft: AnimatedContentTransitionScope<String>.() -> ContentTransform = {
  slideInHorizontally(
    initialOffsetX = { fullWidth -> -fullWidth },
    animationSpec = tween(durationMillis)
  ) + scaleIn(
    initialScale = bigScale,
    animationSpec = tween(durationMillis)
  ) + fadeIn() togetherWith slideOutHorizontally(
    targetOffsetX = { fullWidth -> fullWidth },
    animationSpec = tween(durationMillis)
  ) + scaleOut(
    targetScale = smallScale,
    animationSpec = tween(durationMillis)
  ) + fadeOut()
}