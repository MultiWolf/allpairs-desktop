package com.fleey.allpairs.ui.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import com.fleey.allpairs.data.entity.Param

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppMain(isDark: Boolean) {
  val paramList = remember { mutableStateListOf<Param>() }
  var paramIndex by remember { mutableStateOf(0) }
  
  val handleParamInteraction: (Param?) -> Unit = { param ->
    param?.let {
      paramList.add(it.id, it)
    } ?: run {
      paramList.add(Param(paramIndex, "因子${paramIndex + 1}"))
    }
    paramIndex++
  }
  
  // init params
  LaunchedEffect(true) {
    repeat(2) {
      val randomValues = ('a'..'z').shuffled().take(3).joinToString(separator = " ")
      handleParamInteraction(Param(it, "因子${it + 1}", sortedSetOf(randomValues)))
    }
  }
  
  val removeParamAtIndex: (Int) -> Unit = { index ->
    paramList.removeAt(index)
    paramIndex--
    paramList.forEachIndexed { i, param ->
      if (param.id != i) paramList[i] = param.copy(id = i)
    }
  }
  
  val addParamAfter: (Int) -> Unit = { index ->
    handleParamInteraction(Param(index + 1, "新因子${paramIndex + 1}"))
    for (i in index + 2 until paramList.size) {
      paramList[i] = paramList[i].copy(id = i)
    }
  }
  
  val pagerState = rememberPagerState { 2 }
  
  MainScaffold({ handleParamInteraction(null) }, pagerState) {
    MainContent(
      isDark,
      paramList = paramList,
      pagerState = pagerState,
      onUpdateParam = { paramList[it.id] = it },
      onRemoveParam = removeParamAtIndex,
      onAddParamAfter = addParamAfter
    )
  }
}