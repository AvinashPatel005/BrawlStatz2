package com.kal.brawlstatz2.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import com.kal.brawlstatz2.data.MetaTier
import com.kal.brawlstatz2.viewmodel.MetaViewModel

@Composable
fun ShowMetaList(nestedList: List<MetaTier> , sortedMetaList : ArrayList<Brawler>) {
    val cardModel = viewModel<MetaViewModel>()
    LazyColumn() {
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
        for(sublist in nestedList) {
            item {
                Surface(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                    color = sublist.color,
                ) {
                    Surface(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 2.dp, end = 2.dp, top = 2.dp)
                        .clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
                        color = Color(0xFF111010),
                    ){
                        Row(
                            Modifier
                                .fillMaxSize()
                                .padding(start = 4.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = sublist.Tname,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                                fontStyle = FontStyle.Italic,
                                color = sublist.color

                            )
                            Text(
                                text = "TIER",
                                fontSize = 8.sp,
                                fontStyle = FontStyle.Italic,
                                color = Color.White
                            )
                        }
                    }
                }
            }
            items(sublist.Tier.size) {
                MetaCard(brawler = sublist.Tier[it], sortedMetaList.indexOf(sublist.Tier[it]), sublist.color , cardModel)
            }
            item {
                Surface(modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .height(10.dp)
                    .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)),
                    color = sublist.color,
                ) {
                    Surface(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 2.dp, end = 2.dp, bottom = 2.dp)
                        .height(10.dp)
                        .clip(RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)),
                        color = Color(0xFF111010),
                    ){

                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}



@Composable
fun MetaCard(
    brawler: Brawler, i: Int, color: Color, cardModel: MetaViewModel
){
    val cardHeight : Dp = 86.dp
    val isVisible = (cardModel.mclist.value.isExpanded && cardModel.mclist.value.id == brawler.bname)
    var clicked by remember {
        mutableStateOf(0)
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
            .height(cardHeight),
        color = color
    ){
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 2.dp, end = 2.dp)
                .height(cardHeight),
            color = Color(0xFF111010)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(cardHeight)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(cardHeight)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                    ) {
                        brawler.bpro?.let {
                            ImageAsync(
                                url = it,
                                placeholder = R.drawable.placeholder1,
                                modifier = Modifier.size(80.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Spacer(modifier = Modifier.height(5.dp))
                            brawler.bname?.let {
                                Text(
                                    text = it,
                                    fontSize = 19.sp,
                                    color = brawler.color,
                                    fontWeight = FontWeight.Bold,
                                    style = TextStyle(
                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 1f),
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(0.dp))
                            Row(
                                modifier = Modifier,
                            ) {
                                Column() {
                                    Image(
                                        painter = painterResource(
                                            id = if (brawler.bstarpower.equals("1")) R.drawable.s1 else if (brawler.bstarpower.equals("2")
                                            ) R.drawable.s2 else R.drawable.sd
                                        ), contentDescription = null,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clickable {
                                                cardModel.mclist.value =
                                                    ExpandableCardModel(
                                                        brawler.bname.toString(),
                                                        true
                                                    )
                                                clicked = 1
                                            },
                                    )
                                    if(clicked==1&&isVisible)
                                        Canvas(modifier = Modifier.size(20.dp,5.dp), onDraw = {
                                            drawCircle(Color.White,4f)
                                        })
                                }

                                Spacer(modifier = Modifier.width(2.dp))
                                Column {
                                    Image(
                                        painter = painterResource(
                                            id = if (brawler.bgadget.equals("1")) R.drawable.g1 else if (brawler.bgadget.equals(
                                                    "2"
                                                )
                                            ) R.drawable.g2 else R.drawable.gd
                                        ), contentDescription = null, modifier = Modifier
                                            .size(20.dp)
                                            .clickable {
                                                cardModel.mclist.value =
                                                    ExpandableCardModel(
                                                        brawler.bname.toString(),
                                                        true
                                                    )
                                                clicked = 2
                                            },
                                    )
                                    if(clicked==2&&isVisible)
                                        Canvas(modifier = Modifier.size(20.dp,5.dp), onDraw = {
                                            drawCircle(Color.White,4f)
                                        })
                                }

                                Spacer(modifier = Modifier.width(8.dp))
                                brawler.bgear1?.let { ImageAsync2(it) }
                                brawler.bgear2?.let { ImageAsync2(it) }
                                brawler.bgear3?.let { ImageAsync2(it) }
                            }
                        }

                    }
                    val t: String
                    var hide = false
                    when(clicked){
                        1->{
                            t = if(brawler.bstarpower.equals("1")) brawler.s1t.toString() else brawler.s2t.toString()
                        }
                        2->{
                            t = if(brawler.bgadget.equals("1")) brawler.g1t.toString() else brawler.g2t.toString()
                        }
                        else -> {
                            t = ""
                            hide =true
                        }
                    }
                    HelperBox2(hide = (!hide)&&isVisible, helperText = t)
                    if(color!= Color.Cyan) Text(
                        text = "#" + (i + 1).toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        style = TextStyle(
                            shadow = Shadow(offset = Offset(4f, 1f), blurRadius = 1f)
                        ),
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun HelperBox2(
    hide: Boolean,
    helperText: String,
    modifier: Modifier=Modifier
) {
    AnimatedVisibility(visible = hide , enter = fadeIn()) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
        ) {
            Box(
                modifier = modifier
                    .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(horizontal = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = helperText,
                    fontSize = 9.sp,
                    fontStyle = FontStyle.Italic,
                    style = TextStyle(
                        textIndent = TextIndent(0.sp),
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageAsync2(string:String,modifier: Modifier=Modifier) {
    GlideImage(model = string, contentDescription = null,modifier = modifier
        .size(20.dp)
        .padding(end = 2.dp))
}
