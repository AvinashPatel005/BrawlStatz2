package com.kal.brawlstatz2.data.events

data class Slot(
    val background: String,
    val emoji: String,
    val hash: String,
    val hideForSlot: Int,
    val hideable: Boolean,
    val id: Int,
    val listAlone: Boolean,
    val name: String
)