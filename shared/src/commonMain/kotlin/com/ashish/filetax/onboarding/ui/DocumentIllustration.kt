package com.ashish.filetax.onboarding.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.ashish.filetax.onboarding.model.DocumentArt

@Composable
internal fun DocumentIllustration(art: DocumentArt, modifier: Modifier = Modifier) {
    val ink = MaterialTheme.colorScheme.primary
    val paper = MaterialTheme.colorScheme.primaryContainer
    Canvas(modifier = modifier.size(48.dp)) {
        val stroke = Stroke(width = size.width * 0.05f, cap = StrokeCap.Round)
        when (art) {
            DocumentArt.SHEET -> drawSheet(ink, paper, stroke)
            DocumentArt.IDENTITY_CARD -> drawIdentityCard(ink, paper, stroke)
            DocumentArt.PORTAL -> drawPortal(ink, paper, stroke)
            DocumentArt.PASSBOOK -> drawPassbook(ink, paper, stroke)
            DocumentArt.CHART -> drawChart(ink, paper, stroke)
            DocumentArt.HOUSE -> drawHouse(ink, paper, stroke)
            DocumentArt.COIN -> drawCoin(ink, paper, stroke)
            DocumentArt.GLOBE -> drawGlobe(ink, paper, stroke)
            DocumentArt.RECEIPT -> drawReceipt(ink, paper, stroke)
            DocumentArt.FOLDER -> drawFolder(ink, paper, stroke)
        }
    }
}

private fun DrawScope.point(x: Float, y: Float) = Offset(size.width * x, size.height * y)

