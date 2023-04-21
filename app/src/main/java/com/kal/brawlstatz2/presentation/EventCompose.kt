package com.kal.brawlstatz2.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.kal.brawlstatz2.data.ExpandableCardModel
import com.kal.brawlstatz2.data.events.Active

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun MapCard(active:Active) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(start = 6.dp, end = 6.dp, top = 3.dp)
            .border(
                width = 2.dp,
                color = Color(active.map.gameMode.color.toColorInt()),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(Color(0xFF111010)),
    ){
        Column() {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.48f)
                    .background(Color(active.map.gameMode.color.toColorInt()))
                    .padding(4.dp)
            ){
              Row() {
                  GlideImage(model = active.map.gameMode.imageUrl, contentDescription = null,
                      Modifier
                          .aspectRatio(1f)
                          .fillMaxHeight())
                  Column() {
                     Box(Modifier.height(22.dp).offset(y=(-4).dp), contentAlignment = Alignment.BottomCenter){
                         Text(text = active.map.gameMode.name,textAlign = TextAlign.Center , fontSize = 20.sp, style = MaterialTheme.typography.bodyMedium+ TextStyle(
                             shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 1f),
                             textIndent = TextIndent(0.sp)
                         )
                         )
                     }
                      Text(text = active.map.name+" ", textAlign = TextAlign.Center ,fontSize = 14.sp, style = MaterialTheme.typography.bodyMedium+ TextStyle(
                          shadow = Shadow(offset = Offset(1f, 1f), blurRadius = 1f),
                          textIndent = TextIndent(0.sp)
                      ),modifier = Modifier.height(22.dp).offset(y=(-4).dp)
                      )
                  }
              }
            }
            Divider()
            GlideImage(model = active.map.environment.imageUrl, contentDescription = null,Modifier.fillMaxWidth()){
                it.centerCrop()
            }
        }
    }
}