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
  val paramList: MutableList<Param> = remember { mutableStateListOf() }
  var paramIndex by remember { mutableStateOf(0) }
  
  val onFabClick: (Param?) -> Unit = {
    val param = it ?: Param(paramIndex, "因子${paramIndex + 1}")
    paramIndex++
    paramList.add(param)
    Unit
  }
  
  // init Params
  LaunchedEffect(true) {
    repeat(2) { onFabClick(null) }
  }
  
  val onRemoveParam: (Int) -> Unit = {
    paramList.removeAt(it)
    paramIndex--
    paramList.subList(it, paramList.size).forEachIndexed { i, param ->
      paramList[it + i] = param.copy(id = it + i, name = "因子${it + i + 1}")
    }
  }
  
  val pagerState = rememberPagerState { 2 }
  
  MainScaffold(isDark, toggleTheme, onFabClick, pagerState) {
    MainContent(
      paramList = paramList,
      pagerState = pagerState,
      onUpdateParam = { paramList[it.id] = it },
      onRemoveParam = onRemoveParam
    )
  }
}