private fun DrawScope.rule(ink: Color, stroke: Stroke, startX: Float, endX: Float, y: Float) {
    drawLine(
        color = ink,
        start = point(startX, y),
        end = point(endX, y),
        strokeWidth = stroke.width,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.shape(ink: Color, paper: Color, stroke: Stroke, build: Path.() -> Unit) {
    val path = Path().apply(build)
    drawPath(path = path, color = paper)
    drawPath(path = path, color = ink, style = stroke)
}

private fun DrawScope.drawSheet(ink: Color, paper: Color, stroke: Stroke) {
    shape(ink, paper, stroke) {
        moveTo(size.width * 0.20f, size.height * 0.10f)
        lineTo(size.width * 0.60f, size.height * 0.10f)
        lineTo(size.width * 0.82f, size.height * 0.32f)
        lineTo(size.width * 0.82f, size.height * 0.90f)
        lineTo(size.width * 0.20f, size.height * 0.90f)
        close()
    }
    drawLine(ink, point(0.60f, 0.10f), point(0.60f, 0.32f), stroke.width, StrokeCap.Round)
    drawLine(ink, point(0.60f, 0.32f), point(0.82f, 0.32f), stroke.width, StrokeCap.Round)
    rule(ink, stroke, 0.30f, 0.68f, 0.50f)
    rule(ink, stroke, 0.30f, 0.68f, 0.64f)
    rule(ink, stroke, 0.30f, 0.54f, 0.78f)
}

private fun DrawScope.drawIdentityCard(ink: Color, paper: Color, stroke: Stroke) {
    drawRoundRect(
        color = paper,
        topLeft = point(0.10f, 0.24f),
        size = Size(size.width * 0.80f, size.height * 0.52f),
        cornerRadius = CornerRadius(size.width * 0.08f)
    )
    drawRoundRect(
        color = ink,
        topLeft = point(0.10f, 0.24f),
        size = Size(size.width * 0.80f, size.height * 0.52f),
        cornerRadius = CornerRadius(size.width * 0.08f),
        style = stroke
    )
    drawRoundRect(
        color = ink,
        topLeft = point(0.20f, 0.36f),
        size = Size(size.width * 0.18f, size.height * 0.28f),
        cornerRadius = CornerRadius(size.width * 0.03f),
        style = stroke
    )
    rule(ink, stroke, 0.46f, 0.78f, 0.42f)
    rule(ink, stroke, 0.46f, 0.78f, 0.54f)
    rule(ink, stroke, 0.46f, 0.66f, 0.66f)
}

private fun DrawScope.drawPortal(ink: Color, paper: Color, stroke: Stroke) {
    drawRoundRect(
        color = paper,
        topLeft = point(0.10f, 0.18f),
        size = Size(size.width * 0.80f, size.height * 0.64f),
        cornerRadius = CornerRadius(size.width * 0.07f)
    )
    drawRoundRect(
        color = ink,
        topLeft = point(0.10f, 0.18f),
        size = Size(size.width * 0.80f, size.height * 0.64f),
        cornerRadius = CornerRadius(size.width * 0.07f),
        style = stroke
    )
    rule(ink, stroke, 0.10f, 0.90f, 0.36f)
    drawCircle(ink, radius = size.width * 0.025f, center = point(0.20f, 0.27f))
    drawCircle(ink, radius = size.width * 0.025f, center = point(0.28f, 0.27f))
    rule(ink, stroke, 0.22f, 0.60f, 0.52f)
    rule(ink, stroke, 0.22f, 0.72f, 0.66f)
}

private fun DrawScope.drawPassbook(ink: Color, paper: Color, stroke: Stroke) {
    drawRoundRect(
        color = paper,
        topLeft = point(0.14f, 0.16f),
        size = Size(size.width * 0.72f, size.height * 0.68f),
        cornerRadius = CornerRadius(size.width * 0.06f)
    )
    drawRoundRect(
        color = ink,
        topLeft = point(0.14f, 0.16f),
        size = Size(size.width * 0.72f, size.height * 0.68f),
        cornerRadius = CornerRadius(size.width * 0.06f),
        style = stroke
    )
    drawLine(ink, point(0.34f, 0.16f), point(0.34f, 0.84f), stroke.width, StrokeCap.Round)
    rule(ink, stroke, 0.44f, 0.76f, 0.36f)
    rule(ink, stroke, 0.44f, 0.76f, 0.50f)
    rule(ink, stroke, 0.44f, 0.64f, 0.64f)
}

private fun DrawScope.drawChart(ink: Color, paper: Color, stroke: Stroke) {
    drawRoundRect(
        color = paper,
        topLeft = point(0.12f, 0.14f),
        size = Size(size.width * 0.76f, size.height * 0.72f),
        cornerRadius = CornerRadius(size.width * 0.06f)
    )
    drawRoundRect(
        color = ink,
        topLeft = point(0.12f, 0.14f),
        size = Size(size.width * 0.76f, size.height * 0.72f),
        cornerRadius = CornerRadius(size.width * 0.06f),
        style = stroke
    )
    val path = Path().apply {
        moveTo(size.width * 0.24f, size.height * 0.68f)
        lineTo(size.width * 0.40f, size.height * 0.48f)
        lineTo(size.width * 0.54f, size.height * 0.58f)
        lineTo(size.width * 0.76f, size.height * 0.28f)
    }
    drawPath(path = path, color = ink, style = stroke)
    drawCircle(ink, radius = size.width * 0.045f, center = point(0.76f, 0.28f))
}

private fun DrawScope.drawHouse(ink: Color, paper: Color, stroke: Stroke) {
    shape(ink, paper, stroke) {
        moveTo(size.width * 0.50f, size.height * 0.14f)
        lineTo(size.width * 0.88f, size.height * 0.46f)
        lineTo(size.width * 0.78f, size.height * 0.46f)
        lineTo(size.width * 0.78f, size.height * 0.86f)
        lineTo(size.width * 0.22f, size.height * 0.86f)
        lineTo(size.width * 0.22f, size.height * 0.46f)
        lineTo(size.width * 0.12f, size.height * 0.46f)
        close()
    }
    drawRoundRect(
        color = ink,
        topLeft = point(0.42f, 0.58f),
        size = Size(size.width * 0.16f, size.height * 0.28f),
        cornerRadius = CornerRadius(size.width * 0.02f),
        style = stroke
    )
}

private fun DrawScope.drawCoin(ink: Color, paper: Color, stroke: Stroke) {
    drawCircle(paper, radius = size.width * 0.34f, center = point(0.50f, 0.50f))
    drawCircle(ink, radius = size.width * 0.34f, center = point(0.50f, 0.50f), style = stroke)
    drawLine(ink, point(0.46f, 0.28f), point(0.46f, 0.72f), stroke.width, StrokeCap.Round)
    rule(ink, stroke, 0.40f, 0.62f, 0.42f)
    rule(ink, stroke, 0.40f, 0.62f, 0.58f)
}

private fun DrawScope.drawGlobe(ink: Color, paper: Color, stroke: Stroke) {
    drawCircle(paper, radius = size.width * 0.34f, center = point(0.50f, 0.50f))
    drawCircle(ink, radius = size.width * 0.34f, center = point(0.50f, 0.50f), style = stroke)
    drawOval(
        color = ink,
        topLeft = point(0.34f, 0.16f),
        size = Size(size.width * 0.32f, size.height * 0.68f),
        style = stroke
    )
    rule(ink, stroke, 0.16f, 0.84f, 0.50f)
}

private fun DrawScope.drawReceipt(ink: Color, paper: Color, stroke: Stroke) {
    shape(ink, paper, stroke) {
        moveTo(size.width * 0.22f, size.height * 0.12f)
        lineTo(size.width * 0.78f, size.height * 0.12f)
        lineTo(size.width * 0.78f, size.height * 0.78f)
        lineTo(size.width * 0.69f, size.height * 0.88f)
        lineTo(size.width * 0.60f, size.height * 0.78f)
        lineTo(size.width * 0.50f, size.height * 0.88f)
        lineTo(size.width * 0.41f, size.height * 0.78f)
        lineTo(size.width * 0.31f, size.height * 0.88f)
        lineTo(size.width * 0.22f, size.height * 0.78f)
        close()
    }
    rule(ink, stroke, 0.32f, 0.68f, 0.34f)
    rule(ink, stroke, 0.32f, 0.68f, 0.50f)
    rule(ink, stroke, 0.32f, 0.56f, 0.64f)
}

private fun DrawScope.drawFolder(ink: Color, paper: Color, stroke: Stroke) {
    shape(ink, paper, stroke) {
        moveTo(size.width * 0.12f, size.height * 0.26f)
        lineTo(size.width * 0.42f, size.height * 0.26f)
        lineTo(size.width * 0.50f, size.height * 0.38f)
        lineTo(size.width * 0.88f, size.height * 0.38f)
        lineTo(size.width * 0.88f, size.height * 0.82f)
        lineTo(size.width * 0.12f, size.height * 0.82f)
        close()
    }
    rule(ink, stroke, 0.24f, 0.60f, 0.58f)
    rule(ink, stroke, 0.24f, 0.48f, 0.70f)
}