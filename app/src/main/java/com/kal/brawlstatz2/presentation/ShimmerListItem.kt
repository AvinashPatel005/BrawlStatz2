package com.kal.brawlstatz2.presentation

import android.widget.GridLayout
import android.widget.Space
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.GlideImage
import com.kal.brawlstatz2.R
import java.util.concurrent.TimeUnit

@Composable
fun ShimmerListItem(
    modifier: Modifier = Modifier
) {
        Row(modifier = modifier) {
            Box(modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(6.dp))
                .shimmerEffect())
            Spacer(modifier = Modifier.width(0.dp))
            Column (modifier.weight(1f)){
                Box(modifier= Modifier
                    .fillMaxWidth(0.6f)
                    .clip(RoundedCornerShape(4.dp))
                    .height(18.dp)
                    .shimmerEffect())
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier= Modifier
                    .fillMaxWidth(0.4f)
                    .clip(RoundedCornerShape(4.dp))
                    .height(12.dp)
                    .shimmerEffect())
            }
            Column() {
                Spacer(modifier = Modifier.height(36.dp))
                Row{
                    Box(modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .shimmerEffect())
                    Spacer(modifier = Modifier.size(4.dp))
                    Box(modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect())
                    Spacer(modifier = Modifier.size(4.dp))
                    Box(modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect())
                }
            }
        }
}
@Composable
fun ShimmerListItem1(
    modifier: Modifier = Modifier
) {
    Column(
        Modifier
            .padding(6.dp)
            .alpha(0.3f)) {
        Row(Modifier.weight(3.1f)) {
            Box(modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(10.dp))
                .shimmerEffect())
            Spacer(modifier = Modifier.width(6.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(10.dp))
                .shimmerEffect())
        }
        Spacer(modifier = Modifier.size(6.dp))
        Row(Modifier.weight(3.1f)) {
            Box(modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(10.dp))
                .shimmerEffect())
            Spacer(modifier = Modifier.width(6.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(10.dp))
                .shimmerEffect())
        }
        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier
            .weight(2.2f)
            .fillMaxWidth()
            .fillMaxHeight()
            .clip(RoundedCornerShape(10.dp))
            .shimmerEffect())
    }
}
@Composable
fun ShimmerListItem2(
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Box(modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(6.dp))
            .shimmerEffect())
        Spacer(modifier = Modifier.width(0.dp))
        Column (modifier.weight(1f)){
            Box(modifier= Modifier
                .fillMaxWidth(0.5f)
                .clip(RoundedCornerShape(6.dp))
                .height(20.dp)
                .shimmerEffect())
            Spacer(modifier = Modifier.height(8.dp))
            Row{
                Box(modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .shimmerEffect())
                Spacer(modifier = Modifier.size(4.dp))
                Box(modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .shimmerEffect())
                Spacer(modifier = Modifier.size(4.dp))
                Box(modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .shimmerEffect())
                Spacer(modifier = Modifier.size(4.dp))
                Box(modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .shimmerEffect())
            }

        }
        Column() {
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier
                .size(15.dp, 30.dp)
                .clip(RoundedCornerShape(6.dp))
                .shimmerEffect())
        }

    }
}

@Composable
fun TrackerShimmer(
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .fillMaxSize()
        ) {
        val transition = rememberInfiniteTransition()
        val startOffsetX by transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000)
            )
        )
        Icon(painter = painterResource(id = R.drawable.logo_splash), contentDescription =null ,Modifier.size(100.dp).align(
            Alignment.Center).rotate(startOffsetX))
    }

}

fun Modifier.shimmerEffect() : Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000)
        )
    )
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFC4C0C0),
                Color(0xFF949191),
                Color(0xFFC4C0C0)
            ),
            start = Offset(startOffsetX,0f),
            end = Offset(startOffsetX+size.width.toFloat(),size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size=it.size
        }
}