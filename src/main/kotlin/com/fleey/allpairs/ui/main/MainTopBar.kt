package com.fleey.allpairs.ui.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fleey.allpairs.extender.scrollToPageWithAnimTo
import com.fleey.allpairs.ui.anim.fromLeftToRightAnim
import com.fleey.allpairs.ui.anim.fromRightToLeft
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainTopBar(
  modifier: Modifier = Modifier,
  pagerState: PagerState,
) {
  val scope = rememberCoroutineScope()
  
  val titles = listOf("Allpairs", "结果列表")
  val title = titles.getOrNull(pagerState.currentPage) ?: "Allpairs"
  val subTitle = when (pagerState.currentPage) {
    1 -> "查看结果"
    else -> "for Desktop"
  }
  
  var lastPageIndex by remember { mutableStateOf(pagerState.currentPage) }
  
  LaunchedEffect(pagerState.currentPage) {
    lastPageIndex = pagerState.currentPage
  }
  
  TopAppBar(
    backgroundColor = MaterialTheme.colors.onSecondary,
    title = {
      AnimatedContent(
        targetState = title,
        transitionSpec = if (pagerState.currentPage > lastPageIndex) fromLeftToRightAnim else fromRightToLeft
      ) { targetTitle ->
        Column {
          Text(text = targetTitle)
          if (subTitle.isNotEmpty()) {
            Text(text = subTitle, style = MaterialTheme.typography.subtitle2)
          }
        }
      }
    },
    modifier = modifier,
    navigationIcon = {
      if (pagerState.currentPage != 0) {
        IconButton(
          onClick = { scope.launch { pagerState.scrollToPageWithAnimTo(0) } }
        ) {
          Icon(Icons.Rounded.ArrowBack, "back")
        }
      }
    },
    elevation = 0.dp,
  )
}