package com.fleey.allpairs.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainTopBar(
  modifier: Modifier = Modifier,
  isDark: Boolean,
  toggleTheme: () -> Unit,
  pagerState: PagerState,
) {
  val scope = rememberCoroutineScope()
  TopAppBar(
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
          Icon(Icons.Default.ArrowBack, "back")
        }
      }
    },
    actions = {
      IconButton(onClick = toggleTheme) {
        val icon = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode
        
        Icon(icon, contentDescription = "Toggle Light/Dark Theme")
      }
    },
  )
}