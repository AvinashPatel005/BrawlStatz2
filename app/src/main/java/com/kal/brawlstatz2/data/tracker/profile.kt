package com.kal.brawlstatz2.data.tracker

import com.kal.brawlstatz2.data.Brawler

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
    var bdraw:Int,
    val brawler : ArrayList<BrawlerStats>,
    val prevClubs : ArrayList<PlayerClub>,
    val battleLog : ArrayList<BattleResult>,
    var trophyArray : ArrayList<Int>,
    var gadgets : String,
    var starpowers :String,
    var gears : String,
    var maxBrawler: Int
)