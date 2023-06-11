package com.kal.brawlstatz2.presentation
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestListener
import com.kal.brawlstatz2.R
import com.kal.brawlstatz2.data.ExpandableCardModel
import com.kal.brawlstatz2.data.events.Active
import com.kal.brawlstatz2.viewmodel.MainViewModel
import com.kal.brawlstatz2.viewmodel.MapCardViewModel
import java.util.Calendar
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun Curr(value: List<Active>, viewModel: MainViewModel ,m:Int) {
    val cardModel1 = viewModel<MapCardViewModel>()
    var enlarged by remember {
        mutableStateOf("")
    }

    LazyColumn(
        Modifier.fillMaxSize()
    ){
        items(value){
            MapCard(active = it,cardModel1,viewModel,m){t->
                enlarged=t
            }
        }
    }
    AnimatedVisibility(visible = enlarged!="" ,enter = fadeIn(), exit = fadeOut()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)))
    }
    AnimatedVisibility(visible = enlarged!="", enter = scaleIn()+ fadeIn(), exit = scaleOut()+ fadeOut()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    enlarged = ""
                },
            contentAlignment = Alignment.Center
        ) {
            Card() {
                GlideImage(model = enlarged, contentDescription = null, Modifier.clickable {})
            }
        }
    }

}
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MapCard(
    active: Active,
    cardModel: MapCardViewModel,
    viewModel: MainViewModel,
    m: Int,
    onclick: (String) -> Unit
) {
    val cardId = active.map.name
    val cardHeight: Dp = 92.dp
    val isExp = (cardModel.currmaplist.value.isExpanded && cardModel.currmaplist.value.id==cardId )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 6.dp, end = 6.dp, top = 3.dp)
            .border(
                width = 2.dp,
                color = Color(active.map.gameMode.color.toColorInt()),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(12.dp)),
        onClick = {
            if (isExp) cardModel.currmaplist.value = ExpandableCardModel(cardId, false)
            else cardModel.currmaplist.value = ExpandableCardModel(cardId, true)
        },
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(Color(0xFF111010)),
    ){
        Column {
            Box(modifier = Modifier.height(100.dp)){
                Column{
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.48f)
                            .background(Color(active.map.gameMode.color.toColorInt()))
                            .padding(4.dp)
                    ){
                        Row {
                            GlideImage(model = active.map.gameMode.imageUrl, contentDescription = null,
                                Modifier
                                    .aspectRatio(1f)
                                    .fillMaxHeight())
                            Column {
                                Box(
                                    Modifier
                                        .height(22.dp)
                                        .offset(y = (-4).dp), contentAlignment = Alignment.BottomCenter){
                                    Text(text = active.map.gameMode.name,textAlign = TextAlign.Center , color = Color.White, fontSize = 20.sp, style = MaterialTheme.typography.bodyMedium+ TextStyle(
                                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 1f),
                                        textIndent = TextIndent(0.sp)
                                    )
                                    )
                                }
                                Text(text = active.map.name+" ", textAlign = TextAlign.Center , color = Color.White,fontSize = 14.sp, style = MaterialTheme.typography.bodyMedium+ TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 1f),
                                    textIndent = TextIndent(0.sp)
                                ),modifier = Modifier
                                    .height(22.dp)
                                    .offset(y = (-4).dp)
                                )
                            }
                            

                        }

                    }
                    Divider()
                    Box(){
                        val time = if(m==0) active.endTime else active.startTime
                        val year = time.substring(0,4).toInt()
                        val date = time.substring(8,10).toInt()
                        val month = time.substring(5,7).toInt()-1
                        val hrs = time.substring(11,13).toInt()
                        val min = time.substring(14,16).toInt()
                        val sec = time.substring(17,19).toInt()
                        val timeStamp: String = java.lang.String.valueOf(
                            TimeUnit.MILLISECONDS.toSeconds(
                                System.currentTimeMillis()
                            )
                        )
                        val currtime : Long = timeStamp.toLong()*1000
                        GlideImage(model = active.map.environment.imageUrl, contentDescription = null,Modifier.fillMaxWidth()){
                            it.centerCrop()
                        }
                        val cal: Calendar = Calendar.getInstance()
                        cal.set(year, month, date,hrs,min,sec)
                        val millis: Long = cal.timeInMillis+19800000

                        val difTime = millis.minus(currtime)

                        val dayLeft = difTime/86400000;
                        val hourLeft = (difTime%86400000)/3600000
                        val minLeft = ((difTime%86400000)%3600000)/60000
                        Row(modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                            ImageAsync2(string = "https://firebasestorage.googleapis.com/v0/b/brawlstatz2-7dd0c.appspot.com/o/traits%2Fsupertime.png?alt=media")

                            Text(text = "${dayLeft}d ${hourLeft}h ${minLeft}m" , style=MaterialTheme.typography.bodyMedium + TextStyle(
                                shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                textIndent = TextIndent(0.sp),
                            ), color = Color.White,modifier=Modifier.background(Color.Gray.copy(alpha = 0.4f)))
                        }

                    }
                    
                }
            }
            if(isExp){
                Box(modifier = Modifier
                    .height(200.dp)
                    .background(color = Color(active.map.gameMode.color.toColorInt()).copy(alpha = 0.25f))){
                    Row() {
                        GlideImage(model = active.map.imageUrl, contentDescription = null, modifier = Modifier.clickable{
                            onclick(active.map.imageUrl)
                        }){
                            it.placeholder(R.drawable.mapthumbnail)
                        }
                        Box(modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(), contentAlignment = Alignment.Center){
                            Text(
                                text = "WIN RATE", color = Color.White,
                                style = MaterialTheme.typography.bodyMedium + TextStyle(
                                    shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                    textIndent = TextIndent(0.sp),
                                ),
                                modifier = Modifier.offset(y = (-52).dp)
                            )
                            Column() {
                            Row {
                                for (i in 0..3){
                                    val brawler = viewModel.blist.value.find { it.id!! == active.map.stats[i].brawler }
                                    Box {
                                        ImageAsync(brawler?.bpro.toString(), placeholder = R.drawable.placeholder1, modifier = Modifier.size(40.dp))
                                        Text(text = "${active.map.stats[i].winRate.toInt()}%", color = Color.White, style = MaterialTheme.typography.bodyMedium+ TextStyle(
                                            shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                            textIndent = TextIndent(0.sp),
                                        ), modifier = Modifier
                                            .width(40.dp)
                                            .offset(
                                                (-4).dp,
                                                (-6).dp
                                            ))
                                    }
                                    Spacer(modifier = Modifier.width(2.dp))
                                }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row() {
                                    for (i in 4..7){
                                        val brawler = viewModel.blist.value.find { it.id!! == active.map.stats[i].brawler }
                                        Box {
                                            ImageAsync(brawler?.bpro.toString(), placeholder = R.drawable.placeholder1, modifier = Modifier.size(40.dp))
                                            Text(text = "${active.map.stats[i].winRate.toInt()}%", color = Color.White, style = MaterialTheme.typography.bodyMedium+ TextStyle(
                                                shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                                                textIndent = TextIndent(0.sp),
                                            ), modifier = Modifier
                                                .width(40.dp)
                                                .offset(
                                                    (-4).dp,
                                                    (-6).dp
                                                ))
                                        }
                                        Spacer(modifier = Modifier.width(2.dp))
                                    }
                                }

                            }
                        }
                    }
                }
            }
        }

    }
}