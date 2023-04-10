package com.kal.brawlstatz2.data

data class ExpandableCardModel(var id: String?, var isExpanded:Boolean){
    constructor():this(null,false)
}