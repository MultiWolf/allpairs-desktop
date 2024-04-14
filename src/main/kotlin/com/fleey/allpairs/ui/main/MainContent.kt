package com.fleey.allpairs.ui.main

import androidx.compose.animation.*
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.fleey.allpairs.data.entity.Param
import com.fleey.allpairs.extender.scrollToPageWithAnimTo
import com.fleey.allpairs.ui.main.page.EditPage
import com.fleey.allpairs.ui.main.page.ResultPage
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun MainContent(
  isDark: Boolean,
  paramList: List<Param>,
  pagerState: PagerState,
  onUpdateParam: (Param) -> Unit,
  onRemoveParam: (Int) -> Unit,
  onAddParamAfter: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  val resultBottomSheetState = rememberModalBottomSheetState(
    ModalBottomSheetValue.Hidden,
    skipHalfExpanded = true,
  )
  
  val scope = rememberCoroutineScope()
  
  HorizontalPager(
    state = pagerState,
    modifier = Modifier.fillMaxSize(),
    userScrollEnabled = false,
  ) { page ->
    when (page) {
      0 -> {
        EditPage(
          paramList,
          onUpdateParam,
          onRemoveParam,
          onAddParamAfter,
          { scope.launch { pagerState.scrollToPageWithAnimTo(1) } },
          modifier
        )
        scope.launch { resultBottomSheetState.hide() }
      }
      
      1 -> {
        ResultPage(
          isDark,
          paramList,
          modifier,
          scope,
          resultBottomSheetState
        )
      }
    }
  }
}


@OptIn(ExperimentalFoundationApi::class)
fun pageOffsetFraction(pagerState: PagerState): (AnimatedContentTransitionScope<String>) -> () -> ContentTransform =
  {
    val pageOffset = pagerState.currentPageOffsetFraction
    val isMovingForward = pagerState.currentPage > pagerState.targetPage
    
    val (inInitialOffsetX, outTargetOffsetX) = if (isMovingForward) {
      Pair({ fullWidth: Int -> fullWidth }, { fullWidth: Int -> -fullWidth })
    } else {
      Pair({ fullWidth: Int -> -fullWidth }, { fullWidth: Int -> fullWidth })
    }
    
    {
      slideInHorizontally(
        initialOffsetX = inInitialOffsetX,
        animationSpec = TweenSpec(durationMillis = 300)
      ) + fadeIn() togetherWith slideOutHorizontally(
        targetOffsetX = outTargetOffsetX,
        animationSpec = TweenSpec(durationMillis = 300)
      ) + fadeOut()
    }
  }