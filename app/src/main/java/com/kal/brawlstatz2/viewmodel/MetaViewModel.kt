package com.kal.brawlstatz2.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kal.brawlstatz2.data.ExpandableCardModel

class MetaViewModel : ViewModel() {
    val mclist: MutableState<ExpandableCardModel> = mutableStateOf(ExpandableCardModel(null,false))

}