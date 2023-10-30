package com.kal.brawlstatz2.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kal.brawlstatz2.R
import com.kal.brawlstatz2.ui.theme.White2
import com.kal.brawlstatz2.viewmodel.MainViewModel


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SetTrackerData(viewModel: MainViewModel) {
    val data = viewModel.tracker.value
    LazyColumn(Modifier.padding(8.dp)){
        item{
            Card(modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
                shape = RoundedCornerShape(bottomEnd = 20.dp, topStart = 20.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
            ){
                Box(){

                    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)) {
                        Column() {
                            Row(verticalAlignment = Alignment.CenterVertically) {

                                Column() {
                                    GlideImage(model = data.dp, contentDescription = null,Modifier.size(100.dp))
                                }

                                Spacer(modifier = Modifier.width(4.dp))
                                Column() {

                                    Text(text = data.name, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        GlideImage(model = R.drawable.club, contentDescription = null,
                                            Modifier
                                                .size(18.dp)
                                                .offset(0.dp, 1.dp))
                                        Text(text = data.player_club.clubNane, fontSize =12.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(contentAlignment = Alignment.CenterStart){
                                            Row(
                                                Modifier.padding(4.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.CenterStart){
                                                    Box(contentAlignment = Alignment.Center){
                                                        Image(painter = painterResource(id = R.drawable.level), contentDescription = null, modifier = Modifier.size(40.dp))
                                                        Text(text = data.xp.level, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                                    }
                                                }
                                            }
                                        }

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
            Row(Modifier.padding(horizontal = 14.dp, vertical = 4.dp)) {
                Text(text = "Graph", fontWeight = FontWeight.Bold)
            }
        }
        item{
            TrophyGraph(data.trophyArray)
        }

        item{
            Row(Modifier.padding(horizontal = 14.dp, vertical = 4.dp)) {
                Text(text = "Stats", fontWeight = FontWeight.Bold)
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
                   Image(painter = painterResource(id = R.drawable.highesttrophies), contentDescription = null, modifier = Modifier
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
                                   Text(text = "Current Trophies", fontWeight = FontWeight.Bold)
                               }
                               Text(text = data.trophy.progress+"  ", fontWeight = FontWeight.Bold)
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
                   Image(painter = painterResource(id = R.drawable.set), contentDescription = null, modifier = Modifier
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
                                   Text(text = "Season End Reward", fontWeight = FontWeight.Bold)
                               }
                               Text(text = data.trophy.seasonEndReward+"  ", fontWeight = FontWeight.Bold)
                           }
                       }
                   }
                   Image(painter = painterResource(id = R.drawable.bling), contentDescription = null, modifier = Modifier
                       .align(
                           Alignment.CenterStart
                       )
                       .size(45.dp)
                       .padding(0.dp, 4.dp))

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
                                   Text(text = "Max Brawlers", fontWeight = FontWeight.Bold)
                               }
                               Text(text = data.maxBrawler.toString()+"  ", fontWeight = FontWeight.Bold)
                           }
                       }
                   }
                   Image(painter = painterResource(id = R.drawable.p11), contentDescription = null, modifier = Modifier
                       .align(
                           Alignment.CenterStart
                       )
                       .size(45.dp)
                       .padding(0.dp, 4.dp))

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
//                Box(contentAlignment = Alignment.BottomCenter) {
//                    Row(modifier = Modifier.padding(horizontal = 14.dp)) {
//
//                        Box(
//                            modifier = Modifier
//                                .height(40.dp)
//                                .fillMaxWidth()
//                                .background(MaterialTheme.colorScheme.primaryContainer),
//                            contentAlignment = Alignment.CenterStart
//                        ) {
//                            Row(
//                                horizontalArrangement = Arrangement.SpaceBetween,
//                                modifier = Modifier.fillMaxWidth()
//                            ) {
//                                Row() {
//                                    Spacer(modifier = Modifier.width(35.dp))
//                                    Text(text = "Most Challenge Wins", fontWeight = FontWeight.Bold)
//                                }
//                                Text(
//                                    text = data.victories.challenge + "  ",
//                                    fontWeight = FontWeight.Bold
//                                )
//                            }
//                        }
//                    }
//                    Image(
//                        painter = painterResource(id = R.drawable.challenges),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .align(
//                                Alignment.CenterStart
//                            )
//                            .size(45.dp)
//                    )
//                }

            }
        }
        item{
            Row(Modifier.padding(horizontal = 14.dp, vertical = 4.dp)) {
                Text(text = "Upgrades", fontWeight = FontWeight.Bold)
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
                                    Text(text = "Gadgets", fontWeight = FontWeight.Bold)
                                }
                                Text(text = data.gadgets+"  ", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Image(painter = painterResource(id = R.drawable.gd), contentDescription = null, modifier = Modifier
                        .align(
                            Alignment.CenterStart
                        )
                        .size(45.dp)
                        .padding(0.dp, 4.dp))

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
                                    Text(text = "Star Powers", fontWeight = FontWeight.Bold)
                                }
                                Text(text = data.starpowers+"  ", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Image(painter = painterResource(id = R.drawable.sd), contentDescription = null, modifier = Modifier
                        .align(
                            Alignment.CenterStart
                        )
                        .size(45.dp)
                        .padding(0.dp, 4.dp))

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
                                    Text(text = "Gears", fontWeight = FontWeight.Bold)
                                }
                                Text(text = data.gears+"  ", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Image(painter = painterResource(id = R.drawable.gears), contentDescription = null, modifier = Modifier
                        .align(
                            Alignment.CenterStart
                        )
                        .size(45.dp)
                        .padding(0.dp, 4.dp))

                }
            }
        }

        item{
            Row(Modifier.padding(horizontal = 14.dp, vertical = 4.dp)) {
                Text(text = "Last ${data.bloss+data.bwin+data.bdraw} Battles", fontWeight = FontWeight.Bold)
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
                    Text(text ="W"+data.bwin, fontWeight = FontWeight.Bold, color = Color.Green)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "L"+data.bloss, fontWeight = FontWeight.Bold, color = Color.Red)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "D"+(data.bdraw), fontWeight = FontWeight.Bold, color = Color(0xFF00a3ff))
                }
            }
        }
        item{

            Row(Modifier.padding(horizontal = 14.dp, vertical = 4.dp)) {
                Text(text = "Brawlers (${data.brawler.size}/${viewModel.size})", fontWeight = FontWeight.Bold)

            }

        }
        item{
            LazyRow(){
                items(data.brawler) {brawler->
                    Column (modifier = Modifier
                        .padding(2.dp)
                        .width(180.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f))
                        .shadow(
                            1.dp,
                            RectangleShape, spotColor = Color.White
                        ), verticalArrangement = Arrangement.Center){
                        Box(modifier = Modifier
                            .fillMaxSize()
                            .border(2.dp, Color.Black)
                            .padding(4.dp)){
                            GlideImage(model = brawler.pro, contentDescription = null, modifier = Modifier
                                .size(70.dp)
                                .border(2.dp, color = Color.Black)){
                                it.placeholder(R.drawable.placeholder1)
                            }
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.offset((-4).dp,(-2).dp
                            )){
                                GlideImage(
                                    model = brawler.rank , contentDescription = null,
                                    modifier = Modifier.size(23.dp))
                            }
                            Column(modifier = Modifier.align(Alignment.BottomEnd), horizontalAlignment = Alignment.End) {
                                LazyRow {
                                    items(brawler.gears){url->
                                        GlideImage(model = url, contentDescription = null, modifier = Modifier
                                            .size(18.dp)
                                            .padding(1.dp)
                                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                                            .padding(1.dp))
                                    }
                                }

                                LazyRow {
                                    items(brawler.gdst){url->
                                        GlideImage(model = url, contentDescription = null, modifier = Modifier
                                            .size(18.dp)
                                            .padding(1.dp)
                                            .background(Color.Black.copy(alpha = 0.4f), CircleShape)
                                            .padding(1.dp))
                                    }
                                }
                            }
                            Text(text = brawler.name, fontSize = 20.sp,fontWeight = FontWeight.Bold, modifier = Modifier.align(
                                Alignment.TopEnd))
                        }
                        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color.Black
                            )){
                            Column {
                                Text(text = "LEVEL", color = Color.White, fontSize = 14.sp)
                                Row (verticalAlignment = Alignment.CenterVertically){
                                    Image(painter = painterResource(id = R.drawable.plevel), contentDescription = null, modifier = Modifier.size(20.dp))
                                    Text(text = brawler.level, color = Color.White, fontSize = 14.sp)
                                }
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(text = "CURRENT", color = Color.White, fontSize = 14.sp, textAlign = TextAlign.Center)
                                Row  (verticalAlignment = Alignment.CenterVertically){
                                    Image(painter = painterResource(id = R.drawable.ht), contentDescription = null, modifier = Modifier.size(20.dp))
                                    Text(text = buildAnnotatedString {
                                        withStyle(style = SpanStyle(fontSize = 14.sp, color = Color.White)){
                                            append(brawler.currTrophy)
                                        }
                                        withStyle(style = SpanStyle(fontSize = 10.sp, color = Color.White)){
                                            append("("+brawler.minusTrophy+")")
                                        }
                                    })
                                }
                            }
                            Column {
                                Text(text = "HIGHEST", color = Color.White, fontSize = 14.sp)
                                Row  (verticalAlignment = Alignment.CenterVertically){
                                    Image(painter = painterResource(id = R.drawable.ht), contentDescription = null, modifier = Modifier.size(20.dp))
                                    Text(text = brawler.highTrophy, color = Color.White, fontSize = 14.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}