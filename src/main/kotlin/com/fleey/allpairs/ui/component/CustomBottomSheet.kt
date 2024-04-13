package com.fleey.allpairs.ui.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomBottomSheet(
  state: ModalBottomSheetState,
  sheetContent: @Composable ColumnScope.() -> Unit,
  sheetShape: Shape = RoundedCornerShape(16.dp),
  content: @Composable () -> Unit
) {
  ModalBottomSheetLayout(
    modifier = Modifier,
    sheetShape = sheetShape,
    sheetState = state,
    sheetContent = sheetContent,
  ) {
    content()
  }
}