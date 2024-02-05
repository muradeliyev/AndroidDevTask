package com.murad.androiddevtask.common.extensions

import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette


/**
 * Created on 03 February 2024, 6:08 PM
 * @author Murad Eliyev
 */


fun ImageView.getColorPalette(onSuccess: (Palette) -> Unit) {
    val bitmap = drawable.toBitmap()

    Palette.from(bitmap).generate { palette ->
        palette?.let {
            onSuccess(it)
        }
    }
}
