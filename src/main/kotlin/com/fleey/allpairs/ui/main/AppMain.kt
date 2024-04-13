package com.fleey.allpairs.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import com.fleey.allpairs.data.entity.Param

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppMain(
  isDark: Boolean,
  toggleTheme: () -> Unit
) {
  val paramList = remember { mutableStateListOf<Param>() }
  var paramIndex by remember { mutableStateOf(0) }
  
  val onFabClick: (Param?) -> Unit = {
    val param = it ?: Param(paramIndex, "因子${paramIndex + 1}")
    paramIndex++
    paramList.add(param)
    Unit
  }
  
  // init Params
  LaunchedEffect(true) {
    repeat(2) {
      val randomValues = ('a'..'z').shuffled().take(3).joinToString(separator = " ")
      onFabClick(Param(it, "因子${it + 1}", sortedSetOf(randomValues)))
    }
  }
  
  val onRemoveParam: (Int) -> Unit = { removedParamIndex ->
    paramList.removeAt(removedParamIndex)
    paramIndex--
    paramList.forEachIndexed { index, param ->
      if (param.id != index) paramList[index] = param.copy(id = index)
    }
  }
  
  val pagerState = rememberPagerState { 2 }
  
  MainScaffold(isDark, toggleTheme, onFabClick, pagerState) {
    MainContent(
      isDark,
      paramList = paramList,
      pagerState = pagerState,
      onUpdateParam = { paramList[it.id] = it },
      onRemoveParam = onRemoveParam
    )
  }
}