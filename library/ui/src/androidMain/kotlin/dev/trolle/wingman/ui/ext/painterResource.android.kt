package dev.trolle.wingman.ui.ext

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import dev.trolle.af.wingman.ui.R

@Composable
actual fun painterResource(res: String): Painter {
    val id = drawableId(res)
    return androidx.compose.ui.res.painterResource(id)
}

private fun drawableId(res: String): Int {
    val imageName = res.substringAfterLast("/").substringBeforeLast(".")
    val drawableClass = R.drawable::class.java
    val field = drawableClass.getDeclaredField(imageName)
   return field.get(drawableClass) as Int
}