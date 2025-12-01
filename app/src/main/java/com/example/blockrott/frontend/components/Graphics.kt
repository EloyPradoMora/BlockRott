package com.example.blockrott.frontend.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class PieChartEntry(val percentage: Double, val color: Color)
@Composable
fun DailyPieChart(
    data: List<PieChartEntry>,
    modifier: Modifier = Modifier
){
    val total = data.sumOf { it.percentage }
    val gap = 2f
    Canvas(modifier = modifier.testTag("DailyPieChartCanvas")) {
        if (total == 0.0) return@Canvas
        var startAngle = -90f

        data.forEach { entry ->
            val sweep = ((entry.percentage / total ) * 360f).toFloat()
            val stroke = Stroke(
                width = 30f,
                cap = StrokeCap.Round
            )
            drawArc(
                color = entry.color,
                startAngle = startAngle + gap,
                sweepAngle = sweep - gap,
                useCenter = false,
                size = size,
                style = stroke
            )
            startAngle += sweep
        }
    }
}

@Composable
fun WeeklyBarChart(
    values: List<Float>,
    labels: List<String>,
    barColor: Color = Color(0xFF7B61FF),
) {
    val maxValue = values.maxOrNull() ?: 1f
    Canvas(
        modifier = Modifier
            .height(180.dp)
            .fillMaxWidth()
            .testTag("WeeklyBarChartCanvas")
    ) {
        val leftPadding = 2.dp.toPx()
        val bottomPadding = 24.dp.toPx()
        val barSpace = 16.dp.toPx()

        val barWidth = (size.width - barSpace * (values.size - 1)) / values.size
        val chartHeight = size.height - bottomPadding

        values.forEachIndexed { index, value ->
            val barHeight = (value / maxValue) * chartHeight
            val x = leftPadding + index * (barWidth + barSpace)
            val y = size.height - barHeight - bottomPadding

            drawRoundRect(
                color = barColor,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 3)
            )
        }
        drawLine(
            color = Color.Black,
            start = Offset(leftPadding, 0f),
            end = Offset(leftPadding, size.height - bottomPadding),
            strokeWidth = 3f
        )
        drawLine(
            color = Color.Black,
            start = Offset(leftPadding, size.height - bottomPadding),
            end = Offset(size.width, size.height - bottomPadding),
            strokeWidth = 3f
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("WeeklyBarChartLabels")
        ,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        labels.forEach {
            Text(
                text = it,
                fontSize = 12.sp,
                modifier = Modifier.width(30.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}