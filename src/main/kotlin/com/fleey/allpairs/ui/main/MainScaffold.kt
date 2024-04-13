package com.fleey.allpairs.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fleey.allpairs.data.entity.Param

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScaffold(
  onFabClick: (Param?) -> Unit,
  pagerState: PagerState,
  content: @Composable (PaddingValues) -> Unit
) {
  Scaffold(
    backgroundColor = MaterialTheme.colors.background,
    topBar = {
      MainTopBar(
        pagerState = pagerState
      )
    },
    floatingActionButton = {
      if (pagerState.currentPage == 0) {
        FloatingActionButton(
          onClick = { onFabClick(null) },
          modifier = Modifier.padding(32.dp, 64.dp)
        ) {
          Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add Param")
        }
      }
    },
    content = content
  )
}