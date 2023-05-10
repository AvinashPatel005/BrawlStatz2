package com.kal.brawlstatz2.data

import androidx.compose.ui.graphics.Color
data class Brawler(
    val id: Int?=0,
    val bname: String?=null,
    val brare: String?=null,
    var bpro: String?=null,
    val babout: String?=null,
    var bmodel: String?=null,
    var c1: String?=null,
    val c1n: String?=null,
    var c2: String?=null,
    val c2n: String?=null,
    var c3: String?=null,
    val c3n: String?=null,
    var g1: String?=null,
    val g1t: String?=null,
    var g2: String?=null,
    val g2t: String?=null,
    var s1: String?=null,
    val s1t: String?=null,
    var s2: String?=null,
    val s2t: String?=null,
    val battack: String?=null,
    val bsuper: String?=null,
    val battackt: String?=null,
    val bsupert: String?=null,
    var trait: String?=null,
    val tier: String?=null,
    val bstarpower: String?=null,
    val bgadget: String?=null,
    var bgear1: String?=null,
    var bgear2: String?=null,
    var bgear3: String?=null,
    val zver : String?=null,
    var color : Color=Color.Black,

){
    constructor() :this(0,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
        Color.Black)
}

