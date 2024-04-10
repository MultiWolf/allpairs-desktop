package com.fleey.allpairs.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fleey.allpairs.data.entity.Param

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppMain(
  isDark: Boolean,
  toggleTheme: () -> Unit
) {
  val paramList: MutableList<Param> = remember {
    mutableStateListOf<Param>().apply {
      add(Param("因子1", sortedSetOf("a", "b", "c")))
      add(Param("因子2", sortedSetOf("d", "e", "f")))
    }
  }
  
  val onFabClick = {
    paramList.add(Param())
    Unit
  }
  
  val pagerState = rememberPagerState { 2 }
  
  MainScaffold(isDark, toggleTheme, onFabClick, pagerState) { pading ->
    MainContent(
      modifier = Modifier.padding(16.dp).padding(pading),
      paramList = paramList,
      pagerState = pagerState,
      onUpdateParam = { index, it ->
        paramList[index] = it
      },
      onRemoveParam = { index ->
        paramList.removeAt(index)
      }
    )
  }
}