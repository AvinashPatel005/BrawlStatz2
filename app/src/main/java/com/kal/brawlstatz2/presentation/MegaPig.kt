package com.kal.brawlstatz2.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kal.brawlstatz2.R
import com.kal.brawlstatz2.viewmodel.MainViewModel
import java.util.concurrent.TimeUnit

@Composable
fun MegaPig(viewModel:MainViewModel) {
    val difTime = viewModel.megaPig.value.minus(viewModel.timeFromServer.value)
    val dayLeft = difTime / 86400000
    val hourLeft = (difTime % 86400000) / 3600000
    val minLeft = ((difTime % 86400000) % 3600000) / 60000
    if(dayLeft>=3){
        Box {
            Image(
                painter = painterResource(id = R.drawable.megapig),
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
                    text = "MEGA PIG",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                        textIndent = TextIndent(0.sp),
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.align(Alignment.TopStart)
                )
                Column(Modifier.align(Alignment.TopEnd), horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Starts in", style = MaterialTheme.typography.bodyMedium + TextStyle(
                            shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                            textIndent = TextIndent(0.sp),
                            color = Color.White
                        )
                    )
                    Text(
                        text = "${dayLeft-3}d ${hourLeft}h ${minLeft}m", modifier = Modifier
                            .background(Color.Gray.copy(alpha = 0.3f)),
                        style = MaterialTheme.typography.bodyMedium + TextStyle(
                            shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                            textIndent = TextIndent(0.sp),
                            color = Color.White
                        )
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.stardrop),
                    contentDescription = null,
                    modifier = Modifier
                        .size(68.dp)
                        .align(Alignment.BottomStart)
                )
                Text(
                    "Fill the Mega Pig with your club!", color = Color.White,
                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                        textIndent = TextIndent(0.sp),
                        fontSize = 14.sp
                    ), modifier = Modifier.align(Alignment.BottomEnd)
                )
            }
        }
    }
    else{
        Box {
            Image(
                painter = painterResource(id = R.drawable.megapig),
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
                Column (modifier = Modifier.align(Alignment.TopStart)){
                    Text(
                        text = "MEGA PIG",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium + TextStyle(
                            shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                            textIndent = TextIndent(0.sp),
                            fontSize = 20.sp
                        ),

                    )
                    Image(painter = painterResource(id = R.drawable.live), contentDescription = null , modifier = Modifier.width(40.dp))
                }
                Column(Modifier.align(Alignment.TopEnd), horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Ends in", style = MaterialTheme.typography.bodyMedium + TextStyle(
                            shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                            textIndent = TextIndent(0.sp),
                            color = Color.White
                        )
                    )
                    Text(
                        text = "${dayLeft}d ${hourLeft}h ${minLeft}m", modifier = Modifier
                            .background(Color.Gray.copy(alpha = 0.3f)),
                        style = MaterialTheme.typography.bodyMedium + TextStyle(
                            shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                            textIndent = TextIndent(0.sp),
                            color = if(dayLeft==0L)Color.Red else Color.White
                        )
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.stardrop),
                    contentDescription = null,
                    modifier = Modifier
                        .size(68.dp)
                        .align(Alignment.BottomStart)
                )
                Text(
                    "Fill the Mega Pig with your club!", color = Color.White,
                    style = MaterialTheme.typography.bodyMedium + TextStyle(
                        shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                        textIndent = TextIndent(0.sp),
                        fontSize = 14.sp
                    ), modifier = Modifier.align(Alignment.BottomEnd)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ct),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                    )
                    Text(
                        text = " x18", color = Color.White,
                        style = MaterialTheme.typography.bodyMedium + TextStyle(
                            shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 20f),
                            textIndent = TextIndent(0.sp),
                            fontSize = 20.sp
                        )
                    )
                }
            }
        }
    }
}