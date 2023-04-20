package com.kal.brawlstatz2.presentation

import android.app.Activity
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
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

@Composable
fun BrawlersList(brawler: List<Brawler>, isSearching: Boolean, viewModel: MainViewModel) {
    val cardModel = viewModel<CardsViewModel>()
    if (isSearching && brawler.isNotEmpty()) cardModel.c1list.value =
        ExpandableCardModel(brawler[0].bname, true)
    else cardModel.c1list.value = ExpandableCardModel(null, false)
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(brawler) {
            BrawlerCard(brawler = it, cardModel,viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrawlerCard(
    brawler: Brawler,
    cardModel: CardsViewModel,
    viewModel: MainViewModel
) {
    val cardId = brawler.bname
    val cardHeight: Dp = 92.dp

    val isExp = (cardModel.c1list.value.isExpanded && cardModel.c1list.value.id == brawler.bname)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp, end = 6.dp, top = 3.dp)
            .border(width = 2.dp, color = brawler.color, shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(Color(0xFF111010)),
        onClick = {
            if (isExp) cardModel.c1list.value = ExpandableCardModel(cardId, false)
            else cardModel.c1list.value = ExpandableCardModel(cardId, true)
        }
    ){
        Box(
            modifier = Modifier.fillMaxWidth()
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
                ) {
                    Row {
                        ImageAsync(
                            url = brawler.bpro.toString(),
                            placeholder = R.drawable.placeholder1,
                            modifier = Modifier.size(if (isExp) 110.dp else 84.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = brawler.bname.toString(),
                                fontSize = 19.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 1f),
                                )
                            )
                            Text(
                                text = brawler.brare.toString(),
                                fontSize = 10.sp,
                                fontStyle = FontStyle.Italic,
                                color = brawler.color,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 1f)
                                )
                            )
                            val activity = (LocalContext.current as? Activity)
                            if(brawler.trait!="null") {
                                ImageAsync2(string = brawler.trait.toString(), modifier = Modifier
                                    .clickable {
                                        for (ts in viewModel.traits) {
                                            if (brawler.trait!!.contains(ts.tName)) Toast.makeText(activity,ts.tDis,Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                )
                            }
                        }
                    }
                    Row(
                        modifier = Modifier.align(Alignment.Bottom),
                    ) {
                        CounterColumn(
                            url = brawler.c1.toString(),
                            name = brawler.c1n.toString(),
                            placeholder = R.drawable.placeholder4,
                            isExp = isExp
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        CounterColumn(
                            url = brawler.c2.toString(),
                            name = brawler.c2n.toString(),
                            placeholder = R.drawable.placeholder3,
                            isExp = isExp
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        CounterColumn(
                            url = brawler.c3.toString(),
                            name = brawler.c3n.toString(),
                            placeholder = R.drawable.placeholder2,
                            isExp = isExp
                        )
                    }
                }

                //Expandable Part
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
                                ) {

                                    Column {
                                        BarPreview(
                                            moveName = brawler.battack.toString(),
                                            previewText = "ATTACK",
                                            previewIcon = R.drawable.attack,
                                            previewColor = Color(0xfffd9798),
                                            clicked = clicked == 5,
                                            onClick = {
                                                clicked = 5
                                            }
                                        )
                                        BarPreview(
                                            moveName = brawler.bsuper.toString(),
                                            previewText = "SUPER",
                                            previewIcon = R.drawable.mainsuper,
                                            previewColor = Color(0xffffc11d),
                                            clicked = clicked == 6,
                                            onClick = {
                                                clicked = 6
                                            }
                                        )
                                    }
                                }

                                Box(modifier = Modifier
                                    .fillMaxSize(),
                                    contentAlignment = Alignment.BottomCenter
                                ){
                                    Row(
                                        modifier = Modifier.height(54.dp)
                                    ) {
                                        StarGadget(
                                            url = brawler.g1.toString(),
                                            clicked = clicked == 1,
                                            onClick = {
                                                clicked = 1
                                            }
                                        )
                                        StarGadget(
                                            url = brawler.g2.toString(),
                                            clicked = clicked == 2,
                                            onClick = {
                                                clicked = 2
                                            }
                                        )
                                        StarGadget(
                                            url = brawler.s1.toString(),
                                            clicked = clicked == 3,
                                            onClick = {
                                                clicked = 3
                                            }
                                        )
                                        StarGadget(
                                            url = brawler.s2.toString(),
                                            clicked = clicked == 4,
                                            onClick = {
                                                clicked = 4
                                            }
                                        )
                                    }
                                }
                            }
                            var hide by remember {
                                mutableStateOf(false)
                            }
                            val t: String
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
                            HelperBox(hide = hide, helperText = t)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HelperBox(
    hide: Boolean,
    helperText: String
) {
    if (!hide) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .border(1.dp, Color.White, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .padding(horizontal = 2.dp)
                    .fillMaxWidth()
                    .height(24.dp),
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
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp),
        )
    }
}

@Composable
fun StarGadget(
    url: String,
    clicked: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageAsync2(string = url, modifier = Modifier
            .size(40.dp)
            .clickable {
                onClick()
            })
        Spacer(modifier = Modifier.height(2.dp))
        if (clicked) Image(
            painter = painterResource(id = R.drawable.arrow),
            contentDescription = null,
            modifier = Modifier.height(12.dp)
        )
    }
}

@Composable
fun BarPreview(
    moveName: String,
    previewText: String,
    previewIcon: Int,
    previewColor: Color,
    clicked: Boolean,
    onClick: () -> Unit
) {
    Box {
        Box(
            modifier = Modifier.height(35.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(25.dp)
                    .clip(
                        RoundedCornerShape(
                            topEnd = 4.dp,
                            bottomEnd = 4.dp
                        )
                    )
                    .background(Color(0xff262626))
                    .clickable {
                        onClick()
                    }
            )
        }
        Column(
            modifier = Modifier
                .height(35.dp)
                .padding(start = 43.dp),
        ) {
            Text(
                previewText,
                fontSize = 12.sp,
                color = previewColor,
                style = MaterialTheme.typography.bodyMedium + TextStyle(
                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 1f)
                )
            )
            Text(
                moveName.uppercase(),
                fontSize = 14.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.offset(y = (-4).dp)
            )
        }
        Image(
            painter = painterResource(id = previewIcon),
            contentDescription = null,
            modifier = Modifier
                .size(35.dp)
                .background(Color.Transparent)
        )
        if (clicked) {
            Image(
                painter = painterResource(id = R.drawable.atacksuperborder),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .background(Color.Transparent)
            )

        }
    }
}

@Composable
fun CounterColumn(url: String, name: String, placeholder: Int, isExp: Boolean) {
    Column {
        ImageAsync(url, placeholder, Modifier.size(42.dp))
        if (isExp) Text(
            text = name,
            fontSize = 8.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(43.dp),
            color = Color.White,
            fontStyle = FontStyle.Italic
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageAsync(url: String, placeholder: Int, modifier: Modifier = Modifier) {
    GlideImage(
        model = url,
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f)
            .border(width = 2.dp, color = Color.Black, shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp)),
    ) {
        it.placeholder(placeholder).fitCenter()
    }
}