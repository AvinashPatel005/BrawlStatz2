package com.kal.brawlstatz2.data.tracker

data class BrawlerStats(
    var name:String,
    var pro:String,
    var rank:String,
    var level:String,
    var currTrophy: String,
    var highTrophy: String,
    var bg : String,
    var unlocked:ArrayList<String>
)
