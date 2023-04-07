package com.kal.brawlstatz2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kal.brawlstatz2.data.BottomNavItem
import com.kal.brawlstatz2.presentation.ShowBrawlersList
import com.kal.brawlstatz2.sealed.DataState
import com.kal.brawlstatz2.ui.theme.BrawlStatz2Theme
import com.kal.brawlstatz2.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    private val viewModel : MainViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BrawlStatz2Theme {
                
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
                    val navController = rememberNavController()
                    LaunchedEffect(key1 = isVisible, key2 = isSearch){
                        if(isVisible&&isSearch){
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
                                                     overflow = TextOverflow.Ellipsis
                                                 )
                                             }
                                         },
                                         colors = TopAppBarDefaults.topAppBarColors(Color(0xFF022C65)),
                                         navigationIcon = {
                                             Icon(
                                                 painter = painterResource(id = R.drawable.logo_menu),
                                                 contentDescription = null,
                                                 modifier = Modifier.padding(start = 10.dp, end = 4.dp)
                                             )
                                         },

                                         actions = {
                                             if(isSearch&&isSearchVisible){
                                                 isVisible = !isVisible
                                                 OutlinedTextField(
                                                     value = value,
                                                     onValueChange = {
                                                         value = it
                                                         viewModel.findData(value.lowercase())
                                                     },
                                                     modifier = Modifier
                                                         .focusRequester(focusRequester)
                                                         .width(200.dp),
                                                     label = {
                                                         Text(text = "Search")
                                                     },
                                                     placeholder = { Text(text = "Name, Rarity, etc.")}
                                                 )
                                             }
                                             if(isSearchVisible){
                                                 IconButton(onClick = {
                                                     if(value=="") isSearch = !isSearch
                                                     else {
                                                         value=""
                                                         viewModel.findData(value.lowercase())
                                                     }

                                                 }){
                                                     Icon(
                                                         if(isSearch) Icons.Default.Close
                                                         else Icons.Default.Search ,
                                                         contentDescription = "search"
                                                     )

                                                 }
                                                 IconButton(onClick = { showMenu = true }) {
                                                     Icon(Icons.Default.MoreVert, contentDescription = null )
                                                 }

                                                     DropdownMenu(
                                                         expanded = showMenu,
                                                         onDismissRequest = { showMenu=false }) {
                                                         DropdownMenuItem(onClick = { viewModel.sortData()
                                                                 showMenu=false}, leadingIcon = {Icon(
                                                             Icons.Filled.Call,
                                                             contentDescription = null)
                                                         },
                                                             text = {
                                                                 Text("Epic")
                                                             }
                                                         )

                                                         DropdownMenuItem(onClick = {
                                                             viewModel.findData("")
                                                             showMenu=false
                                                                                    }, leadingIcon = {Icon(
                                                             Icons.Filled.Call,
                                                             contentDescription = null)
                                                         },
                                                             text = {
                                                                 Text("Call")
                                                             }
                                                         )
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
                                .background(Color(0xFF021835))
                        ){
                            Navigation(navController = navController)
                            Divider()
                        }

                    }
                }
            }

    }
    @Composable
    fun Navigation(navController: NavHostController) {
        NavHost(navController = navController, startDestination = "brawler" ){
            composable("brawler"){
                SetDataBrawler(viewModel)
                
            }
            composable("map"){
                SetDataMap()
            }
            composable("meta"){

                SetDataMeta(viewModel)
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
        containerColor = Color(0xFF022C65),
    ){
       items.forEach{
           val selected = it.route == backStackEntry.value?.destination?.route
           NavigationBarItem(
               selected = selected,
               modifier = Modifier.height(45.dp),
               onClick = { onItemClicked(it) },
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
fun SetDataBrawler(viewModel: MainViewModel) {
    viewModel.BrawlerClicked()
    when (val result = viewModel.response.value) {

        is DataState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
        is DataState.Sorted -> {
            ShowBrawlersList(brawler = result.data)
        }
        is DataState.Success -> {
            ShowBrawlersList(brawler = result.data)
        }
        is DataState.Failure -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = result.message)
            }
        }
        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error")
            }
        }

    }
}
    @Composable
    fun SetDataMeta(viewModel:MainViewModel) {
        viewModel.metaClicked()
        when(val result = viewModel.response.value){
            is DataState.Loading ->{
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ){
                    CircularProgressIndicator()
                }
            }

            is DataState.Meta ->{
                ShowBrawlersList(brawler = result.data)
            }
            is DataState.Failure ->{
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = result.message)
                }
            }
            else ->{
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(text = "Errorr")
                }
            }

        }
    }

@Composable
fun SetDataMap() {
    Text(text = "META Coming soon")
}
