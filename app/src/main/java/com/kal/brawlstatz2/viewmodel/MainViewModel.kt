package com.kal.brawlstatz2.viewmodel
import android.content.ContentValues.TAG
import android.os.Build
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.values
import com.google.firebase.ktx.Firebase
import com.kal.brawlstatz2.data.ApkInfo
import com.kal.brawlstatz2.data.Brawler
import com.kal.brawlstatz2.data.MetaTier
import com.kal.brawlstatz2.data.Trait
import com.kal.brawlstatz2.data.events.Active
import com.kal.brawlstatz2.data.events.event
import com.kal.brawlstatz2.data.tracker.BattleResult
import com.kal.brawlstatz2.data.tracker.BrawlerStats
import com.kal.brawlstatz2.data.tracker.League
import com.kal.brawlstatz2.data.tracker.PlayerClub
import com.kal.brawlstatz2.data.tracker.Profile
import com.kal.brawlstatz2.data.tracker.Trophy
import com.kal.brawlstatz2.data.tracker.Victories
import com.kal.brawlstatz2.data.tracker.Xp
import com.kal.brawlstatz2.retrofit.Api
import com.kal.brawlstatz2.retrofit.RetrofitInstance
import com.kal.brawlstatz2.ui.theme.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.lang.Exception
import kotlin.math.log

class MainViewModel : ViewModel() {
    val list :ArrayList<Brawler> = ArrayList()
    var sortedMetaList:ArrayList<Brawler> = ArrayList()
    var traits : ArrayList<Trait> = ArrayList();
    val blist: MutableState<List<Brawler>> = mutableStateOf(listOf())
    var nestedList: MutableState<List<MetaTier>> = mutableStateOf(listOf())

    var previewBrawler:MutableState<Brawler?> = mutableStateOf(null)

    var apiLock:MutableState<Boolean> = mutableStateOf(true)
    var downloadId:Long=-1L

    val isLoading : MutableState<Boolean> = mutableStateOf(true)
    var isSearching : MutableState<Boolean> = mutableStateOf(false)
    var size : Int=0
    var isLoadingStats : MutableState<Int> = mutableIntStateOf(0)

    val _activeList :ArrayList<Active> = ArrayList()
    val activeList: MutableState<List<Active>> = mutableStateOf(listOf())

    val _upcomingList :ArrayList<Active> = ArrayList()
    val upcomingList: MutableState<List<Active>> = mutableStateOf(listOf())

    val _info  = ApkInfo("0","");
    var isUpdateAvailable : MutableState<Boolean> = mutableStateOf(false)

    val _changelog : ArrayList<String> = ArrayList()
    val changelog: MutableState<List<String>> = mutableStateOf(listOf())

    var _tracker :Profile= Profile("","","", PlayerClub("","","","",""), Xp("","0/1"), Trophy("","0/1","","",""),"",
        Victories("","","",""), League("","",""),"",0,0, arrayListOf(), arrayListOf(),
        arrayListOf(),"","",""
    )
    val tracker : MutableState<Profile> = mutableStateOf(Profile("","","", PlayerClub("","","","",""), Xp("","0/1"), Trophy("","0/1","","",""),"",
        Victories("","","",""), League("","",""), "",0,0,arrayListOf(), arrayListOf(),
        arrayListOf(),"","",""
    ))


    val _bp :ArrayList<String> = ArrayList()
    val bp: MutableState<List<String>> = mutableStateOf(listOf())
    val _pl :ArrayList<String> = ArrayList()
    val pl: MutableState<List<String>> = mutableStateOf(listOf())
    val _cl :ArrayList<String> = ArrayList()
    val cl: MutableState<List<String>> = mutableStateOf(listOf())
    var timeFromServer : MutableState<Long> = mutableLongStateOf(0)
    var metaVer : MutableState<String> = mutableStateOf("")

    init {
        appUpdater()
        fetchData()
        getData()
        getEvent()
        getCurrTime()
    }

    fun brawlStats(
        tag : String,
    ){
        isLoadingStats.value=0
        val retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl("https://brawlace.com/")
            .build()
        val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
            throwable.printStackTrace()
            isLoadingStats.value=2
        }
        val api = retrofit.create(Api::class.java)

