package com.kal.brawlstatz2.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.LineType
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine

@Composable
fun TrophyGraph(str: ArrayList<Int>) {


    val pData:ArrayList<Point> = ArrayList()

    for (i in 0 until str.size){
        pData.add(Point(i.toFloat(),str[i].toFloat()))
    }
    val pointsData: List<Point> = pData.toList()
    val yMax = pointsData.maxOf { it.y }
    val yMin = pointsData.minOf { it.y }
    val xAxisData = AxisData.Builder()
        .axisStepSize(30.dp)
        .steps(25)
        .axisLineColor(MaterialTheme.colorScheme.background)
        .backgroundColor(MaterialTheme.colorScheme.primaryContainer)
        .shouldDrawAxisLineTillEnd(true)
        .build()

    val yAxisData = AxisData.Builder()
        .steps(4)
        .axisLineColor(MaterialTheme.colorScheme.background)
        .labelAndAxisLinePadding(10.dp)
        .backgroundColor(MaterialTheme.colorScheme.primaryContainer)
        .axisLabelColor(MaterialTheme.colorScheme.onBackground.copy(0.8f))
        .labelData { i ->
            (i *((yMax-yMin)/4)+yMin ).toInt().toString()
        }.build()
    val lineChartData = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    lineStyle = LineStyle(lineType = LineType.Straight(), MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)),
                    IntersectionPoint(
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    SelectionHighlightPoint(isHighlightLineRequired = false, style = Stroke(10f)),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp(popUpLabel = {_,j->
                        j.toInt().toString()
                    }),

                )
            ),
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(color=MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f)),
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        paddingRight = 0.dp,
        bottomPadding = 5.dp,
        isZoomAllowed = false

    )
    Box(modifier = Modifier
        .padding(horizontal = 14.dp)
        .background(MaterialTheme.colorScheme.primaryContainer)){
        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            lineChartData = lineChartData
        )
    }

}
