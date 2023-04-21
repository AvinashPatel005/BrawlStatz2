package com.kal.brawlstatz2.data.events

data class Active(
    val endTime: String,
    val historyLength: Int,
    val map: Map,
    val modifier: Any,
    val predicted: Boolean,
    val reward: Int,
    val slot: Slot,
    val startTime: String
)