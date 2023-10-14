package com.kal.brawlstatz2.data.tracker

data class Profile(
    var name:String,
    var dp: String,
    var tag: String,
    val player_club: PlayerClub,
    val xp: Xp,
    val trophy: Trophy,
    var updated: String,
    val victories: Victories,
    val league: League,
    var main: String,
    var bwin:Int,
    var bloss:Int,
    val brawler : ArrayList<BrawlerStats>,
    val prevClubs : ArrayList<PlayerClub>,
    val battleLog : ArrayList<BattleResult>,
    var gadgets : String,
    var starpowers :String,
    var gears : String
)