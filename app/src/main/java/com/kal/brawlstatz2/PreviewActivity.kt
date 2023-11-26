package com.kal.brawlstatz2

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kal.brawlstatz2.data.Brawler
import com.kal.brawlstatz2.downloader.ModelDownloader
import com.kal.brawlstatz2.presentation.First
import com.kal.brawlstatz2.presentation.ModelScreen
import com.kal.brawlstatz2.ui.theme.AppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PreviewActivity : ComponentActivity() {
    @SuppressLint("Range", "CoroutineCreationDuringComposition")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme(themeMode = intent.getIntExtra("theme",0)) {
                // A surface container using the 'background' color from the theme
                var fileExist by remember{
                    mutableStateOf(false)
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    var bundle = intent.getBundleExtra("bundle")
                    var brawler = bundle?.getParcelable<Brawler>("preview")

                    fileExist= applicationContext.getExternalFilesDir("model/${brawler?.bname}/${brawler?.bname}${brawler?.zver}.glb")?.isFile == true
                    if(!fileExist&&brawler?.model3d=="true"){
                        var isFinishedDl = false
                        var progress =0
                        var downloadManager=getSystemService(DownloadManager::class.java)
                        var x = ModelDownloader().download(brawler?.model.toString(),brawler?.bname.toString(),brawler?.zver.toString(), applicationContext)
                        var job = CoroutineScope(Dispatchers.Default).launch {
                            while (!isFinishedDl) {
                                val q = DownloadManager.Query().setFilterById(x)
                                val cursor = downloadManager.query(q)
                                if (cursor.moveToFirst()) {
                                    val colStatus = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                                    if (colStatus >= 0) {
                                        when (cursor.getInt(colStatus)) {
                                            DownloadManager.STATUS_FAILED -> {isFinishedDl = true }
                                            DownloadManager.STATUS_PAUSED -> { }
                                            DownloadManager.STATUS_PENDING -> { }
                                            DownloadManager.STATUS_RUNNING -> {
                                                val colIdTotal =  cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                                                if (colIdTotal >= 0) {
                                                    val total = cursor.getLong(colIdTotal)
                                                    if (total > 0L) {
                                                        val colIdBytes = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                                                        if (colIdBytes >= 0) {
                                                            val downloaded = cursor.getLong(colIdBytes)
                                                            progress = (downloaded * 100L / total).toInt()
                                                        }
                                                    }
                                                }
                                            }
                                            DownloadManager.STATUS_SUCCESSFUL -> {
                                                progress = 100
                                                isFinishedDl = true
                                                fileExist=true
                                            }
                                        }

                                    }
                                } else {
                                    isFinishedDl = true
                                }
                            }
                        }
                    }
                    var scaffold = rememberBottomSheetScaffoldState()
                    var navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "pre3d"){
                        val file ="file:///storage/emulated/0/Android/data/com.kal.brawlstatz2/files/model/${brawler?.bname}/${brawler?.bname}${brawler?.zver}.glb".toUri().toString()
                        composable("pre3d"){

                            Box(modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)){

                                if(fileExist) First(file)
                                else if(brawler?.model3d=="false") {
                                    GlideImage(model = brawler.bmodel, contentDescription = null, modifier = Modifier.padding(0.dp,80.dp).fillMaxHeight(0.5f).align(
                                        Alignment.TopCenter))
                                }
                                else{
                                    CircularProgressIndicator(strokeCap = StrokeCap.Round, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier
                                        .align(
                                            Alignment.Center
                                        )
                                        .offset(0.dp, (-80).dp))
                                    Icon(painter = painterResource(id = R.drawable.cloud), contentDescription = null, modifier = Modifier
                                        .align(
                                            Alignment.Center
                                        )
                                        .offset(0.dp, (-80).dp))
                                }

                                LaunchedEffect(key1 = true){
                                    delay(100)
                                    scaffold.bottomSheetState.expand()
                                }
                                BottomSheetScaffold(scaffoldState = scaffold, sheetContainerColor = MaterialTheme.colorScheme.primaryContainer, sheetContent = {
                                    Box (modifier = Modifier
                                        .padding(10.dp, 0.dp)){
                                        Column (
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.SpaceEvenly
                                        ){
                                            brawler?.bname?.let {
                                                Text(
                                                    text = it,
                                                    fontSize = 36.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = brawler.color,
                                                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 1f),
                                                    )
                                                )
                                            }
                                            Box(modifier = Modifier
                                                .border(
                                                    1.dp,
                                                    MaterialTheme.colorScheme.onSecondary,
                                                    RoundedCornerShape(10.dp)
                                                )
                                                .clip(RoundedCornerShape(10.dp))
                                                .fillMaxWidth()
                                                .padding(vertical = 2.dp, horizontal = 4.dp)
                                            ){
                                                Text(
                                                    text = brawler?.babout.toString(),
                                                    fontSize = 16.sp,
                                                    fontStyle = FontStyle.Italic,
                                                    style = TextStyle(
                                                        textIndent = TextIndent(0.sp),
                                                        textAlign = TextAlign.Center,
                                                    ),
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                            }
                                            Column(modifier = Modifier.padding(10.dp,4.dp)){
                                                Row(modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(0.dp, 2.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                                                    Text(text = "RARITY:",
                                                        fontSize = 14.sp, textAlign = TextAlign.End,lineHeight = 15.sp)
                                                    brawler?.color?.let {
                                                        Text(text = brawler?.brare.toString().uppercase(),
                                                            fontSize = 14.sp, textAlign = TextAlign.End,
                                                            color = it, lineHeight = 15.sp,style= TextStyle(shadow = Shadow(
                                                                Color.Black, Offset(1f,1f),1f)
                                                            )
                                                        )
                                                    }
                                                }
                                                Row(modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(0.dp, 2.dp),horizontalArrangement = Arrangement.SpaceBetween) {

                                                    Text(text = "CLASS:",
                                                        fontSize = 14.sp, textAlign = TextAlign.End, lineHeight = 15.sp)
                                                    Text(text = brawler?.classType.toString().uppercase(),
                                                        fontSize = 14.sp, textAlign = TextAlign.End, lineHeight = 15.sp)
                                                }
                                                Row(modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(0.dp, 2.dp),horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                                    Text(text = "MOVEMENT SPEED:",
                                                        fontSize = 14.sp, textAlign = TextAlign.End, lineHeight = 15.sp)
                                                    Text(text = brawler?.movementSpeed.toString().uppercase(),
                                                        fontSize = 14.sp, textAlign = TextAlign.End, lineHeight = 15.sp)
                                                }
                                            }
                                            HorizontalDivider()
                                            Text(text = "MASTERY", style = MaterialTheme.typography.bodyMedium + TextStyle(
                                                shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 1f),
                                            ),
                                                fontSize = 30.sp, textAlign = TextAlign.End, lineHeight = 24.sp, fontWeight = FontWeight.Bold)
                                            Box{

                                                Row(modifier = Modifier
                                                    .clip(RoundedCornerShape(4.dp))
                                                    .background(Color.Black.copy(alpha = 0.1f))
                                                    .padding(4.dp, 0.dp)
                                                    .align(Alignment.CenterStart)) {
                                                    Spacer(modifier = Modifier.width(60.dp))
                                                    Text(text = brawler?.mastery.toString(), color = Color(0xFFfcc500) ,style = MaterialTheme.typography.bodyMedium + TextStyle(
                                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 1f), textIndent = TextIndent(0.sp)
                                                    ), fontSize = 26.sp)
                                                }
                                                Image(painter = painterResource(id = R.drawable.titles), contentDescription = "mastery titles", modifier = Modifier
                                                    .size(50.dp)
                                                    .align(
                                                        Alignment.CenterStart
                                                    ))
                                            }
                                            Spacer(modifier = Modifier.height(10.dp))


                                        }
                                    }
                                }){
                                }
                                IconButton(onClick = {
                                    finish()
                                } ,modifier = Modifier.align(Alignment.TopStart)) {
                                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null )

                                }
                                    if(fileExist){
                                        IconButton(onClick = {
                                            navController.navigate("ar")
                                        }, modifier = Modifier.align(Alignment.TopEnd)) {
                                            Icon(painter = painterResource(id = R.drawable.ar), contentDescription = null,modifier=Modifier.size(20.dp))
                                        }
                                    }

                            }
                        }
                        composable("ar"){
                            Box(modifier = Modifier.fillMaxSize()){
                                ModelScreen(file = file)
                                IconButton(onClick = {
                                    navController.popBackStack()
                                } ,modifier = Modifier.align(Alignment.TopStart)) {
                                    Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null )

                                }
                            }
                        }
                        
                    }

                }
            }
        }
    }
}
