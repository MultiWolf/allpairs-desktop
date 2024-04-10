package com.fleey.allpairs.ui.component.allpairs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
  val selectionState = remember { mutableStateOf(-1) }
  
  LazyColumn(modifier = modifier, state = rememberLazyListState()) {
    item {
      HeaderRow(col, border, borderWidth, headerData, headerRow)
    }
    items(row) { rowIndex ->
      DataRow(
        rowIndex = rowIndex,
        col = col,
        border = border,
        borderWidth = borderWidth,
        stripe = stripe,
        dataRowHeight = dataRowHeight,
        isSelected = selectionState.value == rowIndex,
        onRowSelected = { selected ->
          selectionState.value = if (selected) rowIndex else -1
          onRowSelected?.invoke(rowIndex, selected)
        },
        dataRow = dataRow
      )
    }
  }
}

@Composable
private fun HeaderRow(
  col: Int,
  border: Boolean,
  borderWidth: Dp,
  headerData: List<String>,
  headerRow: @Composable ((Int) -> Unit)?
) {
  LazyRow {
    items(col) { colIndex ->
      TableCell(border, borderWidth) {
        headerRow?.invoke(colIndex) ?: Text(
          headerData[colIndex],
          style = MaterialTheme.typography.subtitle1,
          color = Color.Gray
        )
      }
    }
  }
  if (!border) {
    Divider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray)
  }
}

@Composable
private fun DataRow(
  rowIndex: Int,
  col: Int,
  border: Boolean,
  borderWidth: Dp,
  stripe: Boolean,
  dataRowHeight: Dp,
  isSelected: Boolean,
  onRowSelected: (Boolean) -> Unit,
  dataRow: @Composable (Int, Int) -> Unit
) {
  val backgroundColor =
    if (isSelected) Color.Gray.copy(alpha = if (stripe) 0.382f else 0.191f) else MaterialTheme.colors.background
  
  Row(
    modifier = Modifier.fillMaxWidth().height(dataRowHeight).background(backgroundColor).clickable {
      onRowSelected(!isSelected)
    }
  ) {
    LazyRow {
      items(col) { colIndex ->
        TableCell(border, borderWidth) {
          dataRow(rowIndex, colIndex)
        }
      }
    }
  }
  if (!border) {
    Divider(modifier = Modifier.fillMaxWidth(), color = Color.LightGray)
  }
}

@Composable
private fun TableCell(border: Boolean, borderWidth: Dp, content: @Composable () -> Unit) {
  val cellModifier = Modifier.width(100.dp).height(30.dp)
  Box(
    modifier = if (border) cellModifier.border(
      BorderStroke(
        width = borderWidth,
        color = MaterialTheme.colors.secondary
      )
    ) else cellModifier,
    contentAlignment = Alignment.Center
  ) {
    content()
  }
}
