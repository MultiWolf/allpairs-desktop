package com.fleey.allpairs.ui.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CustomBottomSheet(
  state: ModalBottomSheetState,
  sheetContent: @Composable ColumnScope.() -> Unit,
  content: @Composable () -> Unit
) {
  ModalBottomSheetLayout(
    sheetState = state,
    sheetContent = sheetContent,
  ) {
    content()
  }
}