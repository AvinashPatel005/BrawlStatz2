package com.kal.brawlstatz2.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kal.brawlstatz2.data.ExpandableCardModel

class MapCardViewModel : ViewModel(){
    val currmaplist: MutableState<ExpandableCardModel> = mutableStateOf(ExpandableCardModel(null,false))
}