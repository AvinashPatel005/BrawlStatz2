package com.kal.brawlstatz2.presentation

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kal.brawlstatz2.R
import com.kal.brawlstatz2.data.ExpandedUnlockedBrawler
import com.kal.brawlstatz2.ui.theme.White2
import com.kal.brawlstatz2.viewmodel.CardsViewModel
import com.kal.brawlstatz2.viewmodel.MainViewModel
import com.kal.brawlstatz2.viewmodel.UnlockedViewModel
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalEncodingApi::class)
@Composable
fun SetTrackerData(viewModel: MainViewModel) {
    val data = viewModel.tracker.value
    val expansion = viewModel<UnlockedViewModel>()
    LazyColumn(Modifier.padding(8.dp)){
        item{
            Card(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
                shape = RectangleShape,
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
            ){
                Box(){
                    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)) {
                        Column() {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Column() {
                                    GlideImage(model = data.dp, contentDescription = null,Modifier.size(80.dp))
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Column() {
                                    Text(text = data.name, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        GlideImage(model = data.player_club.clubBanner, contentDescription = null,
                                            Modifier
                                                .size(18.dp)
                                                .offset(0.dp, 1.dp))
                                        Text(text = data.player_club.clubNane, fontSize =12.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row() {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(contentAlignment = Alignment.CenterStart){
                                        Row() {
                                            Spacer(modifier = Modifier.width(20.dp))
                                            Box(contentAlignment = Alignment.CenterStart){
                                                val lp = data.xp.progress.substring(0,data.xp.progress.indexOf('/')).trim().toFloat()
                                                val lt = data.xp.progress.substring(data.xp.progress.indexOf('/')+1).trim().toFloat()
                                                val xp_percent = lp/lt
                                                Box(
                                                    Modifier
                                                        .width(100.dp)
                                                        .height(25.dp)
                                                        .background(Color(0XFF380424))) {

                                                }
                                                Box(
                                                    Modifier
                                                        .width(100.dp * xp_percent)
                                                        .height(21.dp)
                                                        .background(Color(0XFF5cd6ff))) {

                                                }
                                            }
                                        }
                                        Box(contentAlignment = Alignment.Center){
                                            Image(painter = painterResource(id = R.drawable.level), contentDescription = null, modifier = Modifier.size(40.dp))
                                            Text(text = data.xp.level, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                        Text(text = data.xp.progress , fontSize = 12.sp,color = Color.White, fontWeight = FontWeight.Bold , style = TextStyle(
                                            shadow = Shadow(offset = Offset(1f,1f), blurRadius =0.7f)
                                        ), modifier =Modifier.offset(40.dp,(-0.10).dp) )

                                    }

                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(contentAlignment = Alignment.CenterStart){
                                        Row() {
                                            Spacer(modifier = Modifier.width(20.dp))
                                            Box(contentAlignment = Alignment.CenterStart){
                                                Box(
                                                    Modifier
                                                        .width(100.dp)
                                                        .height(25.dp)
                                                        .background(Color(0XFF380424))) {

                                                }
                                                Box(
                                                    Modifier
                                                        .width(98.dp)
                                                        .height(21.dp)
                                                        .background(Color(0XFFfabc3b))) {

                                                }
                                            }
                                        }
                                        Box(contentAlignment = Alignment.Center){
                                            GlideImage(model = data.trophy.trophyImg, contentDescription = null, modifier = Modifier.size(40.dp))
                                        }
                                        val lp = data.trophy.progress.substring(0,data.trophy.progress.indexOf('/')).trim().toInt()
                                        Text(text = lp.toString() , color = Color.White, fontWeight = FontWeight.ExtraBold , style = TextStyle(
                                            shadow = Shadow(offset = Offset(1f,1f), blurRadius =0.7f)
                                        ), modifier =Modifier.offset(45.dp,(-0.10).dp) )

                                    }

                                }
                            }
                        }
                        GlideImage(model = data.main, contentDescription = null, modifier = Modifier
                            .fillMaxHeight()
                            .alpha(0.7f))

                    }
                }
            }
        }
        
        item{
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(text = data.updated, fontSize = 14.sp)
            }
        }
        item{
           Column {
               Box(contentAlignment = Alignment.BottomCenter) {
                   Row(modifier = Modifier.padding(horizontal = 14.dp)){

                       Box(modifier = Modifier
                           .height(40.dp)
                           .fillMaxWidth()
                           .background(MaterialTheme.colorScheme.primaryContainer),
                           contentAlignment = Alignment.CenterStart){
                           Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                               Row() {
                                   Spacer(modifier = Modifier.width(35.dp))
                                   Text(text = "Highest Trophies", fontWeight = FontWeight.Bold)
                               }
                               Text(text = data.trophy.highest+"  ", fontWeight = FontWeight.Bold)
                           }
                       }
                   }
                   Image(painter = painterResource(id = R.drawable.ht), contentDescription = null, modifier = Modifier
                       .align(
                           Alignment.CenterStart
                       )
                       .size(45.dp))
               }
               Box(contentAlignment = Alignment.BottomCenter) {
                   Row(modifier = Modifier.padding(horizontal = 14.dp)){

                       Box(modifier = Modifier
                           .height(40.dp)
                           .fillMaxWidth()
                           .background(MaterialTheme.colorScheme.primaryContainer),
                           contentAlignment = Alignment.CenterStart){
                           Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                               Row() {
                                   Spacer(modifier = Modifier.width(35.dp))
                                   Text(text = "Season End Trophies", fontWeight = FontWeight.Bold)
                               }
                               Text(text = data.trophy.seasonEnd+"  ", fontWeight = FontWeight.Bold)
                           }
                       }
                   }
                   Image(painter = painterResource(id = R.drawable.ht), contentDescription = null, modifier = Modifier
                       .align(
                           Alignment.CenterStart
                       )
                       .size(45.dp))
               }
               Box(contentAlignment = Alignment.BottomCenter) {
                   Row(modifier = Modifier.padding(horizontal = 14.dp)){

                       Box(modifier = Modifier
                           .height(40.dp)
                           .fillMaxWidth()
                           .background(MaterialTheme.colorScheme.primaryContainer),
                           contentAlignment = Alignment.CenterStart){
                           Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                               Row() {
                                   Spacer(modifier = Modifier.width(35.dp))
                                   Text(text = "Highest Solo League", fontWeight = FontWeight.Bold)
                               }
                               Row() {
                                   GlideImage(model = data.league.highestSoloImg, contentDescription = null, modifier = Modifier.size(40.dp))
                                   Spacer(modifier = Modifier.width(10.dp))
                               }
                           }
                       }
                   }
                   Image(painter = painterResource(id = R.drawable.hsl), contentDescription = null, modifier = Modifier
                       .align(
                           Alignment.CenterStart
                       )
                       .size(45.dp))
               }
               Box(contentAlignment = Alignment.BottomCenter) {
                   Row(modifier = Modifier.padding(horizontal = 14.dp)){

                       Box(modifier = Modifier
                           .height(40.dp)
                           .fillMaxWidth()
                           .background(MaterialTheme.colorScheme.primaryContainer),
                           contentAlignment = Alignment.CenterStart){
                           Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                               Row() {
                                   Spacer(modifier = Modifier.width(35.dp))
                                   Text(text = "Highest Team League", fontWeight = FontWeight.Bold)
                               }
                               Row() {
                                   GlideImage(model = data.league.highestTeamImg, contentDescription = null, modifier = Modifier.size(40.dp))
                                   Spacer(modifier = Modifier.width(10.dp))
                               }
                           }
                       }
                   }
                   Image(painter = painterResource(id = R.drawable.htl), contentDescription = null, modifier = Modifier
                       .align(
                           Alignment.CenterStart
                       )
                       .size(45.dp))
               }
               Box(contentAlignment = Alignment.BottomCenter) {
                   Row(modifier = Modifier.padding(horizontal = 14.dp)){

                       Box(modifier = Modifier
                           .height(40.dp)
                           .fillMaxWidth()
                           .background(MaterialTheme.colorScheme.primaryContainer),
                           contentAlignment = Alignment.CenterStart){
                           Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                               Row() {
                                   Spacer(modifier = Modifier.width(35.dp))
                                   Text(text = "Highest Club League", fontWeight = FontWeight.Bold)
                               }
                               Row() {
                                   GlideImage(model = data.league.highestClubImg, contentDescription = null, modifier = Modifier.size(40.dp))
                                   Spacer(modifier = Modifier.width(10.dp))
                               }
                           }
                       }
                   }
                   Image(painter = painterResource(id = R.drawable.hcl), contentDescription = null, modifier = Modifier
                       .align(
                           Alignment.CenterStart
                       )
                       .size(45.dp))
               }

           }

        }
        item{
            Row(Modifier.padding(horizontal = 14.dp, vertical = 4.dp)) {
                Text(text = "Game Modes", fontWeight = FontWeight.Bold)
            }
        }
        item{
            Column {
                Box(contentAlignment = Alignment.BottomCenter) {
                    Row(modifier = Modifier.padding(horizontal = 14.dp)) {

                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row() {
                                    Spacer(modifier = Modifier.width(35.dp))
                                    Text(text = "3 VS 3 Victories", fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = data.victories.team3v3 + "  ",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.team3v3),
                        contentDescription = null,
                        modifier = Modifier
                            .align(
                                Alignment.CenterStart
                            )
                            .size(45.dp)
                    )
                }

                Box(contentAlignment = Alignment.BottomCenter) {
                    Row(modifier = Modifier.padding(horizontal = 14.dp)) {

                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row() {
                                    Spacer(modifier = Modifier.width(35.dp))
                                    Text(text = "Solo Victories", fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = data.victories.solo + "  ",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.solo),
                        contentDescription = null,
                        modifier = Modifier
                            .align(
                                Alignment.CenterStart
                            )
                            .size(45.dp)
                    )
                }
                Box(contentAlignment = Alignment.BottomCenter) {
                    Row(modifier = Modifier.padding(horizontal = 14.dp)) {

                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row() {
                                    Spacer(modifier = Modifier.width(35.dp))
                                    Text(text = "Duo Victories", fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = data.victories.duo + "  ",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.duo),
                        contentDescription = null,
                        modifier = Modifier
                            .align(
                                Alignment.CenterStart
                            )
                            .size(45.dp)
                    )
                }
                Box(contentAlignment = Alignment.BottomCenter) {
                    Row(modifier = Modifier.padding(horizontal = 14.dp)) {

                        Box(
                            modifier = Modifier
                                .height(40.dp)
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row() {
                                    Spacer(modifier = Modifier.width(35.dp))
                                    Text(text = "Most Challenge Wins", fontWeight = FontWeight.Bold)
                                }
                                Text(
                                    text = data.victories.challenge + "  ",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Image(
                        painter = painterResource(id = R.drawable.challenges),
                        contentDescription = null,
                        modifier = Modifier
                            .align(
                                Alignment.CenterStart
                            )
                            .size(45.dp)
                    )
                }

            }
        }

        item{
            Row(Modifier.padding(horizontal = 14.dp, vertical = 4.dp)) {
                Text(text = "Last 25 Battles", fontWeight = FontWeight.Bold)
            }
            LazyRow(Modifier.padding(horizontal = 14.dp)){
                items(data.battleLog){
                    GlideImage(model = it.gameMode, contentDescription = null, modifier = Modifier
                        .size(40.dp)
                        .background(
                            Color(it.background.toColorInt())
                        ))
                    Spacer(modifier = Modifier.width(2.dp))
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Row() {
                    Text(text = data.bwin, fontWeight = FontWeight.Bold, color = Color.Green)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = data.bloss, fontWeight = FontWeight.Bold, color = Color.Red)
                }
            }
        }
        item{
            Row(Modifier.padding(horizontal = 14.dp, vertical = 4.dp)) {
                Text(text = "Brawlers (${data.brawler.size}/${viewModel.size})", fontWeight = FontWeight.Bold)
            }
        }
        items((data.brawler.size+1)/2){
            if(expansion.c1list.value.id==it||expansion.c1list.value.id==1){
                Row (Modifier.padding(horizontal = 6.dp)){

                    val i1 = 2 * (it + 1) - 2
                    Column(modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(120.dp)
                        .padding(end = 2.dp, bottom = 4.dp)
                        .border(width = 1.dp, Color.Black)) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .background(Color(data.brawler[i1].bg.toColorInt()))){
                            GlideImage(model = data.brawler[i1].pro, contentDescription = null, modifier = Modifier.height(80.dp))
                            Box(){
                                Row() {
                                    Row(modifier = Modifier.background(Color.Black)) {
                                        Box(contentAlignment = Alignment.Center){
                                            GlideImage(
                                                model = if(data.brawler[i1].rank.toInt()<=4) R.drawable.rank_4
                                                else if(data.brawler[i1].rank.toInt()<=9) R.drawable.rank_9
                                                else if(data.brawler[i1].rank.toInt()<=14) R.drawable.rank_14
                                                else if(data.brawler[i1].rank.toInt()<=19) R.drawable.rank_19
                                                else if(data.brawler[i1].rank.toInt()<=24) R.drawable.rank_24
                                                else if(data.brawler[i1].rank.toInt()<=29) R.drawable.rank_29
                                                else if(data.brawler[i1].rank.toInt()<=34) R.drawable.rank_34
                                                else R.drawable.rank_35 , contentDescription = null,
                                                modifier = Modifier.size(23.dp))
                                            Column() {
                                                Spacer(modifier = Modifier.height(6.dp))
                                                Text(text = data.brawler[i1].rank, fontWeight = FontWeight.Bold, color = Color.White,fontSize = 9.sp, modifier = Modifier.rotate(-4f))
                                            }
                                        }
                                        Text(text = data.brawler[i1].name , fontWeight = FontWeight.Bold, color = White2, fontSize = 14.sp)

                                    }
                                    Image(painter = painterResource(id = R.drawable.dowtriangle), contentDescription = null , modifier = Modifier.height(23.dp))
                                }

                            }
                            Row(Modifier.align(Alignment.BottomEnd)) {
                                Image(painter = painterResource(id = R.drawable.uptriangle), contentDescription = null , modifier = Modifier.height(22.dp))
                                Row(
                                    Modifier
                                        .height(22.dp)
                                        .background(Color.Black)
                                        .padding(vertical = 2.dp)
                                ) {
                                    for (i in data.brawler[i1].unlocked){
                                        GlideImage(model = i, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                    }
                                }
                            }
                        }
                        Row(
                            Modifier
                                .background(Color.Black)
                                .fillMaxWidth()
                                .height(40.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            Column{
                                Text(text = "Level", color = White2, fontSize = 10.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(painter = painterResource(id = R.drawable.plevel), contentDescription =null, modifier = Modifier.size(25.dp))
                                    Text(text = data.brawler[i1].level, color = White2, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Column{
                                Text(text = "Current", color = White2, fontSize = 10.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(painter = painterResource(id = R.drawable.ht), contentDescription =null, modifier = Modifier.size(25.dp))
                                    Text(text = data.brawler[i1].currTrophy, color = White2, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Column{
                                Text(text = "Highest", color = White2, fontSize = 10.sp)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Image(painter = painterResource(id = R.drawable.ht), contentDescription =null, modifier = Modifier.size(25.dp))
                                    Text(text = data.brawler[i1].highTrophy, color = White2, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                    }
                    if(i1+1<data.brawler.size){
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .padding(start = 2.dp, bottom = 4.dp)
                            .border(width = 1.dp, Color.Black)) {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color(data.brawler[i1 + 1].bg.toColorInt()))){
                                GlideImage(model = data.brawler[i1+1].pro, contentDescription = null, modifier = Modifier.height(80.dp))
                                Box(){
                                    Row() {
                                        Row(modifier = Modifier.background(Color.Black)) {
                                            Box(contentAlignment = Alignment.Center){
                                                GlideImage(
                                                    model = if(data.brawler[i1+1].rank.toInt()<=4) R.drawable.rank_4
                                                    else if(data.brawler[i1+1].rank.toInt()<=9) R.drawable.rank_9
                                                    else if(data.brawler[i1+1].rank.toInt()<=14) R.drawable.rank_14
                                                    else if(data.brawler[i1+1].rank.toInt()<=19) R.drawable.rank_19
                                                    else if(data.brawler[i1+1].rank.toInt()<=24) R.drawable.rank_24
                                                    else if(data.brawler[i1+1].rank.toInt()<=29) R.drawable.rank_29
                                                    else if(data.brawler[i1+1].rank.toInt()<=34) R.drawable.rank_34
                                                    else R.drawable.rank_35 , contentDescription = null,
                                                    modifier = Modifier.size(23.dp))
                                                Column() {
                                                    Spacer(modifier = Modifier.height(6.dp))
                                                    Text(text = data.brawler[i1+1].rank, color = White2, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.rotate(-4f))
                                                }
                                            }
                                            Text(text = data.brawler[i1+1].name, color = White2 , fontWeight = FontWeight.Bold, fontSize = 14.sp)

                                        }
                                        Image(painter = painterResource(id = R.drawable.dowtriangle), contentDescription = null , modifier = Modifier.height(23.dp))
                                    }

                                }
                                Row(Modifier.align(Alignment.BottomEnd)) {
                                    Image(painter = painterResource(id = R.drawable.uptriangle), contentDescription = null , modifier = Modifier.height(22.dp))
                                    Row(
                                        Modifier
                                            .height(22.dp)
                                            .background(Color.Black)
                                            .padding(vertical = 2.dp)
                                    ) {
                                        for (i in data.brawler[i1+1].unlocked){
                                            GlideImage(model = i, contentDescription = null, modifier = Modifier.size(18.dp))
                                            Spacer(modifier = Modifier.width(2.dp))
                                        }
                                    }
                                }
                            }
                            Row(
                                Modifier
                                    .background(Color.Black)
                                    .fillMaxWidth()
                                    .height(40.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ){
                                Column{
                                    Text(text = "Level", color = White2, fontSize = 10.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(painter = painterResource(id = R.drawable.plevel), contentDescription =null, modifier = Modifier.size(25.dp))
                                        Text(text = data.brawler[i1+1].level, color = White2, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Column{
                                    Text(text = "Current", color = White2, fontSize = 10.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(painter = painterResource(id = R.drawable.ht), contentDescription =null, modifier = Modifier.size(25.dp))
                                        Text(text = data.brawler[i1+1].currTrophy, color = White2, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                                Column{
                                    Text(text = "Highest", color = White2, fontSize = 10.sp)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(painter = painterResource(id = R.drawable.ht), contentDescription =null, modifier = Modifier.size(25.dp))
                                        Text(text = data.brawler[i1+1].highTrophy, color = White2, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }

                        }
                    }
                }

            }
        }
        item{
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp), horizontalArrangement = Arrangement.End) {
                Text(text = if(expansion.c1list.value.id == 0) "More" else "Less", fontStyle = FontStyle.Italic,modifier=Modifier.clickable {
                    if(expansion.c1list.value.id == 0) expansion.c1list.value= ExpandedUnlockedBrawler(1)
                    else expansion.c1list.value= ExpandedUnlockedBrawler(0)
                })
            }
        }

        item{
            Row(Modifier.padding(horizontal = 14.dp, vertical = 4.dp)) {
                Text(text = "Previous Clubs", fontWeight = FontWeight.Bold)
            }
        }
        items(data.prevClubs){

            Box(contentAlignment = Alignment.BottomCenter) {
                Row(modifier = Modifier.padding(horizontal = 14.dp)) {

                    Box(
                        modifier = Modifier
                            .height(40.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Row(modifier = Modifier) {
                                Spacer(modifier = Modifier.width(35.dp))
                                Text(text = it.clubNane, fontWeight = FontWeight.Bold)
                            }
                            Row() {
                                Text(
                                    text = it.joinDate + "  ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Image(painter = painterResource(id = R.drawable.arrowclub), contentDescription = null, modifier = Modifier.size(20.dp))
                                Text(
                                    text = it.leaveDate + "  ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
                GlideImage(model = it.clubBanner, contentDescription = "", modifier = Modifier
                    .size(45.dp)
                    .align(Alignment.CenterStart))
            }
        }

    }
}