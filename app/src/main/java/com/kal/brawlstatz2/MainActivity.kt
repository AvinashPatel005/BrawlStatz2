package com.kal.brawlstatz2

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context.LOCATION_SERVICE
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Bottom
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kal.brawlstatz2.data.BottomNavItem
import com.kal.brawlstatz2.data.Events
import com.kal.brawlstatz2.data.clubleague
import com.kal.brawlstatz2.presentation.BrawlersList
import com.kal.brawlstatz2.presentation.Curr
import com.kal.brawlstatz2.presentation.ShimmerListItem
import com.kal.brawlstatz2.presentation.ShowMetaList
import com.kal.brawlstatz2.ui.theme.*
import com.kal.brawlstatz2.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrawlStatz2Theme {
                val viewModel = viewModel<MainViewModel>()
                    var isSearch by remember {
                        mutableStateOf(false)
                    }
                    var isVisible = remember {
                        false
                    }
                    val focusRequester = remember{
                        FocusRequester()
                    }
                    var value by remember {
                        mutableStateOf("")
                    }
                    var tabCurrent by remember{
                        mutableStateOf("brawler")
                    }

                var selectedSortMeta by remember {
                    mutableStateOf(0)
                }

                    val focusManager = LocalFocusManager.current
                    val navController = rememberNavController()
                navController.enableOnBackPressed(enabled = false)
                val activity = (LocalContext.current as? Activity)
                BackHandler {
                    if(navController.currentDestination?.route  == "brawler") activity?.finish()
                    else {
                        navController.navigate("brawler")
                        tabCurrent="brawler"
                        isSearch=false
                    }
                }
                    LaunchedEffect(key1 = isVisible, key2 = isSearch){
                        if(isVisible&&isSearch){
                            delay(50)
                            focusManager.clearFocus()

                            focusRequester.requestFocus()
                        }
                        if(!isSearch){
                            focusManager.clearFocus()
                        }
                    }
                    Scaffold(
                        topBar = {
                                 Column {

                                     TopAppBar(
                                         title = {
                                             if (!isSearch) {
                                                 Text(
                                                     "BrawlStatz2",
                                                     maxLines = 1,
                                                     overflow = TextOverflow.Ellipsis,
                                                     color = Color(0xFFd2d4d2),
                                                     fontWeight = FontWeight.Bold,
                                                 )
                                             }
                                         },
                                         colors = TopAppBarDefaults.topAppBarColors(Color(0xFF000000)),
                                         navigationIcon = {
                                             Icon(
                                                 painter = painterResource(id = R.drawable.logo_menu),
                                                 contentDescription = null,
                                                 modifier = Modifier.padding(
                                                     start = 10.dp,
                                                     end = 4.dp
                                                 ),
                                                 tint = Color.White
                                             )
                                         },

                                         actions = {

                                             Row(
                                                 verticalAlignment = Alignment.CenterVertically
                                             ) {
                                                 if (tabCurrent == "events") {
                                                     Text(
                                                         text = "EVENTS    ",
                                                         color = Color.Gray,
                                                         fontSize = 9.sp
                                                     )
                                                 } else if (tabCurrent == "meta") {
                                                     Text(
                                                         text = "V${viewModel.metaVer.value} ",
                                                         color = Color.Gray,
                                                         fontSize = 9.sp
                                                     )
                                                 }
                                                 AnimatedVisibility(
                                                     visible = (isSearch && tabCurrent == "brawler"),
                                                     enter = fadeIn(),
                                                     exit = fadeOut()
                                                 ) {

                                                     isVisible = !isVisible
                                                     OutlinedTextField(
                                                         value = value,
                                                         onValueChange = {
                                                             viewModel.isSearching.value = true
                                                             value = it
                                                             viewModel.find(value.lowercase())
                                                         },
                                                         modifier = Modifier
                                                             .focusRequester(focusRequester)
                                                             .scale(scaleY = 0.9F, scaleX = 1F),

                                                         placeholder = { Text(text = "Search") },
                                                         shape = RoundedCornerShape(100),
                                                         colors = OutlinedTextFieldDefaults.colors(
                                                             focusedContainerColor = PurpleGrey40,
                                                             unfocusedContainerColor = PurpleGrey40,
                                                             disabledContainerColor = PurpleGrey40,
                                                             focusedBorderColor = Color.Black,
                                                             unfocusedBorderColor = Color.Black,
                                                             cursorColor = PurpleGrey80
                                                         )
                                                     )
                                                 }
                                                 Card(
                                                     modifier = Modifier.clip(RoundedCornerShape(100))
                                                 ) {
                                                     if (tabCurrent == "brawler") {
                                                         IconButton(onClick = {
                                                             if (value == "") {
                                                                 isSearch = !isSearch
                                                                 viewModel.isSearching.value = false
                                                             } else {
                                                                 value = ""
                                                                 viewModel.find(value.lowercase())
                                                             }

                                                         }) {
                                                             Icon(
                                                                 if (isSearch) Icons.Default.Close
                                                                 else Icons.Default.Search,
                                                                 contentDescription = "search",
                                                             )
                                                         }
                                                     }
                                                 }
                                             }
                                         }
                                     )
                                 }
                        },
                        bottomBar = {
                            BottomNavBar(
                                items = listOf(
                                    BottomNavItem("BRAWLER","brawler", Icons.Default.Home),
                                    BottomNavItem("EVENTS","events", Icons.Default.Notifications),
                                    BottomNavItem("META","meta", Icons.Default.Settings)
                                ),
                                navController = navController,
                                onItemClicked = {
                                    tabCurrent=it.route
                                    //selectedSort=0
                                    selectedSortMeta=0
                                    isSearch=false
                                    value = ""
                                    viewModel.isSearching.value=false
                                    viewModel.find(value.lowercase())
                                    navController.navigate(it.route)
                                }
                            )
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = it.calculateTopPadding(),
                                    bottom = it.calculateBottomPadding()
                                )
                                .background(Color(0xFF000000))
                        )
                        {
                                Navigation(navController = navController)
                               

                            if(viewModel.isUpdateAvailable.value){
                                Update(viewModel)
                            }
                        }
                    }
                }
            }

    }
    @Composable
    fun Navigation(navController: NavHostController) {
        val viewModel = viewModel<MainViewModel>()
        val brawlers = viewModel.blist.value
        val meta = viewModel.nestedList.value
        val isLoading = viewModel.isLoading.value
        val isSearching = viewModel.isSearching.value
        NavHost(navController = navController, startDestination = "brawler" ){
            composable("brawler"){
                if(isLoading){
                   Column() {
                       Spacer(modifier = Modifier.height(40.dp))
                       LazyColumn(modifier = Modifier.fillMaxSize()){
                           items(10){
                               ShimmerListItem(
                                   modifier = Modifier
                                       .background(Color.Black)
                                       .fillMaxWidth()
                                       .padding(16.dp)
                               )
                           }
                       }
                   }
                }
                BrawlersList(brawler = brawlers,isSearching,viewModel)
            }
            composable("events"){
                val navController1 = rememberNavController()
                NavHost(navController = navController1, startDestination = "menu") {
                    composable("menu") { SetDataMap(viewModel){route->
                        navController1.navigate(route = route)
                    } }
                    composable("curr") {Curr(viewModel.activeList.value,viewModel,0) }
                    composable("up") { Curr(viewModel.upcomingList.value,viewModel,1) }
                }



            }
            composable("meta"){
                ShowMetaList(meta,viewModel.sortedMetaList)
            }
        }
    }
}

