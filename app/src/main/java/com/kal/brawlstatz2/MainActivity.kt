package com.kal.brawlstatz2

import android.app.Activity
import android.app.DownloadManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.kal.brawlstatz2.broadcast.UpdateBroadcastReceiver
import com.kal.brawlstatz2.data.BottomNavItem
import com.kal.brawlstatz2.data.Events
import com.kal.brawlstatz2.data.clubleague
import com.kal.brawlstatz2.downloader.UpdateDownloader
import com.kal.brawlstatz2.presentation.BrawlersList
import com.kal.brawlstatz2.presentation.Curr
import com.kal.brawlstatz2.presentation.SetTrackerData
import com.kal.brawlstatz2.presentation.ShimmerListItem
import com.kal.brawlstatz2.presentation.ShimmerListItem1
import com.kal.brawlstatz2.presentation.ShimmerListItem2
import com.kal.brawlstatz2.presentation.ShowMetaList
import com.kal.brawlstatz2.ui.theme.*
import com.kal.brawlstatz2.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    private var prevTag = ""

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            val token = task.result
            Log.d(TAG, token)
        })
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            val activity = (LocalContext.current as? Activity)

            if (ContextCompat.checkSelfPermission(
                    LocalContext.current,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 202)
                }
            }


            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            val theme = sharedPref?.getInt("theme", 0)
            var tag by remember {
                mutableStateOf(sharedPref?.getString("tag", ""))
            }

            var themeMode by remember {
                if (theme != null) {
                    mutableIntStateOf(theme)
                } else mutableIntStateOf(0)
            }
            if (sharedPref != null) {
                with(sharedPref.edit()) {
                    putInt("theme", themeMode)
                    apply()
                }
                with(sharedPref.edit()) {
                    putString("tag", tag)
                    apply()
                }
            }
            val viewModel = viewModel<MainViewModel>()

            LaunchedEffect(key1 = tag) {
                if (tag != "") viewModel.brawlStats(tag.toString())
            }

            AppTheme(themeMode) {

                var isSearch by remember {
                    mutableStateOf(false)
                }
                var isVisible = remember {
                    false
                }
                val focusRequester = remember {
                    FocusRequester()
                }
                var value by remember {
                    mutableStateOf("")
                }
                var tabCurrent by remember {
                    mutableStateOf("brawler")
                }

                val focusManager = LocalFocusManager.current


                val navController = rememberNavController()
//                val navController2 = rememberNavController()
                LaunchedEffect(key1 = true) {
                    navController.enableOnBackPressed(false)
//                    navController2.enableOnBackPressed(false)
                }
                BackHandler(onBack = {
                    if (navController.currentDestination?.route == "brawler") {
                        activity?.finish()
                    } else {
                        navController.navigate("brawler")
                        tabCurrent = "brawler"
                        isSearch = false
                    }
                })

                var fabExpanded by remember {
                    mutableStateOf(false)
                }
                var themeChanger by remember {
                    mutableStateOf(false)
                }
                LaunchedEffect(key1 = isVisible, key2 = isSearch) {
                    if (isVisible && isSearch) {
                        delay(50)
                        focusManager.clearFocus()

                        focusRequester.requestFocus()
                    }
                    if (!isSearch) {
                        focusManager.clearFocus()
                    }
                }
                LaunchedEffect(key1 = viewModel.previewBrawler.value){
                    if(viewModel.previewBrawler.value!=null) Intent(applicationContext,previewActivity::class.java).also {
                        var bundle = Bundle()
                        var parcel = viewModel.previewBrawler.value
                        bundle.putParcelable("preview",parcel)
                        it.putExtra("theme",themeMode)
                        it.putExtra("bundle",bundle)
                        startActivity(it)
                        viewModel.previewBrawler.value=null
                    }
                }

//                NavHost(navController = navController2, startDestination = "main"){
//                    composable("main"){
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
                                                    color = MaterialTheme.colorScheme.onBackground,
                                                    fontWeight = FontWeight.Bold,
                                                )
                                            }
                                        },
                                        colors = TopAppBarDefaults.topAppBarColors(MaterialTheme.colorScheme.background),
                                        navigationIcon = {
                                            Icon(
                                                painter = painterResource(id = R.drawable.logo_menu),
                                                contentDescription = null,
                                                modifier = Modifier.padding(
                                                    start = 10.dp,
                                                    end = 4.dp
                                                ),
                                                tint = MaterialTheme.colorScheme.onBackground
                                            )
                                        },

                                        actions = {

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (tabCurrent == "events") {
                                                    Text(
                                                        text = "EVENTS    ",
                                                        color = MaterialTheme.colorScheme.onSecondary,
                                                        fontSize = 9.sp
                                                    )
                                                } else if (tabCurrent == "meta") {
                                                    Text(
                                                        text = "V${viewModel.metaVer.value}   ",
                                                        color = MaterialTheme.colorScheme.onSecondary,
                                                        fontSize = 9.sp
                                                    )
                                                } else if (tabCurrent == "tracker") {
                                                    Icon(
                                                        imageVector = Icons.Default.Refresh,
                                                        contentDescription = null,
                                                        Modifier.clickable {
                                                            viewModel.brawlStats(tag.toString())
                                                        })
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Icon(
                                                        imageVector = Icons.Default.Edit,
                                                        contentDescription = null,
                                                        Modifier.clickable {
                                                            prevTag = tag.toString()
                                                            tag = ""
                                                        })
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

                                                        placeholder = {
                                                            Text(
                                                                text = " Search",
                                                                color = MaterialTheme.colorScheme.onTertiaryContainer
                                                            )
                                                        },
                                                        shape = RoundedCornerShape(100),
                                                        colors = OutlinedTextFieldDefaults.colors(
                                                            focusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                                            unfocusedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                                            disabledContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                                            focusedBorderColor = MaterialTheme.colorScheme.background,
                                                            unfocusedBorderColor = MaterialTheme.colorScheme.background,
                                                            cursorColor = MaterialTheme.colorScheme.onTertiaryContainer
                                                        )
                                                    )
                                                }
                                                Card(
                                                    modifier = Modifier.clip(RoundedCornerShape(100)),
                                                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.tertiaryContainer)
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

                            floatingActionButton = {
                                if (tabCurrent != "events") {


                                    Column(horizontalAlignment = Alignment.End) {
                                        AnimatedVisibility(visible = fabExpanded) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "Soon ",
                                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                                        shadow = Shadow(
                                                            offset = Offset(1f, 1f),
                                                            blurRadius = 20f
                                                        ),
                                                        textIndent = TextIndent(0.sp),
                                                        fontSize = 20.sp
                                                    ),
                                                    color = Color.White
                                                )
                                                FloatingActionButton(
                                                    onClick = { },
                                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                                    shape = CircleShape,
                                                    modifier = Modifier.size(60.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.MoreVert,
                                                        contentDescription = null
                                                    )
                                                }
                                            }

                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        AnimatedVisibility(visible = fabExpanded) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                    text = "Check for Updates ",
                                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                                        shadow = Shadow(
                                                            offset = Offset(1f, 1f),
                                                            blurRadius = 20f
                                                        ),
                                                        textIndent = TextIndent(0.sp),
                                                        fontSize = 20.sp
                                                    ),
                                                    color = Color.White
                                                )
                                                FloatingActionButton(
                                                    onClick = {
                                                        fabExpanded = !fabExpanded
                                                        viewModel.isUpdateAvailable.value =
                                                            viewModel._info.version.toFloat() > BuildConfig.VERSION_NAME.toFloat()
                                                        if (!viewModel.isUpdateAvailable.value) Toast.makeText(
                                                            applicationContext,
                                                            "Latest Version Installed",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    },
                                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                                    shape = CircleShape,
                                                    modifier = Modifier.size(60.dp)
                                                ) {
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.cloud),
                                                        contentDescription = null
                                                    )
                                                }
                                            }

                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        AnimatedVisibility(visible = fabExpanded) {

                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "Theme ",
                                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                                        shadow = Shadow(
                                                            offset = Offset(1f, 1f),
                                                            blurRadius = 20f
                                                        ),
                                                        textIndent = TextIndent(0.sp),
                                                        fontSize = 20.sp
                                                    ),
                                                    color = Color.White
                                                )
                                                FloatingActionButton(
                                                    onClick = {
                                                        fabExpanded = !fabExpanded
                                                        themeChanger = true

                                                    },
                                                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                                    shape = CircleShape,
                                                    modifier = Modifier.size(60.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                                        contentDescription = null
                                                    )
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))

                                        FloatingActionButton(
                                            onClick = { fabExpanded = !fabExpanded },
                                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                            shape = RoundedCornerShape(20.dp),
                                            modifier = Modifier.size(60.dp)
                                        ) {
                                            var angle = animateFloatAsState(
                                                targetValue = if (fabExpanded) 1.0F else 0F,
                                                animationSpec = tween(durationMillis = 1000)
                                            )

                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = null,
                                                modifier = Modifier.rotate(135 * angle.value)
                                            )
                                        }
                                    }

                                }
                            },
                            bottomBar = {
                                BottomNavBar(
                                    items = listOf(
                                        BottomNavItem("BRAWLER", "brawler", Icons.Default.Home),
                                        BottomNavItem("EVENTS", "events", Icons.Default.Notifications),
                                        BottomNavItem("META", "meta", Icons.Default.Face),
                                        BottomNavItem("STATS", "tracker", Icons.Default.MoreVert)
                                    ),
                                    navController = navController,
                                    onItemClicked = {
                                        tabCurrent = it.route
                                        isSearch = false
                                        value = ""
                                        viewModel.isSearching.value = false
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
                                    .background(MaterialTheme.colorScheme.background)
                            )
                            {
                                Navigation(navController = navController, tag,viewModel) {
                                    tag = it
                                }


                                if (viewModel.isUpdateAvailable.value) {
                                    Update(viewModel, applicationContext)
                                }
                                if (themeChanger) {
                                    AlertDialog(onDismissRequest = { themeChanger = false }) {
                                        Box(
                                            modifier = Modifier
                                                .clip(
                                                    RoundedCornerShape(20.dp)
                                                )
                                                .background(
                                                    MaterialTheme.colorScheme.tertiaryContainer
                                                )
                                                .fillMaxWidth()
                                                .padding(20.dp)

                                        ) {
                                            Column {
                                                Text(
                                                    text = "Choose theme",
                                                    fontWeight = FontWeight.ExtraBold,
                                                    fontSize = 25.sp
                                                )
                                                Spacer(modifier = Modifier.height(5.dp))
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            themeChanger = false
                                                            themeMode = 0
                                                        }
                                                ) {
                                                    Text(
                                                        text = "Dark",
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 18.sp
                                                    )
                                                    if (themeMode == 0) Icon(
                                                        Icons.Default.Check,
                                                        contentDescription = null
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(5.dp))
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clickable {
                                                            themeChanger = false
                                                            themeMode = 1
                                                        }
                                                ) {
                                                    Text(
                                                        text = "Light",
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 18.sp
                                                    )
                                                    if (themeMode == 1) Icon(
                                                        Icons.Default.Check,
                                                        contentDescription = null
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(5.dp))
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.SpaceBetween,
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .clickable {
                                                                themeChanger = false
                                                                themeMode = 2
                                                            }
                                                    ) {
                                                        Text(
                                                            text = "Dynamic",
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 18.sp
                                                        )
                                                        if (themeMode == 2) Icon(
                                                            Icons.Default.Check,
                                                            contentDescription = null
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }//
//                    }
//                    composable("preview"){
//                        var vm = viewModel<SceneViewModel>()
//                        viewModel.previewBrawler.value?.let { it1 -> Preview3D(brawler = it1, context = applicationContext, viewModel = vm ) }
//                    }
//
//                }

            }
        }

    }

    @Composable
    fun Navigation(navController: NavHostController, tag: String?,viewModel: MainViewModel, send: (String) -> Unit) {
        //val viewModel = viewModel<MainViewModel>()
        val brawlers = viewModel.blist.value
        val meta = viewModel.nestedList.value
        val isLoading = viewModel.isLoading.value
        val isSearching = viewModel.isSearching.value
        NavHost(navController = navController, startDestination = "brawler") {
            composable("brawler") {
                if (isLoading) {
                    Column {
                        Spacer(modifier = Modifier.height(40.dp))
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(10) {
                                ShimmerListItem(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.background)
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
                BrawlersList(brawler = brawlers, isSearching, viewModel)
            }
            composable("events") {
                if (isLoading) {
                    ShimmerListItem1()
                } else {
                    val navController1 = rememberNavController()
                    NavHost(navController = navController1, startDestination = "menu") {
                        composable("menu") {
                            SetDataMap(viewModel) { route ->
                                navController1.navigate(route = route)
                            }
                        }
                        composable("curr") { Curr(viewModel.activeList.value, viewModel, 0) }
                        composable("up") { Curr(viewModel.upcomingList.value, viewModel, 1) }
                    }
                }
            }
            composable("meta") {
                if (isLoading) {
                    Column {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(10) {
                                ShimmerListItem2(
                                    modifier = Modifier
                                        .background(MaterialTheme.colorScheme.background)
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }
                        }
                    }
                }
                ShowMetaList(meta, viewModel.sortedMetaList)
            }
            composable("tracker") {
                if (tag != "") {
                    if (viewModel.isLoadingStats.value == 0) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Center
                        ) { CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground, strokeCap = StrokeCap.Round) }
                    } else if (viewModel.isLoadingStats.value == 1) SetTrackerData(viewModel)
                    else {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Center
                        ) { Text(text = "Something went Wrong!") }
                    }
                } else {
                    GetTag(prevTag) {
                        send(it)
                    }
                }
            }
        }
    }
}

@Composable
fun GetTag(
    tag: String,
    onClick: (String) -> Unit
) {
    var string by remember {
        mutableStateOf(tag)
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
        Box(
            Modifier
                .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(10.dp))
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.7f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Enter Tag",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(value = string, onValueChange = {
                    string = it
                }, leadingIcon = { Text(text = "#") }
                )
                Spacer(modifier = Modifier.height(4.dp))
                Button(onClick = {
                    onClick(string)
                }
                ) {
                    Text(text = "OK")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun Update(viewModel: MainViewModel, context: Context) {
    var isUpdateClicked by remember {
        mutableStateOf(false)
    }
    AlertDialog(
        onDismissRequest = { if (!isUpdateClicked) viewModel.isUpdateAvailable.value = false },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(20.dp)
    ) {
        Box {
            Column {
                Text(
                    text = "Update Available! v${viewModel._info.version}".uppercase(),
                    fontSize = 18.sp,
                    color = chromatic,
                    fontWeight = FontWeight.Bold
                )
                Text(text = "Changelog:", fontSize = 16.sp, color = rare)
                LazyColumn {
                    items(viewModel.changelog.value.size) {
                        Text(text = "${it + 1}. ${viewModel.changelog.value[it]}", fontSize = 14.sp)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 40.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    val url =
                        "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/app-release.apk?alt=media&token=d801241b-fdde-471f-af15-90ff20ae0e52"
                    val downloader = UpdateDownloader()

                    Button(
                        onClick = {
                            isUpdateClicked = true
                            var broadcast = UpdateBroadcastReceiver()
                            context.registerReceiver(
                                broadcast,
                                IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                                Context.RECEIVER_EXPORTED
                            )
                            viewModel.downloadId = downloader.download(url, context)

                        },
                        colors = ButtonColors(
                            containerColor = Color.Green,
                            disabledContainerColor = Color.Green,
                            contentColor = Color.Black,
                            disabledContentColor = Color.Black
                        ),
                        enabled = !isUpdateClicked,
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            Text(
                                text = if (isUpdateClicked) "DOWNLOADING" else "UPDATE",
                                modifier = Modifier.padding(0.dp, 0.dp, 6.dp, 0.dp)
                            )
                            if (isUpdateClicked) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.Black,
                                    strokeCap = StrokeCap.Round,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Image(
                                    painter = painterResource(id = R.drawable.cloud),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                    if (isUpdateClicked) {
                        Icon(imageVector = Icons.Default.Close,
                            contentDescription = null,
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    isUpdateClicked = false
                                    downloader.cancelDownload(viewModel.downloadId, context)
                                })
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavBar(
    items: List<BottomNavItem>,
    navController: NavController,
    onItemClicked: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    NavigationBar(
        modifier = Modifier.height(60.dp),
        tonalElevation = 1.dp,
        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    ) {
        Column {
            Divider(color = MaterialTheme.colorScheme.primaryContainer)
            Row {
                items.forEach {
                    val selected = it.route == backStackEntry.value?.destination?.route
                    NavigationBarItem(
                        selected = selected,
                        onClick = { if (!selected) onItemClicked(it) },
                        icon = {
                            Column(
                                horizontalAlignment = CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = it.icon,
                                    contentDescription = it.name,
                                    modifier = Modifier.size(20.dp)
                                )

                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onTertiaryContainer,
                            indicatorColor = MaterialTheme.colorScheme.tertiaryContainer
                        ),
                        label = {
                            Text(
                                text = it.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp,
                                lineHeight = 10.sp,
                                color = if(selected)MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                            )
                        },
                        alwaysShowLabel = true
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
    val eventCardList = listOf(
        Events("CURRENT", "curr", Color.Green, R.drawable.c1, !viewModel.apiLock.value),
        Events("UPCOMING", "up", Color.Blue, R.drawable.c2, !viewModel.apiLock.value)
    )
    Column(Modifier.padding(vertical = 6.dp)) {
        Row(Modifier.weight(3.1f)) {
            Spacer(modifier = Modifier.width(3.dp))
            for (it in eventCardList) {
                Spacer(modifier = Modifier.width(3.dp))
                Card(
                    Modifier
                        .weight(1f)
                        .clickable {
                            if (it.enabled) onclick(it.route)
                        },
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Center) {
                        Image(
                            painter = painterResource(id = it.drawable),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                        if(viewModel.apiLock.value){
                            Image(
                                painter = painterResource(id = R.drawable.lock),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(14.dp)
                                    .size(30.dp)
                                    .align(BottomEnd),
                                alpha = 0.6f
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.6f))
                                .padding(10.dp)
                        ) {
                            Text(
                                text = it.cardName,
                                style = MaterialTheme.typography.bodyMedium + TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                    textIndent = TextIndent(0.sp),
                                    fontSize = 20.sp
                                ),
                                color = Color.White
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(3.dp))
            }
            Spacer(modifier = Modifier.width(3.dp))
        }
        Spacer(modifier = Modifier.height(6.dp))
        Row(Modifier.weight(3.1f)) {
            Spacer(modifier = Modifier.width(6.dp))
            Card(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable {
                    },

                ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(), contentAlignment = TopEnd
                ) {

                    val timeStamp: String = java.lang.String.valueOf(
                        TimeUnit.MILLISECONDS.toSeconds(
                            System.currentTimeMillis()
                        )
                    )
                    val currtime: Long = timeStamp.toLong() * 1000
                    val difTime = viewModel.bp.value[0].toLong().minus(currtime)
                    val dayLeft = difTime / 86400000
                    val hourLeft = (difTime % 86400000) / 3600000
                    val minLeft = ((difTime % 86400000) % 3600000) / 60000
                    GlideImage(
                        model = "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/bpb.webp?alt=media&token=${viewModel.bp.value[2]}",
                        contentDescription = null,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        it.centerCrop()
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "BRAWL PASS",
                            style = MaterialTheme.typography.bodyMedium + TextStyle(
                                shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                textIndent = TextIndent(0.sp),
                                fontSize = 20.sp
                            ),
                            color = Color.White,
                            modifier = Modifier.align(
                                TopStart
                            )
                        )
                        Column(modifier = Modifier.align(Center)) {
                            Text(
                                text = viewModel.bp.value[1],
                                fontSize = 22.sp,
                                textAlign = TextAlign.End,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic
                            )
                            Text(
                                text = "Season ${viewModel.bp.value[2]}",
                                color = Color.White,
                                modifier = Modifier.align(
                                    Alignment.End
                                )
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.bp),
                            contentDescription = null,
                            modifier = Modifier
                                .align(
                                    BottomStart
                                )
                                .size(80.dp, 42.dp)
                                .rotate(-5f)
                        )

                        Text(
                            text = if (difTime > 0) {
                                "${dayLeft}d ${hourLeft}h ${minLeft}m"
                            } else "ENDED",
                            style = MaterialTheme.typography.bodyMedium + TextStyle(
                                shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                textIndent = TextIndent(0.sp),
                                color = if (dayLeft == 0L) Color.Red else Color.White
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
                Box(
                    modifier = Modifier
                        .fillMaxSize(), contentAlignment = TopEnd
                ) {

                    val timeStamp: String = java.lang.String.valueOf(
                        TimeUnit.MILLISECONDS.toSeconds(
                            System.currentTimeMillis()
                        )
                    )
                    val currtime: Long = timeStamp.toLong() * 1000
                    val difTime = viewModel.pl.value[0].toLong().minus(currtime)
                    val dayLeft = difTime / 86400000
                    val hourLeft = (difTime % 86400000) / 3600000
                    val minLeft = ((difTime % 86400000) % 3600000) / 60000
                    GlideImage(
                        model = "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/bpb1.webp?alt=media&token=${viewModel.pl.value[1]}",
                        contentDescription = null,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        it.centerCrop()
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "POWER LEAGUE",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium + TextStyle(
                                shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                textIndent = TextIndent(0.sp),
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.align(
                                TopStart
                            )
                        )
                        Text(
                            text = "Season ${viewModel.pl.value[1]}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.align(
                                CenterEnd
                            )
                        )
                        Image(
                            painter = painterResource(id = R.drawable.pl),
                            contentDescription = null,
                            modifier = Modifier
                                .align(
                                    BottomStart
                                )
                                .size(60.dp)
                                .rotate(-5f)
                        )
                        Text(
                            text = if (difTime > 0) {
                                "${dayLeft}d ${hourLeft}h ${minLeft}m"
                            } else "ENDED",
                            style = MaterialTheme.typography.bodyMedium + TextStyle(
                                shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                textIndent = TextIndent(0.sp),
                                color = if (dayLeft == 0L) Color.Red else Color.White
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
        if (iscg) {
            val difTime = viewModel.cl.value[0].toLong().minus(viewModel.timeFromServer.value)
            val dayLeft = difTime / 86400000
            val hourLeft = (difTime % 86400000) / 3600000
            val minLeft = ((difTime % 86400000) % 3600000) / 60000
            Card(
                Modifier
                    .weight(2.2f)
                    .padding(horizontal = 4.dp)
                    .clickable {},

                ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.clb),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.7f))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "CLUB GAMES",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium + TextStyle(
                                shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                textIndent = TextIndent(0.sp),
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.align(TopStart)
                        )
                        Text(
                            text = "${dayLeft}d ${hourLeft}h ${minLeft}m", modifier = Modifier
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
                                color = if (dayLeft == 0L) Color.Red else Color.White
                            )
                        )

                        Row(
                            modifier = Modifier
                                .align(
                                    BottomStart
                                )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.cg),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(68.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.align(Bottom)) {
                                Text(
                                    "QUEST WEEK", color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                        textIndent = TextIndent(0.sp),
                                        fontSize = 18.sp
                                    ), modifier = Modifier.offset(y = (4).dp)
                                )
                                Text(
                                    "Complete quests with your club!", color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                        textIndent = TextIndent(0.sp),
                                        fontSize = 12.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                            }
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.align(TopEnd)
                        ) {
                            Text(
                                text = " Club League in", color = Color.White,
                                style = MaterialTheme.typography.bodyMedium + TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                    textIndent = TextIndent(0.sp),
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text =if(hourLeft+13>=24) "${dayLeft+1}d ${(hourLeft+13)%24}h" else "${dayLeft}d ${hourLeft+13}h",
                                style = MaterialTheme.typography.bodyMedium + TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                    textIndent = TextIndent(0.sp),
                                    fontSize = 12.sp,
                                    color = if (dayLeft == 0L) Color.Red else Color.White
                                ), modifier = Modifier.offset(y = (-4).dp)
                            )
                        }
                    }
                }
            }
        } else {
            val difTime = viewModel.cl.value[0].toLong().minus(viewModel.timeFromServer.value).plus(46800000)
            val difTimeCg = viewModel.cl.value[0].toLong().minus(viewModel.timeFromServer.value)
            val dayLeftCg = difTimeCg / 86400000
            var hourLeftCg = (difTimeCg % 86400000) / 3600000
            var minLeftCg = ((difTimeCg % 86400000) % 3600000) / 60000
            val dayLeft = difTime / 86400000
            var hourLeft = (difTime % 86400000) / 3600000
            var minLeft = ((difTime % 86400000) % 3600000) / 60000

            val clEvent = listOf(
                clubleague(1, "EVENT DAY 1", 0, "Preparation !"),
                clubleague(2, "EVENT DAY 1", 4, "Compete with your club!"),
                clubleague(3, "EVENT DAY 2", 0, "Preparation !"),
                clubleague(4, "EVENT DAY 2", 4, "Compete with your club!"),
                clubleague(5, "EVENT DAY 3", 0, "Preparation !"),
                clubleague(6, "EVENT DAY 3", 6, "Compete with your club!"),
                clubleague(7, "EVENT ENDED", 0, "Club Games will start soon!")
            )
            var day=7 - dayLeft - 1
            Card(
                Modifier
                    .weight(2.2f)
                    .padding(horizontal = 6.dp)
                    .clickable {},
            ) {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.clb),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.7f))
                            .padding(10.dp)
                    ) {
                        Text(
                            text = "CLUB LEAGUE",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium + TextStyle(
                                shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                textIndent = TextIndent(0.sp),
                                fontSize = 20.sp
                            ),
                            modifier = Modifier.align(TopStart)
                        )
                        Text(
                            text = if (day == 6L) "ENDED" else "${if(day==-1L) hourLeft+24 else hourLeft}h ${minLeft}m",
                            modifier = Modifier
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
                                color = if (dayLeft == 0L) Color.Red else Color.White
                            )
                        )

                        Row(
                            modifier = Modifier
                                .align(
                                    BottomStart
                                )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.pm),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(68.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.align(Bottom)) {
                                Text(
                                    clEvent[if(day==-1L)0 else day.toInt()].name, color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                        textIndent = TextIndent(0.sp),
                                        fontSize = 18.sp
                                    ), modifier = Modifier.offset(y = (4).dp)
                                )
                                Text(
                                    clEvent[if(day==-1L)0 else day.toInt()].text, color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                        textIndent = TextIndent(0.sp),
                                        fontSize = 12.sp
                                    )
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                            }
                        }
                        if (clEvent[if(day==-1L)0 else day.toInt()].ticket != 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.align(CenterEnd)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ct),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                                Text(
                                    text = " x${clEvent[if(day==-1L)0 else day.toInt()].ticket}", color = Color.White,
                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                        textIndent = TextIndent(0.sp),
                                        fontSize = 20.sp
                                    )
                                )
                            }
                        }
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.align(TopEnd)
                        ) {
                            Text(
                                text = " Club Games in", color = Color.White,
                                style = MaterialTheme.typography.bodyMedium + TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                    textIndent = TextIndent(0.sp),
                                    fontSize = 12.sp
                                )
                            )
                            Text(
                                text = if(dayLeftCg!=0L)"${dayLeftCg}d ${hourLeftCg}h" else "0d ${hourLeftCg}h ${minLeftCg}m",
                                style = MaterialTheme.typography.bodyMedium + TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                    textIndent = TextIndent(0.sp),
                                    fontSize = 12.sp,
                                    color = if (dayLeft == 0L) Color.Red else Color.White
                                ), modifier = Modifier.offset(y = (-4).dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
