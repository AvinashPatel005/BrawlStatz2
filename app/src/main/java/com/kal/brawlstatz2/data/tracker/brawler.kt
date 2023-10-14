package com.kal.brawlstatz2.data.tracker

import androidx.compose.ui.graphics.Color

data class BrawlerStats(
    var name:String,
    var pro:String,
    var rank:String,
    var level:String,
    var currTrophy: String,
    var highTrophy: String,
    var gears:ArrayList<String>,
    var gdst:ArrayList<String>
)
