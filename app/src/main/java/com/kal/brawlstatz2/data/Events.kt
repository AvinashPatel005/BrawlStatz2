package com.kal.brawlstatz2.data

import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import com.kal.brawlstatz2.R

data class Events(
    val cardName : String,
    val route : String,
    val color : Color,
    val drawable : Int,
    val enabled : Boolean
){
    constructor():this("null", "null", Color.Black,R.drawable.placeholder1,false)
}
