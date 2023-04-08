package com.kal.brawlstatz2.data

import androidx.compose.ui.graphics.Color

data class MetaTier(val Tname:String , val Tier : ArrayList<Brawler>, val color : Color){
    constructor() :this("N",ArrayList(), Color.Black)
}