        CoroutineScope(Dispatchers.IO+coroutineExceptionHandler).launch {
            _tracker=Profile("","","", PlayerClub("","","","",""), Xp("","0/1"), Trophy("","0/1","","",""),"",
                Victories("","","",""), League("","",""),"",0,0, arrayListOf(), arrayListOf(),
                arrayListOf(),"","",""
            )
            tracker.value=Profile("","","", PlayerClub("","","","",""), Xp("","0/1"), Trophy("","0/1","","",""),"",
                Victories("","","",""), League("","",""),"",0,0, arrayListOf(), arrayListOf(),
                arrayListOf(),"","",""
            )
            _tracker.brawler.clear()
            _tracker.battleLog.clear()
            _tracker.prevClubs.clear()
            val result = api.getPageResponse("players/%23${tag.trim().uppercase()}")

            val htmlDoc = Jsoup.parse(result.body().toString())
            _tracker.name = htmlDoc.getElementsByClass("pt-3").text()
            _tracker.dp = htmlDoc.getElementsByClass("pt-3").select("img").attr("src")
            _tracker.tag = htmlDoc.getElementsByClass("badge bg-black").text()
            _tracker.player_club.clubNane = htmlDoc.getElementsByClass("text-center py-3").select("h4 a").text()
//            _tracker.player_club.clubBanner=htmlDoc.getElementsByClass("_10NE7YpQTAq_Eh89NXDUyw").attr("src")
            _tracker.player_club.clubLink=htmlDoc.getElementsByClass("text-center py-3").select("h4 a").attr("href")
            println(_tracker.player_club.clubLink)
            _tracker.xp.level = htmlDoc.getElementsByClass("table table-sm table-hover table-bordered").select("tbody tr td")[0].text()
//            _tracker.xp.progress = htmlDoc.getElementsByClass("_3lMfMVxY-knKo2dnVHMCWG _21sSMvccqXG6cJU-5FNqzv _1IVG5wQ6FsDh26OcvvGzDd")[0].text()
            _tracker.trophy.progress = htmlDoc.getElementsByClass("table table-sm table-hover table-bordered").select("tbody tr td")[1].text()
            _tracker.trophy.highest=htmlDoc.getElementsByClass("table table-sm table-hover table-bordered").select("tbody tr td")[2].text()
            _tracker.trophy.seasonEnd=htmlDoc.getElementsByClass("table table-sm table-hover table-bordered").select("tbody tr td")[3].text()
//            _tracker.trophy.trophyImg=htmlDoc.getElementsByClass("_1q57V5vKLuzJ4g6BkfpZbk")[1].attr("style").substring(
//                htmlDoc.getElementsByClass("_1q57V5vKLuzJ4g6BkfpZbk")[1].attr("style").indexOf("(")+1,
//                htmlDoc.getElementsByClass("_1q57V5vKLuzJ4g6BkfpZbk")[1].attr("style").lastIndexOf(")")
//            ).replace("'","").replace("\"","")
//
//            _tracker.league.highestSoloImg=htmlDoc.getElementsByClass("DPUFH-EhiGBBrkki4Gsaf")[0].attr("src")
//            _tracker.league.highestTeamImg=htmlDoc.getElementsByClass("DPUFH-EhiGBBrkki4Gsaf")[1].attr("src")
//            _tracker.league.highestClubImg=htmlDoc.getElementsByClass("DPUFH-EhiGBBrkki4Gsaf")[2].attr("src")
//
            _tracker.trophy.seasonEndReward=htmlDoc.getElementsByClass("table table-sm table-hover table-bordered").select("tbody tr td")[4].text()
            _tracker.victories.team3v3=htmlDoc.getElementsByClass("table table-sm table-hover table-bordered").select("tbody tr td")[5].text()
            _tracker.victories.solo=htmlDoc.getElementsByClass("table table-sm table-hover table-bordered").select("tbody tr td")[6].text()
            _tracker.victories.duo=htmlDoc.getElementsByClass("table table-sm table-hover table-bordered").select("tbody tr td")[7].text()
            var gt = htmlDoc.getElementsByClass("list-group-item")[0].text()
            _tracker.gadgets = gt.substring(gt.indexOf("-")+1).trim()
            gt = htmlDoc.getElementsByClass("list-group-item")[1].text()
            _tracker.starpowers = gt.substring(gt.indexOf("-")).trim()
            gt = htmlDoc.getElementsByClass("list-group-item")[2].text()
            _tracker.gears = gt.substring(gt.indexOf("-")+1).trim()

            var loss=0;
            var win=0;
            var bg ="#19d800"
            for(i in htmlDoc.getElementsByClass("my-3 text-center").select("a")){
                if(i.className().contains("light-red")){
                    bg="#e13b1e"
                    loss++
                }
                else if(i.className().contains("light-green")){
                    bg="#19d800"
                    win++
                }
                else bg="#00a3ff"
                _tracker.battleLog.add(BattleResult(bg,i.select("img").attr("src")))
            }
            _tracker.bloss=loss
            _tracker.bwin=win
            var ox =0
            for (i in htmlDoc.getElementById("brawlersOwnedTable")?.select("tbody tr")!!){
                _tracker.brawler.add(BrawlerStats(
                    i.select("td")[0].text(),
                    "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + i.select("td")[0].text().lowercase() + "%2F" + i.select("td")[0].text()[0]+i.select("td")[0].text().substring(1).lowercase() + ".webp?alt=media&token=",
                    i.select("td")[2].text(),
                    i.select("td")[1].text(),
                    i.select("td")[3].text(),
                    i.select("td")[4].text(),
                    arrayListOf(),
                    arrayListOf()
                ))
                for(j in i.select(".icon-tiny") ){
                    val url = j.attr("src")
                    if(url.contains("gears")) _tracker.brawler[ox].gears.add(url)
                    else _tracker.brawler[ox].gdst.add(url)
                }

                ox++
            }
//            var ix =0
//            for(bg in htmlDoc.getElementsByClass("_13DuW1ZxcgxBsiEQ3xfgMm")){
//                var bgc=""
//                if(bg.attr("style").contains("#")) bgc=bg.attr("style").substring(bg.attr("style").indexOf('#')).replace(";","")
//                else bgc="#f3902d"
//
//                _tracker.brawler[ix].bg=bgc
//                ix++
//            }
//            _tracker.updated=htmlDoc.getElementsByClass("_3lMfMVxY-knKo2dnVHMCWG _21sSMvccqXG6cJU-5FNqzv _1WcgIMQLXx7DHn09FyXAwj").text()
//            var clubin=0
//            for( i in htmlDoc.getElementsByClass("_2F_JwG9U9Kz7NnFPeDmpL7")){
//                _tracker.prevClubs[clubin].clubLink=i.attr("href")
//                clubin++
//            }
//            var clubin1=0
//            for(i in htmlDoc.getElementsByClass("_3Ag9fZpyBrTpLIc_qjtvgo")){
//                _tracker.prevClubs[clubin1].clubBanner=i.attr("src")
//                clubin1++
//
//            }
//            var clubin2=0
//            for(i in htmlDoc.getElementsByClass("_3lMfMVxY-knKo2dnVHMCWG _21sSMvccqXG6cJU-5FNqzv _1O-vM3JfUX0P5_dwNNu6lm")){
//                _tracker.prevClubs[clubin2].clubNane=i.text()
//                clubin2++
//
//            }
            _tracker.main="https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + _tracker.brawler[0].name.lowercase() + "%2F" + _tracker.brawler[0].name[0] + _tracker.brawler[0].name.substring(1).lowercase() + "_Skin-Default.webp?alt=media&token"
            isLoadingStats.value=1

            tracker.value = _tracker
            FirebaseDatabase.getInstance().getReference("users/${tag.trim().uppercase()}").setValue(tracker.value.name)
        }

    }

    fun getCurrTime() {
        FirebaseDatabase.getInstance().getReference(".info").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val offset = snapshot.child("serverTimeOffset").getValue(Double::class.java) ?: 0.0
                val estimatedServerTimeMs = System.currentTimeMillis() + offset
                timeFromServer.value = estimatedServerTimeMs.toLong();
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        FirebaseDatabase.getInstance().getReference("clubleague").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                _cl.clear()


                for(child in snapshot.children){
                    _cl.add(child.value.toString())
                }
                cl.value=_cl
                val diff1 = (timeFromServer.value-cl.value[0].toLong())
                val diff = diff1/86400000
                if(diff1>=0){
                    if( diff<7){
                        Firebase.database.getReference("clubleague/endtimestamp").setValue(cl.value[0].toLong()+7*86400000)
                        Firebase.database.getReference("clubleague/eventname").setValue(if(cl.value[1]=="cl") "cg" else "cl")

                    }
                    else if(diff in 7..13){
                        Firebase.database.getReference("clubleague/endtimestamp").setValue(cl.value[0].toLong()+14*86400000)
                    }
                    else if((diff/7)%2 == 1L ){
                        Firebase.database.getReference("clubleague/endtimestamp").setValue(cl.value[0].toLong()+((7-diff%7)+diff)*86400000)
                    }

                    else{

                        Firebase.database.getReference("clubleague/endtimestamp").setValue(cl.value[0].toLong()+((7-diff%7)+diff)*86400000)
                        Firebase.database.getReference("clubleague/eventname").setValue(if(cl.value[1]=="cl") "cg" else "cl")

                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    
    fun getEvent() {
        FirebaseDatabase.getInstance().getReference("apiLock").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                apiLock.value=snapshot.value as Boolean
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        FirebaseDatabase.getInstance().getReference("metaVersion").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                metaVer.value=snapshot.value as String
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


        FirebaseDatabase.getInstance().getReference("brawlpass").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                _bp.clear()

                for(child in snapshot.children){
                    _bp.add(child.value.toString())
                }
                bp.value=_bp
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
       
        FirebaseDatabase.getInstance().getReference("powerleague").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                _pl.clear()
                for(child in snapshot.children){
                    _pl.add(child.value.toString())
                }
                pl.value=_pl
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun appUpdater() {
        FirebaseDatabase.getInstance().getReference("app").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                _changelog.clear()
                _info.link= snapshot.child("link").value.toString()
                _info.version= snapshot.child("version").value.toString();

                for(snap in snapshot.child("changelog").children){
                    _changelog.add(snap.value.toString())
                }
                changelog.value=_changelog
                isUpdateAvailable.value = _info.version.toFloat() > com.kal.brawlstatz2.BuildConfig.VERSION_NAME.toFloat()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }


    fun getData(){
        RetrofitInstance.apiInterface.getData().enqueue(object : Callback<event?> {
            override fun onResponse(call: Call<event?>, response: Response<event?>) {
                _activeList.clear()
                _upcomingList.clear()
                response.body()?.active?.filter { it.slot.name.contains("Daily") }?.let { _activeList.addAll(it) }
                response.body()?.active?.filter { it.slot.name.contains("Weekly") }?.let { _activeList.addAll(it) }
                _activeList.removeAll(_activeList.filter { it.map.hash.contains("-Duo") }.toSet())


                response.body()?.upcoming?.filter { it.slot.name.contains("Daily") }?.let { _upcomingList.addAll(it) }
                response.body()?.upcoming?.filter { it.slot.name.contains("Weekly") }?.let { _upcomingList.addAll(it) }
                _upcomingList.removeAll(_upcomingList.filter { it.map.hash.contains("-Duo") }.toSet())

                activeList.value=_activeList
                upcomingList.value=_upcomingList
            }

            override fun onFailure(call: Call<event?>, t: Throwable) {

            }
        })
    }
    fun find(txt:String){
        val sortedlist :ArrayList<Brawler> = ArrayList()
        if(txt=="traits"){
            for(brawler in list){
                if(brawler.trait!="null"){
                    sortedlist.add(brawler)
                }
            }
        }
        else if(txt=="3d"){
            for (brawler in list){
                if(brawler.model3d=="true"){
                    sortedlist.add(brawler)
                }
            }
        }
        else{
            for(brawler in list){
                if(brawler.bname?.lowercase()?.startsWith(txt) == true||brawler.brare?.lowercase()?.startsWith(txt) == true){
                    sortedlist.add(brawler)
                }
            }
        }

        blist.value=sortedlist
    }
     fun metaSorting(){
        val mlist :ArrayList<MetaTier> = ArrayList()
         sortedMetaList.clear()
        sortedMetaList.addAll(list.sortedBy { it.tier })
        val sTier: ArrayList<Brawler> = ArrayList()
        val aTier: ArrayList<Brawler> = ArrayList()
        val bTier: ArrayList<Brawler> = ArrayList()
        val cTier: ArrayList<Brawler> = ArrayList()
        val dTier: ArrayList<Brawler> = ArrayList()
        val fTier: ArrayList<Brawler> = ArrayList()
         val nTier: ArrayList<Brawler> = ArrayList()
        for(brawler in sortedMetaList){
            when(brawler.tier?.get(0)){
                in '0'..'9' -> sTier.add(brawler)
                'A' -> aTier.add(brawler)
                'B' -> bTier.add(brawler)
                'C' -> cTier.add(brawler)
                'D' -> dTier.add(brawler)
                'F' -> fTier.add(brawler)
                'N' -> nTier.add(brawler)
            }
        }
         if(nTier.isNotEmpty())mlist.add(MetaTier("NEW",nTier,Color.Cyan))
        mlist.add(MetaTier("S",sTier, Color(0xFFff7e7e)))
        mlist.add(MetaTier("A",aTier, Color(0xFFffbf7f)))
        mlist.add(MetaTier("B",bTier, Color(0xFFffde7f)))
        mlist.add(MetaTier("C",cTier, Color(0xFFfeff7f)))
        mlist.add(MetaTier("D",dTier, Color(0xFFbeff7d)))
        mlist.add(MetaTier("F",fTier, Color(0xFF7eff80)))

        nestedList.value=mlist
    }

    fun metaFind(txt: Char){
        val find :ArrayList<MetaTier> = ArrayList()
        val findTier: ArrayList<Brawler> = ArrayList()
        if(txt=='S') {
            for(brawler in sortedMetaList){
                when(brawler.tier?.get(0)){
                    in '0'..'9' -> findTier.add(brawler)
                }
            }
        }
        else{
            for(brawler in sortedMetaList){
                if(brawler.tier?.get(0)==txt){
                    findTier.add(brawler)
                }
            }
        }
        find.add(MetaTier(txt.toString(),findTier, when(txt){
            'S'-> Color(0xFFff7e7e)
            'A'-> Color(0xFFffbf7f)
            'B'-> Color(0xFFffde7f)
            'C'-> Color(0xFFfeff7f)
            'D'-> Color(0xFFbeff7d)
            'F'-> Color(0xFF7eff80)
            else-> Color.Black
        }))
        nestedList.value=find
    }
    private fun fetchData(){

        FirebaseDatabase.getInstance().getReference("traits").addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                traits.clear()
                for(snap in snapshot.children ){
                    val tempTrait = Trait(snap.key.toString(),snap.value.toString())
                    traits.add(tempTrait)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
        FirebaseDatabase.getInstance().getReference("brawlers")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    list.clear()
                    for(DataSnap in snapshot.children){
                        val brawler = DataSnap.getValue(Brawler::class.java)
                        if (brawler != null) {
                            brawler.classType = DataSnap.child("class").value as String
                            brawler.bpro =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname!!.lowercase() + "%2F" + brawler.bname[0]+brawler.bname.substring(1).lowercase() + ".webp?alt=media&token="+brawler.zver
                            brawler.bmodel =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2F" + brawler.bname[0]+brawler.bname.substring(1).lowercase() + "_Skin-Default.webp?alt=media&token="+brawler.zver
                            brawler.g1 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2FGD-" + brawler.bname[0]+brawler.bname.substring(1).lowercase() + "1.webp?alt=media&token="+brawler.zver
                            brawler.g2 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2FGD-" + brawler.bname[0]+brawler.bname.substring(1).lowercase() + "2.webp?alt=media&token="+brawler.zver
                            brawler.s1 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2FSP-" + brawler.bname[0]+brawler.bname.substring(1).lowercase() + "1.webp?alt=media&token="+brawler.zver
                            brawler.s2 =
                                "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2FSP-" + brawler.bname[0]+brawler.bname.substring(1).lowercase() + "2.webp?alt=media&token="+brawler.zver
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
                            brawler.model ="https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/brawlers%2F" + brawler.bname.lowercase() + "%2F" +  brawler.bname[0]+brawler.bname.substring(1).lowercase()  + "Model.glb?alt=media&token="+brawler.zver
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