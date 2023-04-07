package com.kal.brawlstatz2.sealed

import com.kal.brawlstatz2.data.Brawler

sealed class DataState {
    class Success(val data : MutableList<Brawler>) : DataState()
    class Failure(val message : String) : DataState()
    class Sorted(val data : MutableList<Brawler>) : DataState()
    class Meta(val data : MutableList<Brawler>) : DataState()
    object Loading : DataState()
    object Empty : DataState()

}