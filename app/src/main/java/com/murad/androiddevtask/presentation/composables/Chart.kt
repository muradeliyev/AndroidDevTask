package com.murad.androiddevtask.presentation.composables

import android.view.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.tooling.preview.Preview


/**
 * Created on 05 February 2024, 8:29 PM
 * @author Murad Eliyev
 */


@Composable
fun Chart() {

}


@Stable
data class ChartData(
    val percentage: Int
)


@Preview
@Composable
private fun Demo() {
    Chart()
}