@Composable
fun Update(viewModel: MainViewModel) {
    val uriHandler = LocalUriHandler.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center){
        Box(modifier = Modifier
            .background(Color.Black.copy(alpha = 0.5f))
            .fillMaxSize())
        Card (
            modifier = Modifier
                .border(width = 2.dp, Color.Gray, shape = RoundedCornerShape(20.dp))
                .fillMaxWidth(0.8f),
            shape = RoundedCornerShape(20.dp),

                ){
            Box(modifier = Modifier.padding(10.dp)){

                Column {
                    Text(text = "Update Available! v${viewModel._info.version}", fontSize = 18.sp, color = chromatic)
                    Text(text = "Changelog:", fontSize = 16.sp,color = rare)
                    LazyColumn{
                        items(viewModel.changelog.value.size){
                            Text(text = "${it+1}. ${viewModel.changelog.value[it]}", fontSize = 14.sp)
                        }
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 40.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(text = "Later", color = epic, modifier = Modifier.clickable{
                            viewModel.isUpdateAvailable.value=false
                        })
                        Text(text = "Update", color = epic, modifier = Modifier.clickable{
                            uriHandler.openUri(viewModel._info.link)
                        })
                    }
                }
            }
        }
    }
}
@Composable
fun BottomNavBar(
    items:List<BottomNavItem>,
    navController: NavController,
    onItemClicked : (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(
        modifier = Modifier.height(50.dp),
        tonalElevation = 1000.dp,
    ){
       Column {
           Divider()
           Row{
               items.forEach{
                   val selected = it.route == backStackEntry.value?.destination?.route
                   NavigationBarItem(
                       selected = selected,
                       onClick = { if(!selected) onItemClicked(it) },
                       icon = {
                           Column(
                               horizontalAlignment = CenterHorizontally
                           ) {
                               Icon(imageVector = it.icon, contentDescription = it.name, modifier = Modifier.size(20.dp))
                               if(selected) Text(text = it.name, textAlign = TextAlign.Center, fontSize = 8.sp)
                           }
                       }
                   )
               }
           }
       }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SetDataMap(
    viewModel: MainViewModel,
    onclick: (String) -> Unit
) {
    val eventCardList = listOf<Events>(
        Events("CURRENT","curr", Color.Green,R.drawable.c1,true),
        Events("UPCOMING","up", Color.Blue,R.drawable.c2,true)
    )
    Column(Modifier.padding(vertical = 6.dp)) {
        Row(Modifier.weight(3.1f)){
            Spacer(modifier = Modifier.width(3.dp))
            for(it in eventCardList){
                Spacer(modifier = Modifier.width(3.dp))
                Card(
                    Modifier
                        .weight(1f)
                        .clickable {
                            if (it.enabled) onclick(it.route)
                        }
                        ,
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                        Image(
                            painter = painterResource(id = it.drawable),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(10.dp)) {
                            Text(
                                text = it.cardName,
                                style = MaterialTheme.typography.bodyMedium + TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                    textIndent = TextIndent(0.sp),
                                    fontSize = 20.sp
                                ),
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(3.dp))
            }
            Spacer(modifier = Modifier.width(3.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row (Modifier.weight(3.1f)){
            Spacer(modifier = Modifier.width(6.dp))
            Card(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable {
                    },

                ) {
                Box(modifier = Modifier
                    .fillMaxSize(), contentAlignment = TopEnd){

                    val timeStamp: String = java.lang.String.valueOf(
                        TimeUnit.MILLISECONDS.toSeconds(
                            System.currentTimeMillis()
                        )
                    )
                    val currtime : Long = timeStamp.toLong()*1000
                    val difTime = viewModel.bp.value[0].toLong().minus(currtime)
                    val dayLeft = difTime/86400000;
                    val hourLeft = (difTime%86400000)/3600000
                    val minLeft = ((difTime%86400000)%3600000)/60000
                    GlideImage(model = "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/bpb.webp?alt=media&token=${viewModel.bp.value[2]}", contentDescription = null,
                        modifier = Modifier.fillMaxHeight()
                    ){
                        it.centerCrop()
                    }
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(10.dp)){
                        Text(text = "BRAWL PASS", style=MaterialTheme.typography.bodyMedium + TextStyle(
                            shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                            textIndent = TextIndent(0.sp),
                            fontSize = 20.sp
                        ),modifier = Modifier.align(
                            TopStart))
                        Column(modifier = Modifier.align(Center)) {
                            Text(text = viewModel.bp.value[1], fontSize = 22.sp, textAlign = TextAlign.End, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
                            Text(text = "Season ${viewModel.bp.value[2]}", modifier = Modifier.align(
                                Alignment.End))
                        }
                        Image(painter = painterResource(id = R.drawable.bp), contentDescription = null, modifier = Modifier
                            .align(
                                BottomStart
                            )
                            .size(80.dp, 42.dp)
                            .rotate(-5f))

                        Text(
                            text = if(difTime>0) {"${dayLeft}d ${hourLeft}h ${minLeft}m"} else "ENDED",
                            style = MaterialTheme.typography.bodyMedium + TextStyle(
                                shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                textIndent = TextIndent(0.sp),
                                color = if(dayLeft==0L) Color.Red else Color.White
                            ),
                            modifier = Modifier
                                .background(Color.Gray.copy(alpha = 0.3f))
                                .align(
                                    BottomEnd
                                )
                        )
                    }


                }
            }
            Spacer(modifier = Modifier.width(6.dp))
            Card(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable {
                    },

                ) {
                Box(modifier = Modifier
                    .fillMaxSize(), contentAlignment = TopEnd){

                    val timeStamp: String = java.lang.String.valueOf(
                        TimeUnit.MILLISECONDS.toSeconds(
                            System.currentTimeMillis()
                        )
                    )
                    val currtime : Long = timeStamp.toLong()*1000
                    val difTime = viewModel.pl.value[0].toLong().minus(currtime)
                    val dayLeft = difTime/86400000;
                    val hourLeft = (difTime%86400000)/3600000
                    val minLeft = ((difTime%86400000)%3600000)/60000
                    GlideImage(model = "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/bpb1.webp?alt=media&token=${viewModel.pl.value[1]}", contentDescription = null,
                        modifier = Modifier.fillMaxHeight()
                    ){
                        it.centerCrop()
                    }
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .padding(10.dp)){
                        Text(text = "POWER LEAGUE", style=MaterialTheme.typography.bodyMedium + TextStyle(
                            shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                            textIndent = TextIndent(0.sp),
                            fontSize = 20.sp
                        ),modifier = Modifier.align(
                            TopStart))
                        Text(text = "Season ${viewModel.pl.value[1]}", fontWeight = FontWeight.Bold, modifier = Modifier.align(
                            CenterEnd))
                        Image(painter = painterResource(id = R.drawable.pl), contentDescription = null, modifier = Modifier
                            .align(
                                BottomStart
                            )
                            .size(60.dp)
                            .rotate(-5f))
                        Text(
                            text = if(difTime>0) {"${dayLeft}d ${hourLeft}h ${minLeft}m"} else "ENDED",
                            style = MaterialTheme.typography.bodyMedium + TextStyle(
                                shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                textIndent = TextIndent(0.sp),
                                color = if(dayLeft==0L) Color.Red else Color.White
                            ),
                            modifier = Modifier
                                .background(Color.Gray.copy(alpha = 0.3f))
                                .align(
                                    BottomEnd
                                )
                        )
                    }


                }
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        val iscg = viewModel.cl.value[1] == "cg"
        if(iscg){
            val difTime = viewModel.cl.value[0].toLong().minus(viewModel.timeFromServer.value)
            val dayLeft = difTime/86400000;
            val hourLeft = (difTime%86400000)/3600000
            val minLeft = ((difTime%86400000)%3600000)/60000
            Card(
                Modifier
                    .weight(2.2f)
                    .padding(horizontal = 4.dp)
                    .clickable {},

                ){
                Box(){
                    Image(painter = painterResource(id = R.drawable.clb), contentDescription = null , modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop )
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(10.dp)) {
                            Text(text =  "CLUB GAMES",style=MaterialTheme.typography.bodyMedium + TextStyle(
                                shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                textIndent = TextIndent(0.sp),
                                fontSize = 20.sp
                            ), modifier = Modifier.align(TopStart))
                            Text(text = "${dayLeft}d ${hourLeft}h ${minLeft}m", modifier = Modifier
                                .align(
                                    BottomEnd
                                )
                                .background(Color.Gray.copy(alpha = 0.3f))
                                .align(
                                    BottomEnd
                                ),
                                style = MaterialTheme.typography.bodyMedium + TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                    textIndent = TextIndent(0.sp),
                                    color = if(dayLeft==0L) Color.Red else Color.White
                                ))

                        Row(modifier = Modifier
                            .align(
                                BottomStart
                            )){
                            Image(painter = painterResource(id = R.drawable.cg), contentDescription = null, modifier = Modifier
                                .size(68.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.align(Bottom)) {
                                Text("QUEST WEEK",
                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                        textIndent = TextIndent(0.sp),
                                        fontSize = 18.sp
                                    ), modifier = Modifier.offset(y=(4).dp))
                                Text("Complete quests with your club!",
                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                        textIndent = TextIndent(0.sp),
                                        fontSize = 12.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                            }
                        }
                        Column(horizontalAlignment = Alignment.End, modifier = Modifier.align(TopEnd)) {
                            Text(text = " Club League in",
                                style = MaterialTheme.typography.bodyMedium + TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                    textIndent = TextIndent(0.sp),
                                    fontSize = 12.sp
                                ))
                            Text(text = "${dayLeft}d ${hourLeft}h",
                                style = MaterialTheme.typography.bodyMedium + TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                    textIndent = TextIndent(0.sp),
                                    fontSize = 12.sp,
                                    color = if(dayLeft==0L) Color.Red else Color.White
                                ), modifier = Modifier.offset(y=(-4).dp))
                        }
                    }
                }
            }
        }
        else{
            val difTime = viewModel.cl.value[0].toLong().minus(viewModel.timeFromServer.value)
            val dayLeft = difTime/86400000;
            val hourLeft = (difTime%86400000)/3600000
            val minLeft = ((difTime%86400000)%3600000)/60000

            val clEvent = listOf(
                clubleague(1,"EVENT DAY 1",0,"Preparation !"),
                clubleague(2,"EVENT DAY 1",4,"Compete with your club!"),
                clubleague(3,"EVENT DAY 2",0,"Preparation !"),
                clubleague(4,"EVENT DAY 2",4,"Compete with your club!"),
                clubleague(5,"EVENT DAY 3",0,"Preparation !"),
                clubleague(6,"EVENT DAY 3",6,"Compete with your club!"),
                clubleague(7,"EVENT ENDED",0,"Club Games will start soon!")
            )
            val day = 7-dayLeft-1
            Card(
                Modifier
                    .weight(2.2f)
                    .padding(horizontal = 6.dp)
                    .clickable {},
                ){
                Box(){
                    Image(painter = painterResource(id = R.drawable.clb), contentDescription = null , modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop )
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.7f))
                        .padding(10.dp)) {
                        Text(text =  "CLUB LEAGUE",style=MaterialTheme.typography.bodyMedium + TextStyle(
                            shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                            textIndent = TextIndent(0.sp),
                            fontSize = 20.sp
                        ), modifier = Modifier.align(TopStart))
                        Text(text = if(day == 6L) "ENDED" else "${hourLeft}h ${minLeft}m", modifier = Modifier
                            .align(
                                BottomEnd
                            )
                            .background(Color.Gray.copy(alpha = 0.3f))
                            .align(
                                BottomEnd
                            ),
                            style = MaterialTheme.typography.bodyMedium + TextStyle(
                                shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                textIndent = TextIndent(0.sp),
                                color = if(dayLeft==0L) Color.Red else Color.White
                            ))

                        Row(modifier = Modifier
                            .align(
                                BottomStart
                            )){
                            Image(painter = painterResource(id = R.drawable.pm), contentDescription = null, modifier = Modifier
                                .size(68.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.align(Bottom)) {
                                Text(clEvent[day.toInt()].name,
                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                        textIndent = TextIndent(0.sp),
                                        fontSize = 18.sp
                                    ), modifier = Modifier.offset(y=(4).dp))
                                Text(clEvent[day.toInt()].text,
                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                        textIndent = TextIndent(0.sp),
                                        fontSize = 12.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                            }
                        }
                        if(clEvent[day.toInt()].ticket!=0){
                            Row (verticalAlignment = Alignment.CenterVertically,modifier = Modifier.align(CenterEnd)){
                                Image(painter = painterResource(id = R.drawable.ct), contentDescription = null, modifier = Modifier
                                    .size(30.dp))
                                Text(text = " x${clEvent[day.toInt()].ticket}",
                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                        textIndent = TextIndent(0.sp),
                                        fontSize = 20.sp
                                    ))
                            }
                        }
                        Column(horizontalAlignment = Alignment.End, modifier = Modifier.align(TopEnd)) {
                            Text(text = " Club Games in",
                                style = MaterialTheme.typography.bodyMedium + TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                    textIndent = TextIndent(0.sp),
                                    fontSize = 12.sp
                                ))
                            Text(text = "${dayLeft}d ${hourLeft}h",
                                style = MaterialTheme.typography.bodyMedium + TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                    textIndent = TextIndent(0.sp),
                                    fontSize = 12.sp,
                                    color = if(dayLeft==0L) Color.Red else Color.White
                                ), modifier = Modifier.offset(y=(-4).dp))
                        }
                    }
                }
            }
        }
    }
}
