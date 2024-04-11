package com.fleey.allpairs.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fleey.allpairs.data.entity.Param

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ParamItem(
  param: Param,
  onUpdate: (Param) -> Unit,
) {
  var name by remember { mutableStateOf(param.name) }
  var values by remember { mutableStateOf(param.values.joinToString(" ")) }
  
  val isNameValid = name.isNotBlank()
  val splitValues = remember(values) { values.split(" ").filterNot { it.isBlank() }.toSortedSet() }
  val isValuesValid = splitValues.isNotEmpty()
  
  LaunchedEffect(name, values) { onUpdate(param.copy(name = name, values = splitValues)) }
  
  Surface(elevation = 1.dp) {
    FlowRow(
      Modifier.padding(8.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
      OutlinedTextField(
        value = name,
        onValueChange = { name = it },
        label = { Text("因子 ${param.id + 1}") },
        singleLine = true,
        placeholder = { Text("因子名") },
        isError = !isNameValid,
        modifier = Modifier.weight(1f)
      )
      OutlinedTextField(
        value = values,
        onValueChange = { values = it },
        label = { Text("所有因子") },
        singleLine = true,
        placeholder = { Text("以空格间隔") },
        isError = !isValuesValid,
        modifier = Modifier.weight(3f)
      )
    }
  }
}
