package com.kal.brawlstatz2

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
import com.kal.brawlstatz2.presentation.ShimmerListItem
import com.kal.brawlstatz2.presentation.BrawlersList
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
                    var isSearchVisible by remember{
                        mutableStateOf(true)
                    }
                var showMenu by remember { mutableStateOf(false) }
                    val focusManager = LocalFocusManager.current
                    val navController = rememberAnimatedNavController()
                navController.enableOnBackPressed(enabled = false)
                val activity = (LocalContext.current as? Activity)
                BackHandler() {
                    if(navController.currentDestination?.route  == "brawler") activity?.finish()
                    else {
                        navController.navigate("brawler")
                        isSearchVisible=true
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
                                 Column() {

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

                                                     AnimatedVisibility(visible = (isSearch && isSearchVisible) , enter = fadeIn(),
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
                                                     if(isSearchVisible) {
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
                                     BottomNavBar(
                                         items = listOf(
                                             BottomNavItem("BRAWLER","brawler", Icons.Default.Home),
                                             BottomNavItem("MAP","map", Icons.Default.Notifications),
                                             BottomNavItem("META","meta", Icons.Default.Settings)

                                         ),
                                         navController = navController,
                                         onItemClicked = {
                                             isSearchVisible=it.route=="brawler"
                                             isSearch=false
                                             value = ""
                                             viewModel.isSearching.value=false
                                             viewModel.find(value.lowercase())
                                             navController.navigate(it.route)
                                         }
                                     )
                                 }
                        },
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
                BrawlersList(brawler = brawlers,isSearching)

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
                SetDataMap()
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
    modifier: Modifier = Modifier,
    onItemClicked : (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(
        modifier = modifier.height(50.dp),
        tonalElevation = 0.dp,
        containerColor = Color(0xFF000000),
    ){
       items.forEach{
           val selected = it.route == backStackEntry.value?.destination?.route
           NavigationBarItem(
               selected = selected,
               modifier = Modifier.height(45.dp),
               onClick = { if(!selected) onItemClicked(it) },
               icon = {
                   Column(
                       horizontalAlignment = CenterHorizontally
                   ) {
                       Icon(imageVector = it.icon, contentDescription = it.name )
                       if(selected) Text(text = it.name, textAlign = TextAlign.Center, fontSize = 9.sp)
                   }
               }
           )
       }
    }
}
@Composable
fun SetDataMap() {
    Text(text = "MAP Coming soon", modifier = Modifier.fillMaxSize())

}
