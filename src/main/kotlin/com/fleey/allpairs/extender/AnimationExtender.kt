package com.fleey.allpairs.extender

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState

@OptIn(ExperimentalFoundationApi::class)
suspend fun PagerState.scrollToPageWithAnimTo(page: Int, durationMillis: Int = 360) {
  this.animateScrollToPage(
    page = page,
    animationSpec = TweenSpec(
      durationMillis = durationMillis,
      easing = FastOutSlowInEasing
    )
  )
}