package com.kal.brawlstatz2.data.events

data class Map(
    val credit: String,
    val dataUpdated: Int,
    val disabled: Boolean,
    val environment: Environment,
    val gameMode: GameMode,
    val hash: String,
    val id: Int,
    val imageUrl: String,
    val lastActive: Int,
    val link: String,
    val name: String,
    val new: Boolean,
    val stats: List<Stat>,
    val teamStats: List<Any>,
    val version: Int
)