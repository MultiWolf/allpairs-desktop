package com.fleey.allpairs.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainTopBar(
  modifier: Modifier = Modifier,
  pagerState: PagerState,
) {
  val scope = rememberCoroutineScope()
  TopAppBar(
    backgroundColor = MaterialTheme.colors.onSecondary,
    title = {
      val text = when (pagerState.currentPage) {
        1 -> "结果列表"
        else -> "Allpairs"
      }
      Text(text)
    },
    modifier = modifier,
    navigationIcon = {
      if (pagerState.currentPage != 0) {
        IconButton(
          onClick = { scope.launch { pagerState.animateScrollToPage(0) } }
        ) {
          Icon(Icons.Rounded.ArrowBack, "back")
        }
      }
    },
    elevation = 0.dp,
  )
}