package com.fleey.customwindow.icon

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

private var _vector: ImageVector? = null

public val CustomIcons.Maximize: ImageVector
  get() {
    if (_vector != null) {
      return _vector!!
    }
    _vector = ImageVector.Builder(
      name = "vector",
      defaultWidth = 24.dp,
      defaultHeight = 24.dp,
      viewportWidth = 24f,
      viewportHeight = 24f
    ).apply {
      path(
        fill = SolidColor(Color(0xFFFFFFFF)),
        fillAlpha = 1.0f,
        stroke = null,
        strokeAlpha = 1.0f,
        strokeLineWidth = 1.0f,
        strokeLineCap = StrokeCap.Butt,
        strokeLineJoin = StrokeJoin.Miter,
        strokeLineMiter = 1.0f,
        pathFillType = PathFillType.EvenOdd
      ) {
        moveTo(21.8182f, 2.18182f)
        horizontalLineTo(2.18182f)
        verticalLineTo(21.8182f)
        horizontalLineTo(21.8182f)
        verticalLineTo(2.18182f)
        close()
        moveTo(0f, 0f)
        verticalLineTo(24f)
        horizontalLineTo(24f)
        verticalLineTo(0f)
        horizontalLineTo(0f)
        close()
      }
    }.build()
    return _vector!!
  }