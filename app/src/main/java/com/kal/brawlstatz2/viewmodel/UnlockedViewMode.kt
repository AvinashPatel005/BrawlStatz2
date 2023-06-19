package com.kal.brawlstatz2.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kal.brawlstatz2.data.ExpandableCardModel
import com.kal.brawlstatz2.data.ExpandedUnlockedBrawler

class UnlockedViewModel : ViewModel() {

    val c1list: MutableState<ExpandedUnlockedBrawler> = mutableStateOf(ExpandedUnlockedBrawler(0))
}