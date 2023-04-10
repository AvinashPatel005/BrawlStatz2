package com.kal.brawlstatz2.presentation

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kal.brawlstatz2.R
import com.kal.brawlstatz2.data.Brawler
import com.kal.brawlstatz2.data.ExpandableCardModel
import com.kal.brawlstatz2.viewmodel.CardsViewModel
import com.kal.brawlstatz2.viewmodel.MainViewModel
import kotlin.math.log

@Composable
fun ShowBrawlersList(brawler: List<Brawler>, isSearching: Boolean) {
    val cardModel = viewModel<CardsViewModel>()
    val viewModel = viewModel<MainViewModel>()
    if(isSearching&&brawler.isNotEmpty()) cardModel.c1list.value = ExpandableCardModel(brawler[0].bname,true)
    else cardModel.c1list.value = ExpandableCardModel(null,false)
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ){
        items(brawler){
            BrawlerCard(brawler = it,cardModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrawlerCard(
    brawler: Brawler,
    CardModel: CardsViewModel
){
    val cardId = brawler.bname
    val cardHeight : Dp = 92.dp

    val isExp = (CardModel.c1list.value.isExpanded&& CardModel.c1list.value.id==brawler.bname)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp, end = 6.dp, top = 4.dp)
            .border(width = 2.dp, color = brawler.color, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(Color(0xFF111010)),
        onClick = {
            if(isExp)  {
                CardModel.c1list.value = ExpandableCardModel(cardId,false)
            }
            else  {
                CardModel.c1list.value = ExpandableCardModel(cardId,true)
            }

        }

    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Column {
                Row(
                    modifier= Modifier
                        .fillMaxWidth()
                        .height(if (isExp) 114.dp else cardHeight)
                        .padding(
                            top = 4.dp,
                            end = 4.dp,
                            start = 4.dp,
                            bottom = if (isExp) 0.dp else 4.dp
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Row(
                    ) {
                        brawler.bpro?.let { ImageAsync(url = it,placeholder = com.kal.brawlstatz2.R.drawable.placeholder1,if(isExp) 110.dp else 86.dp) }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            brawler.bname?.let {
                                Text(
                                    text = it,
                                    fontSize = 19.sp,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    style = TextStyle(
                                        shadow = Shadow(offset = Offset(1f,1f), blurRadius = 1f),
                                    )
                                )
                            }
                            brawler.brare?.let {
                                Text(
                                    text = it,
                                    fontSize = 10.sp,
                                    fontStyle = FontStyle.Italic,
                                    color = brawler.color,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    style = TextStyle(
                                        shadow = Shadow(offset = Offset(1f,1f), blurRadius = 1f)
                                    )
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.align(Alignment.Bottom),
                    ) {
                        Column {
                            brawler.c1?.let { ImageAsync1(url = it,placeholder = com.kal.brawlstatz2.R.drawable.placeholder2) }
                            if(isExp) brawler.c1n?.let { Text(text = it, fontSize = 8.sp, textAlign = TextAlign.Center,modifier = Modifier.width(43.dp), color = Color.White, fontStyle = FontStyle.Italic)}
                        }
                        Spacer(modifier = Modifier.width(2.dp))
                        Column {
                            brawler.c2?.let { ImageAsync1(url = it,placeholder = com.kal.brawlstatz2.R.drawable.placeholder1) }
                            if(isExp) brawler.c2n?.let { Text(text = it, fontSize = 8.sp, textAlign = TextAlign.Center,modifier = Modifier.width(43.dp), color = Color.White, fontStyle = FontStyle.Italic) }
                        }
                        Spacer(modifier = Modifier.width(2.dp))
                        Column {
                            brawler.c3?.let { ImageAsync1(url = it,placeholder = com.kal.brawlstatz2.R.drawable.placeholder3) }
                            if(isExp) brawler.c3n?.let { Text(text = it, fontSize = 8.sp, textAlign = TextAlign.Center,modifier = Modifier.width(43.dp), color = Color.White, fontStyle = FontStyle.Italic) }
                        }

                    }
                }

                if(isExp){
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp)
                    ){

                        Column {
                            var clicked by remember {
                                mutableStateOf(0)
                            }
                            Text(text = "ABOUT", fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 35.dp), style = TextStyle(
                                textIndent = TextIndent(0.sp),
                                color = Color.White
                            ))
                            Box(modifier = Modifier
                                .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxWidth()
                                .padding(vertical = 2.dp, horizontal = 4.dp)
                            ){
                                Text(
                                    text = brawler.babout.toString(),
                                    fontSize = 10.sp,
                                    fontStyle = FontStyle.Italic,
                                    style = TextStyle(
                                        textIndent = TextIndent(0.sp),
                                        textAlign = TextAlign.Center,
                                        color = Color.White
                                    ),
                                    modifier = Modifier
                                )
                            }
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(74.dp)
                                    ){
                                Box(modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.5f),
                                    contentAlignment = Alignment.CenterStart
                                ){

                                    Column(
                                    ) {
                                        Box {
                                            Box(
                                                modifier = Modifier.height(35.dp),
                                                contentAlignment = Alignment.BottomStart
                                            ){
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(25.dp)
                                                        .background(Color.Gray).clickable {
                                                            clicked = 5
                                                        }
                                                )
                                            }
                                            Column(
                                                modifier = Modifier.height(35.dp).padding(start = 43.dp),
                                            ) {
                                                Text("ATTACK", fontSize = 12.sp, color = Color(0xfffd9798), style = MaterialTheme.typography.bodyMedium+ TextStyle(
                                                    shadow =  Shadow(offset = Offset(1f,1f), blurRadius = 1f)) )
                                                Text(brawler.battack.toString().uppercase(), fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold ,style = MaterialTheme.typography.bodyMedium , modifier = Modifier.offset(y = (-4).dp))
                                            }
                                            Image(painter = painterResource(id = R.drawable.attack), contentDescription = null, modifier = Modifier
                                                .size(35.dp)
                                                .background(Color.Transparent))
                                            if(clicked==5){
                                                Image(painter = painterResource(id = R.drawable.atacksuperborder), contentDescription = null, modifier = Modifier
                                                    .size(35.dp)
                                                    .background(Color.Transparent))

                                            }
                                        }
                                        Box {
                                            Box(
                                                modifier = Modifier.height(35.dp),
                                                contentAlignment = Alignment.BottomStart
                                            ){
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(25.dp)
                                                        .background(Color.Gray).clickable {
                                                            clicked = 6
                                                        }
                                                )
                                            }
                                            Column(
                                                modifier = Modifier.height(35.dp).padding(start = 43.dp),
                                            ) {
                                                Text("SUPER", fontSize = 12.sp, color = Color(0xffffc11d), style = MaterialTheme.typography.bodyMedium+ TextStyle(
                                                    shadow =  Shadow(offset = Offset(1f,1f), blurRadius = 1f)) )
                                                Text(brawler.bsuper.toString().uppercase(), fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold ,style = MaterialTheme.typography.bodyMedium, modifier = Modifier.offset(y = (-4).dp))
                                            }
                                            Image(painter = painterResource(id = R.drawable.mainsuper), contentDescription = null, modifier = Modifier
                                                .size(35.dp)
                                                .background(Color.Transparent))
                                            if(clicked==6){
                                                Image(painter = painterResource(id = R.drawable.atacksuperborder), contentDescription = null, modifier = Modifier
                                                    .size(35.dp)
                                                    .background(Color.Transparent))

                                            }
                                        }
                                    }

                                }
                                Box(modifier = Modifier
                                    .fillMaxSize(),
                                    contentAlignment = Alignment.BottomCenter
                                ){
                                    Row(
                                        modifier = Modifier.height(54.dp)
                                    ) {

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            brawler.g1?.let { ImageAsync2(string = it, modifier = Modifier
                                                .size(40.dp)
                                                .clickable {
                                                    clicked = 1
                                                }) }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            if(clicked==1)
                                            Image(painter = painterResource(id = R.drawable.arrow), contentDescription = null, modifier = Modifier.height(12.dp))
                                        }
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            brawler.g2?.let { ImageAsync2(string = it, modifier = Modifier
                                                .size(40.dp)
                                                .clickable {
                                                    clicked = 2
                                                }) }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            if(clicked==2)
                                            Image(painter = painterResource(id = R.drawable.arrow), contentDescription = null, modifier = Modifier.height(12.dp))
                                        }
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            brawler.s1?.let { ImageAsync2(string = it, modifier = Modifier
                                                .size(40.dp)
                                                .clickable {
                                                    clicked = 3
                                                }) }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            if(clicked==3)
                                            Image(painter = painterResource(id = R.drawable.arrow), contentDescription = null, modifier = Modifier.height(12.dp))
                                        }
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            brawler.s2?.let { ImageAsync2(string = it, modifier = Modifier
                                                .size(40.dp)
                                                .clickable {
                                                    clicked = 4
                                                }) }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            if(clicked==4)
                                            Image(painter = painterResource(id = R.drawable.arrow), contentDescription = null, modifier = Modifier.height(12.dp))
                                        }

                                    }
                                }

                            }
                            var hide by remember {
                                mutableStateOf(false)
                            }
                            var t = ""
                            when(clicked){
                                1->{
                                    t = brawler.g1t.toString()
                                    hide = false
                                }
                                2->{
                                    t = brawler.g2t.toString()
                                    hide = false
                                }
                                3->{
                                    t = brawler.s1t.toString()
                                    hide = false
                                }
                                4->{
                                    t = brawler.s2t.toString()
                                    hide = false
                                }
                                5->{
                                    t = brawler.battackt.toString()
                                    hide = false
                                }
                                6->{
                                    t = brawler.bsupert.toString()
                                    hide = false
                                }
                                else -> {
                                    t = ""
                                    hide = true
                                }
                            }
                            if(!hide){
                                Box(modifier = Modifier
                                    .fillMaxSize()){
                                    Box(modifier = Modifier
                                        .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(Color.White).padding(horizontal = 2.dp)
                                        .fillMaxWidth()
                                        .height(24.dp),
                                        contentAlignment = Alignment.Center
                                    ){
                                        Text(
                                            text = t,
                                            fontSize = 9.sp,
                                            fontStyle = FontStyle.Italic,
                                            style = TextStyle(
                                                textIndent = TextIndent(0.sp),
                                                textAlign = TextAlign.Center,
                                                color = Color.Black,
                                                fontWeight = FontWeight.Bold
                                            ),
                                        )
                                    }
                                }
                            }
                            else{
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .height(24.dp),
                                )
                            }
                        }

                    }
                }

            }
        }
        }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageAsync(url:String, placeholder : Int,size:Dp) {
    GlideImage(
        model = url,
        contentDescription = null,
        modifier = Modifier
            .size(size)
            .aspectRatio(1f)
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
    ){
        it.placeholder(placeholder).fitCenter()
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageAsync1(url:String, placeholder : Int) {
    GlideImage(
        model = url,
        contentDescription = null,
        modifier = Modifier
            .size(42.dp)
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
    ){
        it.placeholder(placeholder).fitCenter()
    }
}