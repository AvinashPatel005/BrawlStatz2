package com.kal.brawlstatz2.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kal.brawlstatz2.data.ExpandableCardModel

class CardsViewModel : ViewModel() {
    val clist :ArrayList<ExpandableCardModel> = ArrayList()
    val isExpanded : MutableState<Boolean> = mutableStateOf(false)
    val c1list: MutableState<ExpandableCardModel> = mutableStateOf(ExpandableCardModel(null,false))

}