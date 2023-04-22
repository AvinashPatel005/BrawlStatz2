package com.kal.brawlstatz2

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.kal.brawlstatz2.data.BottomNavItem
import com.kal.brawlstatz2.data.Events
import com.kal.brawlstatz2.data.events.Active
import com.kal.brawlstatz2.presentation.ShimmerListItem
import com.kal.brawlstatz2.presentation.BrawlersList
import com.kal.brawlstatz2.presentation.MapCard
import com.kal.brawlstatz2.presentation.ShowMetaList
import com.kal.brawlstatz2.ui.theme.*
import com.kal.brawlstatz2.viewmodel.MainViewModel
import kotlinx.coroutines.delay

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
                var selectedSort by remember {
                    mutableStateOf(0)
                }
                var selectedSortMeta by remember {
                    mutableStateOf(0)
                }

                    val focusManager = LocalFocusManager.current
                    val navController = rememberAnimatedNavController()
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
                                 Column{

                                     TopAppBar(
                                         title = {
                                             if(!isSearch){
                                                 Text(
                                                     "BrawlStatz2",
                                                     maxLines = 1,
                                                     overflow = TextOverflow.Ellipsis,
                                                     color = Color.White,
                                                     fontWeight = FontWeight.Bold,
                                                     fontFamily = FontFamily.Serif
                                                 )
                                             }
                                         },
                                         colors = TopAppBarDefaults.topAppBarColors(Color(0xFF000000)),
                                         navigationIcon = {
                                             Icon(
                                                 painter = painterResource(id = R.drawable.logo_menu),
                                                 contentDescription = null,
                                                 modifier = Modifier.padding(start = 10.dp, end = 4.dp),
                                                 tint = Color.White
                                             )
                                         },

                                         actions = {

                                                 Row(
                                                     verticalAlignment = Alignment.CenterVertically
                                                 ){

                                                     AnimatedVisibility(visible = (isSearch && tabCurrent=="brawler") , enter = fadeIn(),
                                                        exit = fadeOut()) {

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
                                                     if(tabCurrent=="brawler") {
                                                         IconButton(onClick = {
                                                             if (value == "") {
                                                                 isSearch = !isSearch
                                                                 viewModel.isSearching.value = false
                                                             }
                                                             else {
                                                                 value = ""
                                                                 viewModel.find(value.lowercase())
                                                             }

                                                         }) {
                                                             Icon(
                                                                 if (isSearch) Icons.Default.Close
                                                                 else Icons.Default.Search,
                                                                 contentDescription = "search"
                                                             )

                                                         }
                                                     }
                                                 }
                                             }
                                         }
                                     )

                                     if(tabCurrent=="brawler"){
                                         LazyRow(
                                             modifier = Modifier
                                                 .background(Color(0xFF000000))
                                                 .height(40.dp)
                                         ) {
                                             val list  = listOf("All","Traits","Chromatic","Legendary","Mythic","Epic","Super Rare","Rare","Starting")
                                             items(list){s ->
                                                 Spacer(modifier = Modifier.width(10.dp))
                                                 Button(
                                                     modifier =if(s=="Traits") {
                                                         Modifier
                                                             .border(
                                                                 width = 2.dp,
                                                                 color = Color.Gray,
                                                                 shape = RoundedCornerShape(8.dp)
                                                             )
                                                             .height(35.dp)
                                                                    }
                                                     else {
                                                         Modifier.height(35.dp)
                                                          },
                                                     onClick = {
                                                         if(selectedSort==list.indexOf(s)&&selectedSort!=0){
                                                             selectedSort=0
                                                             viewModel.find("")
                                                         }
                                                         else{
                                                             selectedSort = list.indexOf(s)
                                                             if(s=="All") viewModel.find("")
                                                             else  viewModel.find(s.lowercase())
                                                         }
                                                     },
                                                     colors = if(selectedSort==list.indexOf(s)) ButtonDefaults.buttonColors(Color(0xffeeeee4)) else ButtonDefaults.buttonColors(Color(0xFF202124)),
                                                     shape = RoundedCornerShape(8.dp),
                                                     contentPadding = PaddingValues(8.dp)
                                                 ) {
                                                     Text(text = s, color = if(selectedSort!=list.indexOf(s)) Color(0xffeeeee4) else Color(0xFF202124))
                                                 }
                                             }
                                             item{
                                                 Spacer(modifier = Modifier.width(10.dp))
                                             }
                                         }
                                     }
                                     else if(tabCurrent=="meta"){
                                         LazyRow(
                                             modifier = Modifier
                                                 .background(Color(0xFF000000))
                                                 .height(40.dp)
                                         ) {
                                             val list  = listOf("All","S","A","B","C","D","F")
                                             items(list){s ->
                                                 Spacer(modifier = Modifier.width(10.dp))
                                                 Button(
                                                     modifier = Modifier.height(35.dp),
                                                     onClick = {
                                                         if(selectedSortMeta==list.indexOf(s)&&selectedSortMeta!=0){
                                                             selectedSortMeta=0
                                                             viewModel.metaSorting()
                                                         }
                                                         else{
                                                             selectedSortMeta=list.indexOf(s)
                                                             if(s!="All") {
                                                                 viewModel.metaFind(s[0])
                                                             }
                                                             else viewModel.metaSorting()
                                                         }
                                                     },
                                                     colors = if(selectedSortMeta==list.indexOf(s)) ButtonDefaults.buttonColors(Color(0xffeeeee4)) else ButtonDefaults.buttonColors(Color(0xFF202124)),
                                                     shape = RoundedCornerShape(8.dp),
                                                     contentPadding = PaddingValues(8.dp)
                                                 ) {
                                                     Text(text = buildAnnotatedString
                                                     {
                                                         append(s)
                                                         if(s!="All"){
                                                             withStyle(style = SpanStyle(
                                                                 fontSize = 7.sp,
                                                                 color = Color.Gray
                                                             )){
                                                                 append("TIER")
                                                             }
                                                         }
                                                     }, color = if(selectedSortMeta!=list.indexOf(s)) Color(0xffeeeee4) else Color(0xFF202124))
                                                 }
                                             }
                                             item{
                                                 Spacer(modifier = Modifier.width(10.dp))
                                             }
                                         }
                                     }
                                 }
                        },
                        bottomBar = {
                            BottomNavBar(
                                items = listOf(
                                    BottomNavItem("BRAWLER","brawler", Icons.Default.Home),
                                    BottomNavItem("MAP","map", Icons.Default.Notifications),
                                    BottomNavItem("META","meta", Icons.Default.Settings)
                                ),
                                navController = navController,
                                onItemClicked = {
                                    tabCurrent=it.route
                                    selectedSort=0
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
                                .padding(
                                    top = it.calculateTopPadding(),
                                    bottom = it.calculateBottomPadding()
                                )
                                .background(Color(0xFF000000))
                        ){
                            Navigation(navController = navController)
                            Divider()
                        }
                    }
                }
            }

    }
    @OptIn(ExperimentalAnimationApi::class)
    @Composable
    fun Navigation(navController: NavHostController) {
        val viewModel = viewModel<MainViewModel>()
        val brawlers = viewModel.blist.value
        val meta = viewModel.nestedList.value
        val isLoading = viewModel.isLoading.value
        val isSearching = viewModel.isSearching.value
        AnimatedNavHost(navController = navController, startDestination = "brawler" ){
            composable("brawler",
                enterTransition = {
                        slideInHorizontally(
                            initialOffsetX = { -800 },
                            animationSpec = tween(300)
                        ) + fadeIn(animationSpec = tween(300))
                }
                ){
                if(isLoading){
                    LazyColumn(modifier = Modifier.fillMaxSize()){
                        items(20){
                            ShimmerListItem(
                                modifier = Modifier
                                    .background(Color.Black)
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            )
                        }
                    }
                }
                BrawlersList(brawler = brawlers,isSearching,viewModel)
            }
            composable("map",
                enterTransition = {
                    if(navController.previousBackStackEntry?.destination?.route=="meta"){
                        slideInHorizontally(
                            initialOffsetX = { -800 },
                            animationSpec = tween(300)
                        ) + fadeIn(animationSpec = tween(300))
                    }
                    else {
                        slideInHorizontally(
                            initialOffsetX = { 800 },
                            animationSpec = tween(300)
                        ) + fadeIn(animationSpec = tween(300))
                    }
                }
                ){
                val navController1 = rememberAnimatedNavController()
                AnimatedNavHost(navController = navController1, startDestination = "menu") {
                    composable("menu") { SetDataMap(){route->
                        navController1.navigate(route = route)
                    } }
                    composable("curr") {Curr(viewModel.activeList.value)}
                    composable("up") { Curr(viewModel.upcomingList.value) }
                }



            }
            composable("meta",
                enterTransition = {
                    slideInHorizontally(
                        initialOffsetX = { 800 },
                        animationSpec = tween(300)
                    ) + fadeIn(animationSpec = tween(300))
                }
            ){
                ShowMetaList(meta,viewModel.sortedMetaList)
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
        modifier = Modifier.height(45.dp),
        tonalElevation = 0.dp,
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
@Composable
fun SetDataMap(
    onclick : (String) -> Unit
) {
    val eventCardList = listOf<Events>(
        Events("CURRENT","curr", Color.Green,true),
        Events("UPCOMING","up", Color.Blue,true),
        Events("POWER LEAGUE","pow", Color.Cyan,false),
        Events("SEASON","season", Color.Red,false),
        Events("CLUB","club", Color.Yellow,false),
    )
    LazyVerticalGrid(columns = GridCells.Fixed(2),Modifier.padding(top = 2.dp, start = 4.dp,end=4.dp)){
        items(eventCardList){
            Card(
                Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .height(250.dp)
                    .clickable {
                        if (it.enabled) onclick(it.route)
                    },
                colors = CardDefaults.cardColors(it.color)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center){
                    Text(text = it.cardName)
                }
            }
        }
    }
}
@Composable
fun Curr(value: List<Active>) {
    LazyColumn(
        Modifier.fillMaxSize()
    ){
        items(value){
            MapCard(active = it)
        }
    }
}