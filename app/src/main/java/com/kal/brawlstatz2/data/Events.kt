package com.kal.brawlstatz2.data

import androidx.compose.ui.graphics.Color

data class Events(
    val cardName : String,
    val route : String,
    val color : Color,
    val enabled : Boolean
){
    constructor():this("null","null", Color.Black,false)
}
