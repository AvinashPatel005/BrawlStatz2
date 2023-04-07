package com.kal.brawlstatz2.presentation

import android.graphics.drawable.Drawable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kal.brawlstatz2.data.Brawler
import com.kal.brawlstatz2.ui.theme.BrawlStatz2Theme
import com.kal.brawlstatz2.ui.theme.legendary

@Composable
fun ShowBrawlersList(brawler: MutableList<Brawler>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ){
        items(brawler){
            BrawlerCard(brawler = it)
        }
    }
}

@Composable
fun BrawlerCard(
    brawler:Brawler
){
    val cardHeight : Dp = 92.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp, top = 4.dp)
            .height(cardHeight)
            .border(width = 2.dp, color = brawler.color, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(Color(0xFF022C65))
    ){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(cardHeight)
        ){

            Row(
                modifier= Modifier
                    .fillMaxWidth()
                    .height(cardHeight)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Row(
                ) {
                    brawler.bpro?.let { ImageAsync(url = it,placeholder = com.kal.brawlstatz2.R.drawable.placeholder1) }
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
                    brawler.c1?.let { ImageAsync1(url = it,placeholder = com.kal.brawlstatz2.R.drawable.placeholder2) }
                    Spacer(modifier = Modifier.width(1.dp))
                    brawler.c2?.let { ImageAsync1(url = it,placeholder = com.kal.brawlstatz2.R.drawable.placeholder3) }
                    Spacer(modifier = Modifier.width(1.dp))
                    brawler.c3?.let { ImageAsync1(url = it,placeholder = com.kal.brawlstatz2.R.drawable.placeholder4) }
                }
            }
        }
        }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageAsync(url:String, placeholder : Int) {
    GlideImage(
        model = url,
        contentDescription = null,
        modifier = Modifier
            .fillMaxHeight()
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
            .size(44.dp)
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
    ){
        it.placeholder(placeholder).fitCenter()
    }
}


@Preview(showBackground = true)
@Composable
fun CardPreview() {
    BrawlStatz2Theme {
        BrawlerCard(Brawler("AVINASH","MYTHIC","","","","","","","","","","","","","","","","","","","","","","",
            null,legendary))
    }
}