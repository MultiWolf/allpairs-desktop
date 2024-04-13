package com.fleey.allpairs.ui.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Table(
  modifier: Modifier = Modifier,
  row: Int = 0,
  col: Int = 0,
  dataRowHeight: Dp = 30.dp,
  border: Boolean = false,
  stripe: Boolean = false,
  borderWidth: Dp = Dp.Hairline,
  onRowSelected: ((Int, Boolean) -> Unit)? = null,
  headerData: List<String> = listOf(),
  headerRow: @Composable ((Int) -> Unit)? = null,
  dataRow: @Composable (Int, Int) -> Unit
) {
  val hScrollState = rememberLazyListState()
  
  val selectedRowIndex = remember { mutableStateOf(-1) }
  
  val rowModifier = Modifier.width(100.dp).height(dataRowHeight)
  val borderModifier = if (border) Modifier.border(
    BorderStroke(
      borderWidth,
      MaterialTheme.colors.secondary
    )
  ) else Modifier
  
  LazyColumn(
    modifier = modifier,
    state = rememberLazyListState()
  ) {
    stickyHeader {
      LazyRow(
        modifier = Modifier.fillMaxWidth().height(dataRowHeight)
          .background(MaterialTheme.colors.secondaryVariant),
        state = hScrollState
      ) {
        items(col) { colIndex ->
          Box(
            modifier = rowModifier.then(borderModifier),
            contentAlignment = Alignment.Center
          ) {
            headerRow?.invoke(colIndex) ?: Text(
              text = headerData.getOrNull(colIndex).orEmpty(),
              style = MaterialTheme.typography.subtitle1,
              color = MaterialTheme.colors.primaryVariant
            )
          }
        }
      }
      if (!border) Divider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray)
    }
    
    items(row) { rowIndex ->
      var wholeRowModifier = Modifier.fillMaxWidth().height(dataRowHeight)
        .clickable {
          selectedRowIndex.value = if (selectedRowIndex.value == rowIndex) -1 else rowIndex
          onRowSelected?.invoke(rowIndex, selectedRowIndex.value == rowIndex)
        }
      
      if (stripe && rowIndex % 2 == 1) {
        wholeRowModifier = wholeRowModifier.background(color = Color.Gray.copy(alpha = 0.191f))
      }
      if (selectedRowIndex.value == rowIndex) {
        wholeRowModifier = wholeRowModifier.background(color = Color.Gray.copy(alpha = 0.382f))
      }
      
      LazyRow(
        modifier = wholeRowModifier,
        state = hScrollState,
        verticalAlignment = Alignment.CenterVertically
      ) {
        items(col) { colIndex ->
          Box(
            modifier = rowModifier.then(borderModifier),
            contentAlignment = Alignment.Center
          ) {
            dataRow.invoke(rowIndex, colIndex)
          }
        }
      }
      if (!border) Divider(
        modifier = Modifier.fillMaxWidth(),
        color = Color.LightGray
      )
    }
  }
}