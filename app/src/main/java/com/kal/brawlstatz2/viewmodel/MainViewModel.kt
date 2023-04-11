package com.kal.brawlstatz2.viewmodel
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kal.brawlstatz2.data.Brawler
import com.kal.brawlstatz2.data.MetaTier
import com.kal.brawlstatz2.ui.theme.*

class MainViewModel : ViewModel() {
    val list :ArrayList<Brawler> = ArrayList()
    val blist: MutableState<List<Brawler>> = mutableStateOf(listOf())
    val isLoading : MutableState<Boolean> = mutableStateOf(true)
    var size : Int=0
    var isSearching : MutableState<Boolean> = mutableStateOf(false)
    var sortedMetaList:ArrayList<Brawler> = ArrayList()
    val mlist :ArrayList<MetaTier> = ArrayList()
    var nestedList: MutableState<List<MetaTier>> = mutableStateOf(listOf())
    init {
        fetchData()
    }
    fun find(txt:String){
        val sortedlist :ArrayList<Brawler> = ArrayList()
        for(brawler in list){
            if(brawler.bname?.lowercase()?.startsWith(txt) == true||brawler.brare?.lowercase()?.startsWith(txt) == true){
               sortedlist.add(brawler)
            }
        }
        blist.value=sortedlist
    }
    private fun metaSorting(){
        sortedMetaList.addAll(list.sortedBy { it.tier })

        var Stier: ArrayList<Brawler> = ArrayList()
        var Atier: ArrayList<Brawler> = ArrayList()
        var Btier: ArrayList<Brawler> = ArrayList()
        var Ctier: ArrayList<Brawler> = ArrayList()
        var Dtier: ArrayList<Brawler> = ArrayList()
        var Ftier: ArrayList<Brawler> = ArrayList()

        for(brawl in sortedMetaList){
            when(brawl.tier?.get(0)){
                in '0'..'9' -> Stier.add(brawl)
                'A' -> Atier.add(brawl)
                'B' -> Btier.add(brawl)
                'C' -> Ctier.add(brawl)
                'D' -> Dtier.add(brawl)
                'F' -> Ftier.add(brawl)
            }
        }
        mlist.add(MetaTier("S",Stier, Color(0xFFff7e7e)))
        mlist.add(MetaTier("A",Atier, Color(0xFFffbf7f)))
        mlist.add(MetaTier("B",Btier, Color(0xFFffde7f)))
        mlist.add(MetaTier("C",Ctier, Color(0xFFfeff7f)))
        mlist.add(MetaTier("D",Dtier, Color(0xFFbeff7d)))
        mlist.add(MetaTier("E",Ftier, Color(0xFF7eff80)))

        nestedList.value=mlist
    }
    private fun fetchData(){
        FirebaseDatabase.getInstance().getReference("brawlers")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for(DataSnap in snapshot.children){
                        val brawler = DataSnap.getValue(Brawler::class.java)
                        if (brawler != null) {
                            brawler.bpro =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname!!.lowercase() + "%2F" + DataSnap.key + ".webp?alt=media&token="+brawler.zver
                            brawler.bmodel =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2FF" + brawler.bname.lowercase() + "%2F" + DataSnap.key + "_Skin-Default.webp?alt=media&token="+brawler.zver
                            brawler.g1 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2FGD-" + DataSnap.key + "1.webp?alt=media&token="+brawler.zver
                            brawler.g2 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2FGD-" + DataSnap.key + "2.webp?alt=media&token="+brawler.zver
                            brawler.s1 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2FSP-" + DataSnap.key + "1.webp?alt=media&token="+brawler.zver
                            brawler.s2 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2FSP-" + DataSnap.key + "2.webp?alt=media&token="+brawler.zver
                            if (brawler.c1n != null) brawler.c1 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.c1n.lowercase() + "%2F" + brawler.c1n[0] + brawler.c1n.substring(1).lowercase() + ".webp?alt=media&token="+brawler.zver
                            if (brawler.c2n != null) brawler.c2 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.c2n.lowercase() + "%2F" + brawler.c2n[0] + brawler.c2n.substring(1).lowercase() + ".webp?alt=media&token="+brawler.zver
                            if (brawler.c3n != null) brawler.c3 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.c3n.lowercase() + "%2F" + brawler.c3n[0] + brawler.c3n.substring(1).lowercase() + ".webp?alt=media&token="+brawler.zver
                            if (brawler.trait != null) brawler.trait =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/traits%2F" + brawler.trait + ".png?alt=media&token="+brawler.zver else brawler.trait = "null"
                            brawler.bgear1 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/gears%2F" + brawler.bgear1?.lowercase() + ".webp?alt=media&token="+brawler.zver
                            brawler.bgear2 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/gears%2F" + brawler.bgear2?.lowercase() + ".webp?alt=media&token="+brawler.zver
                            if (brawler.bgear3 != null) brawler.bgear3 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/gears%2F" + brawler.bgear3!!.lowercase() + ".webp?alt=media&token="+brawler.zver
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
                        if(brawler!=null) list.add(brawler)
                    }
                    size = list.size
                    blist.value=list
                    metaSorting()
                    isLoading.value=false
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
}