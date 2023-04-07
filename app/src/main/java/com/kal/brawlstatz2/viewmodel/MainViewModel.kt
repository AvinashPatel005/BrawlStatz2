package com.kal.brawlstatz2.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kal.brawlstatz2.data.Brawler
import com.kal.brawlstatz2.sealed.DataState
import com.kal.brawlstatz2.ui.theme.*

class MainViewModel : ViewModel() {
    val response : MutableState<DataState> = mutableStateOf(DataState.Empty)
    var tempList = mutableListOf<Brawler>()
    init {
        fetchDataFromFirebase()
    }
    fun BrawlerClicked(){
        response.value =DataState.Success(tempList)
    }


    fun metaClicked(){
        val tempList2 = ArrayList<Brawler>(tempList)
        for(brawler in tempList){
            tempList2.sortBy { it.tier }
        }
        response.value =DataState.Meta(tempList2)
    }

    fun findData(txt:String){
        val tempList1 = ArrayList<Brawler>()
        for(brawler in this.tempList){
            if(brawler.bname?.lowercase()?.startsWith(txt)  == true) tempList1.add(brawler)
        }
        response.value=DataState.Sorted(tempList1)
    }

    fun sortData() {
        val tempList1 = ArrayList<Brawler>()
        for(brawler in this.tempList){
            if(brawler.brare=="EPIC"){
                tempList1.add(brawler)
            }
        }

        response.value=DataState.Sorted(tempList1)
    }

    private fun fetchDataFromFirebase() {

        response.value = DataState.Loading
        FirebaseDatabase.getInstance().getReference("brawlers")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(DataSnap in snapshot.children){
                        val brawler = DataSnap.getValue(Brawler::class.java)
                        if (brawler != null) {
                            brawler.bpro =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname!!.lowercase() + "%2F" + DataSnap.key + ".webp?alt=media"
                            brawler.bmodel =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2FF" + brawler.bname.lowercase() + "%2F" + DataSnap.key + "_Skin-Default.webp?alt=media"
                            brawler.g1 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2FGD-" + DataSnap.key + "1.webp?alt=media"
                            brawler.g2 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2FGD-" + DataSnap.key + "2.webp?alt=media"
                            brawler.s1 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2FSP-" + DataSnap.key + "1.webp?alt=media"
                            brawler.s2 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2FSP-" + DataSnap.key + "2.webp?alt=media"
                            if (brawler.c1n != null) brawler.c1 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.c1n.lowercase() + "%2F" + brawler.c1n[0] + brawler.c1n.substring(1).lowercase() + ".webp?alt=media"
                            if (brawler.c2n != null) brawler.c2 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.c2n.lowercase() + "%2F" + brawler.c2n[0] + brawler.c2n.substring(1).lowercase() + ".webp?alt=media"
                            if (brawler.c3n != null) brawler.c3 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.c3n.lowercase() + "%2F" + brawler.c3n[0] + brawler.c3n.substring(1).lowercase() + ".webp?alt=media"
                            if (brawler.trait != null) brawler.trait =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/traits%2F" + brawler.trait + ".png?alt=media" else brawler.trait =
                                "null"

                            when(brawler.brare){
                                "LEGENDARY" -> brawler.color= legendary
                                "MYTHIC" -> brawler.color= mythic
                                "CHROMATIC" -> brawler.color= chromatic
                                "EPIC" -> brawler.color= epic
                                "SUPER RARE" -> brawler.color= superrare
                                "RARE" -> brawler.color= rare
                                "STARTING" -> brawler.color= starting
                            }
                        }
                        if(brawler!=null) tempList.add(brawler)
                    }
                    response.value=DataState.Success(tempList)
                }

                override fun onCancelled(error: DatabaseError) {
                    response.value=DataState.Failure(error.message)
                }

            })
    }

}