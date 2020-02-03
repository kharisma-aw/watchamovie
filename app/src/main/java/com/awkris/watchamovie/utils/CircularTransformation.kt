package com.awkris.watchamovie.utils

import android.graphics.*
import com.squareup.picasso.Transformation


class CircularTransformation : Transformation {
    override fun transform(source: Bitmap): Bitmap? {
        val size = Math.min(source.width, source.height)
        val width = (source.width - size) / 2
        val height = (source.height - size) / 2
        val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader =
            BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        if (width != 0 || height != 0) { // source isn't square, move viewport to center
            val matrix = Matrix()
            matrix.setTranslate(-width.toFloat(), -height.toFloat())
            shader.setLocalMatrix(matrix)
        }
        paint.setShader(shader)
        paint.setAntiAlias(true)
        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)
        source.recycle()
        return bitmap
    }

    override fun key(): String? {
        return "CircularTransformation()"
    }
}