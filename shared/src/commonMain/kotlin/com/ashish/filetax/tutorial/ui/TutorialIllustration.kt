package com.ashish.filetax.tutorial.ui

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ashish.filetax.tutorial.TutorialAnimation
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

@Composable
internal fun TutorialIllustration(
    animation: TutorialAnimation,
    modifier: Modifier = Modifier,
    height: Dp = 120.dp
) {
    val ink = MaterialTheme.colorScheme.primary
    val paper = MaterialTheme.colorScheme.primaryContainer
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    val transition = rememberInfiniteTransition(label = animation.name)
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = animation.durationMillis, easing = LinearEasing)
        ),
        label = "time"
    )
    Canvas(modifier = modifier.fillMaxWidth().height(height)) {
        val side = min(size.width, size.height)
        val frame = Frame(
            origin = Offset((size.width - side) / 2f, (size.height - side) / 2f),
            side = side,
            ink = ink,
            paper = paper,
            muted = muted,
            t = time
        )
        when (animation) {
            TutorialAnimation.ELIGIBILITY -> drawEligibility(frame)
            TutorialAnimation.LOGIN -> drawLogin(frame)
            TutorialAnimation.PICK_FORM -> drawPickForm(frame)
            TutorialAnimation.REGIME -> drawRegime(frame)
            TutorialAnimation.SALARY -> drawSalary(frame)
            TutorialAnimation.HOUSE -> drawHouse(frame)
            TutorialAnimation.CAPITAL_GAINS -> drawCapitalGains(frame)
            TutorialAnimation.CRYPTO -> drawCrypto(frame)
            TutorialAnimation.OTHER_INCOME -> drawOtherIncome(frame)
            TutorialAnimation.EXEMPT_INCOME -> drawExemptIncome(frame)
            TutorialAnimation.LOSSES -> drawLosses(frame)
            TutorialAnimation.FOREIGN -> drawForeign(frame)
            TutorialAnimation.EXTRA_SCHEDULES -> drawExtraSchedules(frame)
            TutorialAnimation.LEDGER -> drawLedger(frame)
            TutorialAnimation.PRESUMPTIVE -> drawPresumptive(frame)
            TutorialAnimation.TRADING -> drawTrading(frame)
            TutorialAnimation.PARTNER -> drawPartner(frame)
            TutorialAnimation.DEDUCTIONS -> drawDeductions(frame)
            TutorialAnimation.MATCH_TAXES -> drawMatchTaxes(frame)
            TutorialAnimation.PAY_TAX -> drawPayTax(frame)
            TutorialAnimation.SUBMIT -> drawSubmit(frame)
            TutorialAnimation.VERIFY -> drawVerify(frame)
            TutorialAnimation.CALENDAR -> drawCalendar(frame)
            TutorialAnimation.PRIVACY -> drawPrivacy(frame)
            TutorialAnimation.SCAN -> drawScan(frame)
        }
    }
}

private class Frame(
    val origin: Offset,
    val side: Float,
    val ink: Color,
    val paper: Color,
    val muted: Color,
    val t: Float
) {
    val line: Float = side * 0.022f

    fun at(x: Float, y: Float): Offset = Offset(origin.x + side * x, origin.y + side * y)

    fun span(width: Float, height: Float): Size = Size(side * width, side * height)

    fun len(value: Float): Float = side * value

    fun wave(offset: Float = 0f): Float = sin(((t + offset) % 1f) * 2f * PI.toFloat())

    fun swell(offset: Float = 0f): Float = (1f - cos(((t + offset) % 1f) * 2f * PI.toFloat())) / 2f

    fun sweep(offset: Float = 0f): Float = (t + offset) % 1f

    fun stage(count: Int): Int = (t * count).toInt().coerceIn(0, count - 1)

    fun within(from: Float, to: Float): Float =
        ((t - from) / (to - from)).coerceIn(0f, 1f)
}

private fun DrawScope.card(
    f: Frame,
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    fill: Color = f.paper,
    outline: Color = f.ink,
    corner: Float = 0.03f
) {
    drawRoundRect(
        color = fill,
        topLeft = f.at(x, y),
        size = f.span(width, height),
        cornerRadius = CornerRadius(f.len(corner))
    )
    drawRoundRect(
        color = outline,
        topLeft = f.at(x, y),
        size = f.span(width, height),
        cornerRadius = CornerRadius(f.len(corner)),
        style = Stroke(width = f.line, cap = StrokeCap.Round)
    )
}

private fun DrawScope.rule(
    f: Frame,
    x1: Float,
    y: Float,
    x2: Float,
    color: Color = f.ink,
    width: Float = f.line
) {
    if (x2 <= x1) return
    drawLine(color, f.at(x1, y), f.at(x2, y), width, StrokeCap.Round)
}

private fun DrawScope.stem(
    f: Frame,
    x1: Float,
    y1: Float,
    x2: Float,
    y2: Float,
    color: Color = f.ink,
    width: Float = f.line
) {
    drawLine(color, f.at(x1, y1), f.at(x2, y2), width, StrokeCap.Round)
}

private fun DrawScope.tick(f: Frame, x: Float, y: Float, scale: Float, color: Color = f.ink) {
    if (scale <= 0f) return
    val s = scale.coerceIn(0f, 1f)
    stem(f, x - 0.05f * s, y, x - 0.015f * s, y + 0.04f * s, color, f.line * 1.4f)
    stem(f, x - 0.015f * s, y + 0.04f * s, x + 0.06f * s, y - 0.05f * s, color, f.line * 1.4f)
}

private fun DrawScope.coin(f: Frame, x: Float, y: Float, radius: Float, squash: Float = 1f) {
    val path = Path().apply {
        addOval(
            androidx.compose.ui.geometry.Rect(
                offset = f.at(x - radius * squash, y - radius),
                size = f.span(radius * 2 * squash, radius * 2)
            )
        )
    }
    drawPath(path, f.paper)
    drawPath(path, f.ink, style = Stroke(width = f.line))
}

private fun DrawScope.arrow(f: Frame, x1: Float, y1: Float, x2: Float, y2: Float, color: Color = f.ink) {
    stem(f, x1, y1, x2, y2, color)
    val dx = x2 - x1
    val dy = y2 - y1
    val length = kotlin.math.sqrt(dx * dx + dy * dy).takeIf { it > 0f } ?: return
    val ux = dx / length
    val uy = dy / length
    val head = 0.05f
    stem(f, x2, y2, x2 - head * (ux + uy * 0.7f), y2 - head * (uy - ux * 0.7f), color)
    stem(f, x2, y2, x2 - head * (ux - uy * 0.7f), y2 - head * (uy + ux * 0.7f), color)
}

private fun DrawScope.sheet(f: Frame, x: Float, y: Float, width: Float, height: Float, rules: Int = 3) {
    card(f, x, y, width, height)
    val gap = height / (rules + 1)
    repeat(rules) { index ->
        rule(f, x + width * 0.15f, y + gap * (index + 1), x + width * 0.85f, f.ink, f.line * 0.8f)
    }
}

private fun DrawScope.drawEligibility(f: Frame) {
    sheet(f, 0.16f, 0.12f, 0.54f, 0.76f, rules = 4)
    val row = f.stage(4)
    val y = 0.12f + 0.76f / 5f * (row + 1)
    drawRoundRect(
        color = f.ink.copy(alpha = 0.18f),
        topLeft = f.at(0.20f, y - 0.05f),
        size = f.span(0.46f, 0.10f),
        cornerRadius = CornerRadius(f.len(0.02f))
    )
    val lensX = 0.70f + f.wave() * 0.03f
    drawCircle(f.paper, f.len(0.11f), f.at(lensX, y))
    drawCircle(f.ink, f.len(0.11f), f.at(lensX, y), style = Stroke(width = f.line))
    stem(f, lensX + 0.08f, y + 0.08f, lensX + 0.16f, y + 0.16f, f.ink, f.line * 1.4f)
}

private fun DrawScope.drawLogin(f: Frame) {
    card(f, 0.30f, 0.08f, 0.40f, 0.84f, corner = 0.06f)
    rule(f, 0.42f, 0.14f, 0.58f, f.ink, f.line * 0.8f)
    card(f, 0.36f, 0.32f, 0.28f, 0.10f, fill = f.paper.copy(alpha = 0.4f))
    val filled = (f.sweep() * 5f).toInt().coerceIn(0, 4)
    repeat(4) { index ->
        val x = 0.39f + index * 0.06f
        drawCircle(
            color = if (index < filled) f.ink else f.ink.copy(alpha = 0.25f),
            radius = f.len(0.018f),
            center = f.at(x, 0.37f)
        )
    }
    card(f, 0.36f, 0.50f, 0.28f, 0.10f, fill = if (filled >= 4) f.ink else f.paper.copy(alpha = 0.4f))
    if (filled >= 4) {
        tick(f, 0.50f, 0.72f, f.within(0.8f, 1f))
    }
}

private fun DrawScope.drawPickForm(f: Frame) {
    val chosen = f.stage(3)
    repeat(3) { index ->
        val lift = if (index == chosen) 0.06f else 0f
        val x = 0.10f + index * 0.28f
        card(
            f,
            x,
            0.30f - lift,
            0.24f,
            0.44f,
            fill = if (index == chosen) f.paper else f.paper.copy(alpha = 0.45f),
            outline = if (index == chosen) f.ink else f.ink.copy(alpha = 0.4f)
        )
        rule(f, x + 0.04f, 0.42f - lift, x + 0.20f, f.ink.copy(alpha = 0.6f), f.line * 0.8f)
        rule(f, x + 0.04f, 0.52f - lift, x + 0.16f, f.ink.copy(alpha = 0.6f), f.line * 0.8f)
    }
    tick(f, 0.10f + chosen * 0.28f + 0.12f, 0.20f, 1f)
}

private fun DrawScope.drawRegime(f: Frame) {
    val tiltRaw = f.wave() * 0.07f
    stem(f, 0.50f, 0.30f, 0.50f, 0.80f, f.ink, f.line * 1.2f)
    rule(f, 0.36f, 0.82f, 0.64f, f.ink, f.line * 1.2f)
    stem(f, 0.18f, 0.30f + tiltRaw, 0.82f, 0.30f - tiltRaw, f.ink, f.line * 1.2f)
    drawCircle(f.ink, f.len(0.03f), f.at(0.50f, 0.30f))
    listOf(0.18f to tiltRaw, 0.82f to -tiltRaw).forEachIndexed { index, pair ->
        val (x, offset) = pair
        stem(f, x, 0.30f + offset, x, 0.44f + offset, f.ink.copy(alpha = 0.5f), f.line * 0.8f)
        card(f, x - 0.12f, 0.44f + offset, 0.24f, 0.12f, fill = if (index == 0) f.paper else f.paper.copy(alpha = 0.5f))
        repeat(if (index == 0) 3 else 2) { dot ->
            drawCircle(f.ink, f.len(0.015f), f.at(x - 0.06f + dot * 0.06f, 0.50f + offset))
        }
    }
}

private fun DrawScope.drawSalary(f: Frame) {
    sheet(f, 0.06f, 0.16f, 0.34f, 0.62f, rules = 3)
    card(f, 0.58f, 0.16f, 0.36f, 0.62f, fill = f.paper.copy(alpha = 0.45f))
    rule(f, 0.63f, 0.30f, 0.89f, f.ink.copy(alpha = 0.4f), f.line * 0.8f)
    rule(f, 0.63f, 0.46f, 0.89f, f.ink.copy(alpha = 0.4f), f.line * 0.8f)
    rule(f, 0.63f, 0.62f, 0.89f, f.ink.copy(alpha = 0.4f), f.line * 0.8f)
    repeat(3) { index ->
        val phase = (f.t * 3f - index * 0.34f) % 1f
        if (phase in 0f..0.6f) {
            val travel = (phase / 0.6f).coerceIn(0f, 1f)
            val x = 0.30f + travel * 0.38f
            val y = 0.30f + index * 0.16f
            drawRoundRect(
                color = f.ink,
                topLeft = f.at(x, y - 0.02f),
                size = f.span(0.10f, 0.04f),
                cornerRadius = CornerRadius(f.len(0.02f))
            )
        }
    }
}

private fun DrawScope.drawHouse(f: Frame) {
    val path = Path().apply {
        moveTo(f.at(0.50f, 0.22f).x, f.at(0.50f, 0.22f).y)
        lineTo(f.at(0.82f, 0.48f).x, f.at(0.82f, 0.48f).y)
        lineTo(f.at(0.82f, 0.84f).x, f.at(0.82f, 0.84f).y)
        lineTo(f.at(0.18f, 0.84f).x, f.at(0.18f, 0.84f).y)
        lineTo(f.at(0.18f, 0.48f).x, f.at(0.18f, 0.48f).y)
        close()
    }
    drawPath(path, f.paper)
    drawPath(path, f.ink, style = Stroke(width = f.line, cap = StrokeCap.Round))
    card(f, 0.42f, 0.62f, 0.16f, 0.22f, fill = f.paper.copy(alpha = 0.4f))
    val drop = f.sweep()
    val x = 0.92f - drop * 0.30f
    val y = 0.14f + drop * drop * 0.34f
    coin(f, x, y, 0.05f)
    arrow(f, 0.18f, 0.92f, 0.06f, 0.92f, f.muted)
}

private fun DrawScope.drawCapitalGains(f: Frame) {
    rule(f, 0.12f, 0.84f, 0.90f, f.muted, f.line * 0.8f)
    stem(f, 0.12f, 0.84f, 0.12f, 0.16f, f.muted, f.line * 0.8f)
    val heights = listOf(0.22f, 0.40f, 0.30f, 0.54f)
    heights.forEachIndexed { index, target ->
        val grow = f.within(index * 0.18f, index * 0.18f + 0.3f)
        val height = target * grow
        drawRoundRect(
            color = if (index % 2 == 0) f.paper else f.ink.copy(alpha = 0.35f),
            topLeft = f.at(0.20f + index * 0.17f, 0.84f - height),
            size = f.span(0.11f, height),
            cornerRadius = CornerRadius(f.len(0.02f))
        )
    }
    val trail = f.within(0.35f, 0.95f)
    if (trail > 0f) {
        arrow(f, 0.22f, 0.66f, 0.22f + trail * 0.56f, 0.66f - trail * 0.40f, f.ink)
    }
}

private fun DrawScope.drawCrypto(f: Frame) {
    val squash = abs(cos(f.t * 2f * PI.toFloat())).coerceAtLeast(0.12f)
    coin(f, 0.50f, 0.50f, 0.26f, squash)
    if (squash > 0.5f) {
        rule(f, 0.44f, 0.50f, 0.56f, f.ink, f.line)
        stem(f, 0.50f, 0.40f, 0.50f, 0.60f, f.ink, f.line)
    }
    drawArc(
        color = f.ink,
        startAngle = -90f,
        sweepAngle = 360f * 0.30f * f.within(0.2f, 0.9f),
        useCenter = false,
        topLeft = f.at(0.18f, 0.18f),
        size = f.span(0.64f, 0.64f),
        style = Stroke(width = f.line * 1.6f, cap = StrokeCap.Round)
    )
}

private fun DrawScope.drawOtherIncome(f: Frame) {
    card(f, 0.22f, 0.54f, 0.56f, 0.30f)
    repeat(4) { index ->
        stem(f, 0.30f + index * 0.13f, 0.58f, 0.30f + index * 0.13f, 0.80f, f.ink.copy(alpha = 0.5f), f.line * 0.8f)
    }
    val roof = Path().apply {
        moveTo(f.at(0.16f, 0.54f).x, f.at(0.16f, 0.54f).y)
        lineTo(f.at(0.50f, 0.36f).x, f.at(0.50f, 0.36f).y)
        lineTo(f.at(0.84f, 0.54f).x, f.at(0.84f, 0.54f).y)
        close()
    }
    drawPath(roof, f.paper)
    drawPath(roof, f.ink, style = Stroke(width = f.line, cap = StrokeCap.Round))
    repeat(3) { index ->
        val phase = (f.t + index * 0.33f) % 1f
        val y = 0.06f + phase * 0.26f
        drawCircle(f.ink.copy(alpha = 1f - phase), f.len(0.022f), f.at(0.34f + index * 0.16f, y))
    }
}

private fun DrawScope.drawExemptIncome(f: Frame) {
    val sway = f.wave() * 0.03f
    stem(f, 0.50f, 0.86f, 0.50f + sway, 0.36f, f.ink, f.line * 1.2f)
    repeat(3) { index ->
        val y = 0.44f + index * 0.14f
        val reach = 0.16f - index * 0.02f
        stem(f, 0.50f + sway * 0.6f, y, 0.50f + sway - reach, y - 0.08f, f.ink.copy(alpha = 0.7f))
        stem(f, 0.50f + sway * 0.6f, y, 0.50f + sway + reach, y - 0.08f, f.ink.copy(alpha = 0.7f))
    }
    drawCircle(f.paper, f.len(0.09f), f.at(0.50f + sway, 0.30f))
    drawCircle(f.ink, f.len(0.09f), f.at(0.50f + sway, 0.30f), style = Stroke(width = f.line))
    tick(f, 0.50f + sway, 0.30f, 0.8f)
}

private fun DrawScope.drawLosses(f: Frame) {
    rule(f, 0.10f, 0.72f, 0.90f, f.muted, f.line * 0.8f)
    repeat(3) { index ->
        val x = 0.20f + index * 0.30f
        stem(f, x, 0.68f, x, 0.76f, f.muted, f.line * 0.8f)
    }
    val travel = f.sweep()
    val x = 0.20f + travel * 0.60f
    val hop = abs(sin(travel * 3f * PI.toFloat())) * 0.10f
    card(f, x - 0.09f, 0.44f - hop, 0.18f, 0.16f)
    rule(f, x - 0.05f, 0.52f - hop, x + 0.05f, f.ink, f.line * 0.8f)
    arrow(f, 0.14f, 0.30f, 0.86f, 0.30f, f.ink.copy(alpha = 0.5f))
}

private fun DrawScope.drawForeign(f: Frame) {
    drawCircle(f.paper, f.len(0.28f), f.at(0.50f, 0.50f))
    drawCircle(f.ink, f.len(0.28f), f.at(0.50f, 0.50f), style = Stroke(width = f.line))
    rule(f, 0.22f, 0.50f, 0.78f, f.ink.copy(alpha = 0.6f), f.line * 0.8f)
    val meridian = abs(cos(f.t * 2f * PI.toFloat())).coerceAtLeast(0.08f)
    drawArc(
        color = f.ink.copy(alpha = 0.6f),
        startAngle = 90f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = f.at(0.50f - 0.28f * meridian, 0.22f),
        size = f.span(0.56f * meridian, 0.56f),
        style = Stroke(width = f.line * 0.8f)
    )
    drawArc(
        color = f.ink.copy(alpha = 0.6f),
        startAngle = -90f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = f.at(0.50f - 0.28f * meridian, 0.22f),
        size = f.span(0.56f * meridian, 0.56f),
        style = Stroke(width = f.line * 0.8f)
    )
    val angle = f.t * 2f * PI.toFloat()
    drawCircle(f.ink, f.len(0.04f), f.at(0.50f + cos(angle) * 0.38f, 0.50f + sin(angle) * 0.16f))
}

private fun DrawScope.drawExtraSchedules(f: Frame) {
    val spread = f.swell()
    repeat(4) { index ->
        val offset = (index - 1.5f) * 0.10f * spread
        card(
            f,
            0.34f + offset,
            0.26f + index * 0.02f,
            0.32f,
            0.46f,
            fill = f.paper.copy(alpha = 0.5f + index * 0.12f)
        )
    }
    rule(f, 0.42f, 0.44f, 0.62f, f.ink.copy(alpha = 0.7f), f.line * 0.8f)
    rule(f, 0.42f, 0.54f, 0.56f, f.ink.copy(alpha = 0.7f), f.line * 0.8f)
}

private fun DrawScope.drawLedger(f: Frame) {
    card(f, 0.12f, 0.14f, 0.76f, 0.72f)
    stem(f, 0.56f, 0.14f, 0.56f, 0.86f, f.ink.copy(alpha = 0.4f), f.line * 0.8f)
    repeat(4) { index ->
        val y = 0.26f + index * 0.14f
        val fill = f.within(index * 0.2f, index * 0.2f + 0.25f)
        rule(f, 0.18f, y, 0.18f + 0.32f * fill, f.ink.copy(alpha = 0.7f), f.line * 0.9f)
        rule(f, 0.62f, y, 0.62f + 0.20f * fill, f.ink, f.line * 0.9f)
    }
    val total = f.within(0.85f, 1f)
    rule(f, 0.62f, 0.82f, 0.62f + 0.20f * total, f.ink, f.line * 1.6f)
}

private fun DrawScope.drawPresumptive(f: Frame) {
    drawCircle(f.paper, f.len(0.28f), f.at(0.40f, 0.50f))
    drawCircle(f.ink, f.len(0.28f), f.at(0.40f, 0.50f), style = Stroke(width = f.line))
    drawArc(
        color = f.ink,
        startAngle = -90f,
        sweepAngle = 360f * 0.08f * f.within(0f, 0.6f),
        useCenter = true,
        topLeft = f.at(0.12f, 0.22f),
        size = f.span(0.56f, 0.56f)
    )
    repeat(3) { index ->
        val y = 0.34f + index * 0.16f
        val grow = f.within(0.2f + index * 0.2f, 0.5f + index * 0.2f)
        rule(f, 0.74f, y, 0.74f + 0.18f * grow, f.ink.copy(alpha = 0.8f), f.line * 1.4f)
    }
}

private fun DrawScope.drawTrading(f: Frame) {
    val phase = f.stage(2)
    card(f, 0.44f, 0.34f, 0.12f, 0.32f)
    stem(f, 0.50f, 0.24f, 0.50f, 0.34f, f.ink)
    stem(f, 0.50f, 0.66f, 0.50f, 0.76f, f.ink)
    if (phase == 0) {
        arrow(f, 0.26f, 0.70f, 0.26f, 0.26f, f.ink)
        arrow(f, 0.74f, 0.30f, 0.74f, 0.74f, f.ink.copy(alpha = 0.35f))
    } else {
        arrow(f, 0.26f, 0.30f, 0.26f, 0.74f, f.ink.copy(alpha = 0.35f))
        arrow(f, 0.74f, 0.70f, 0.74f, 0.26f, f.ink)
    }
}

private fun DrawScope.drawPartner(f: Frame) {
    drawCircle(f.paper, f.len(0.24f), f.at(0.34f, 0.50f))
    drawCircle(f.ink, f.len(0.24f), f.at(0.34f, 0.50f), style = Stroke(width = f.line))
    drawCircle(f.paper.copy(alpha = 0.6f), f.len(0.18f), f.at(0.74f, 0.52f))
    drawCircle(f.ink, f.len(0.18f), f.at(0.74f, 0.52f), style = Stroke(width = f.line))
    val travel = f.sweep()
    coin(f, 0.34f + travel * 0.40f, 0.50f + travel * 0.02f, 0.05f)
    tick(f, 0.74f, 0.52f, f.within(0.85f, 1f))
}

private fun DrawScope.drawDeductions(f: Frame) {
    val shrink = f.within(0.25f, 0.75f)
    val height = 0.56f - 0.22f * shrink
    drawRoundRect(
        color = f.paper,
        topLeft = f.at(0.20f, 0.84f - height),
        size = f.span(0.22f, height),
        cornerRadius = CornerRadius(f.len(0.02f))
    )
    drawRoundRect(
        color = f.ink,
        topLeft = f.at(0.20f, 0.84f - height),
        size = f.span(0.22f, height),
        cornerRadius = CornerRadius(f.len(0.02f)),
        style = Stroke(width = f.line)
    )
    val shield = Path().apply {
        moveTo(f.at(0.70f, 0.22f).x, f.at(0.70f, 0.22f).y)
        lineTo(f.at(0.90f, 0.32f).x, f.at(0.90f, 0.32f).y)
        lineTo(f.at(0.90f, 0.56f).x, f.at(0.90f, 0.56f).y)
        cubicTo(
            f.at(0.90f, 0.70f).x, f.at(0.90f, 0.70f).y,
            f.at(0.80f, 0.78f).x, f.at(0.80f, 0.78f).y,
            f.at(0.70f, 0.82f).x, f.at(0.70f, 0.82f).y
        )
        cubicTo(
            f.at(0.60f, 0.78f).x, f.at(0.60f, 0.78f).y,
            f.at(0.50f, 0.70f).x, f.at(0.50f, 0.70f).y,
            f.at(0.50f, 0.56f).x, f.at(0.50f, 0.56f).y
        )
        lineTo(f.at(0.50f, 0.32f).x, f.at(0.50f, 0.32f).y)
        close()
    }
    drawPath(shield, f.paper)
    drawPath(shield, f.ink, style = Stroke(width = f.line, cap = StrokeCap.Round))
    tick(f, 0.70f, 0.52f, 1f)
    arrow(f, 0.46f, 0.30f, 0.34f, 0.30f, f.muted)
}

private fun DrawScope.drawMatchTaxes(f: Frame) {
    card(f, 0.06f, 0.16f, 0.34f, 0.68f, fill = f.paper.copy(alpha = 0.5f))
    card(f, 0.60f, 0.16f, 0.34f, 0.68f, fill = f.paper.copy(alpha = 0.5f))
    repeat(3) { index ->
        val y = 0.30f + index * 0.20f
        rule(f, 0.11f, y, 0.35f, f.ink.copy(alpha = 0.7f), f.line * 0.9f)
        rule(f, 0.65f, y, 0.89f, f.ink.copy(alpha = 0.7f), f.line * 0.9f)
        val matched = f.within(index * 0.28f, index * 0.28f + 0.2f)
        if (matched > 0f) {
            drawCircle(f.paper, f.len(0.07f) * matched, f.at(0.50f, y))
            drawCircle(f.ink, f.len(0.07f) * matched, f.at(0.50f, y), style = Stroke(width = f.line))
            tick(f, 0.50f, y, matched * 0.9f)
        }
    }
}

private fun DrawScope.drawPayTax(f: Frame) {
    card(f, 0.22f, 0.40f, 0.56f, 0.44f)
    rule(f, 0.28f, 0.56f, 0.56f, f.ink.copy(alpha = 0.6f), f.line * 0.8f)
    rule(f, 0.28f, 0.66f, 0.48f, f.ink.copy(alpha = 0.6f), f.line * 0.8f)
    rule(f, 0.34f, 0.44f, 0.66f, f.ink, f.line * 1.4f)
    val drop = f.sweep()
    if (drop < 0.7f) {
        val fall = (drop / 0.7f).coerceIn(0f, 1f)
        coin(f, 0.68f, 0.10f + fall * 0.36f, 0.07f)
    } else {
        val stamp = ((drop - 0.7f) / 0.3f).coerceIn(0f, 1f)
        drawCircle(f.ink.copy(alpha = 0.25f * stamp), f.len(0.12f), f.at(0.66f, 0.66f))
        tick(f, 0.66f, 0.66f, stamp)
    }
}

private fun DrawScope.drawSubmit(f: Frame) {
    card(f, 0.14f, 0.62f, 0.72f, 0.24f, fill = f.paper.copy(alpha = 0.5f))
    rule(f, 0.22f, 0.62f, 0.78f, f.ink, f.line)
    val slide = f.within(0f, 0.65f)
    val y = 0.16f + slide * 0.40f
    sheet(f, 0.30f, y, 0.40f, 0.34f, rules = 2)
    val done = f.within(0.7f, 1f)
    if (done > 0f) {
        tick(f, 0.50f, 0.76f, done)
    }
}

private fun DrawScope.drawVerify(f: Frame) {
    val filled = (f.sweep() * 6f).toInt().coerceIn(0, 4)
    repeat(4) { index ->
        val x = 0.20f + index * 0.16f
        card(
            f,
            x,
            0.34f,
            0.12f,
            0.20f,
            fill = if (index < filled) f.paper else f.paper.copy(alpha = 0.35f)
        )
        if (index < filled) {
            drawCircle(f.ink, f.len(0.02f), f.at(x + 0.06f, 0.44f))
        }
    }
    if (filled >= 4) {
        val pop = f.within(0.75f, 1f)
        drawCircle(f.paper, f.len(0.12f) * pop, f.at(0.50f, 0.72f))
        drawCircle(f.ink, f.len(0.12f) * pop, f.at(0.50f, 0.72f), style = Stroke(width = f.line))
        tick(f, 0.50f, 0.72f, pop)
    }
}

private fun DrawScope.drawCalendar(f: Frame) {
    card(f, 0.14f, 0.20f, 0.72f, 0.66f)
    rule(f, 0.14f, 0.36f, 0.86f, f.ink, f.line)
    stem(f, 0.30f, 0.14f, 0.30f, 0.24f, f.ink, f.line * 1.2f)
    stem(f, 0.70f, 0.14f, 0.70f, 0.24f, f.ink, f.line * 1.2f)
    val marked = f.stage(8)
    repeat(8) { index ->
        val column = index % 4
        val row = index / 4
        val x = 0.24f + column * 0.16f
        val y = 0.50f + row * 0.20f
        val active = index == marked
        drawCircle(
            color = if (active) f.ink else f.ink.copy(alpha = 0.22f),
            radius = f.len(if (active) 0.05f else 0.035f),
            center = f.at(x, y)
        )
    }
    val flagX = 0.24f + 3 * 0.16f
    stem(f, flagX, 0.70f, flagX, 0.42f, f.ink, f.line)
    val flag = Path().apply {
        moveTo(f.at(flagX, 0.42f).x, f.at(flagX, 0.42f).y)
        lineTo(f.at(flagX + 0.14f, 0.47f).x, f.at(flagX + 0.14f, 0.47f).y)
        lineTo(f.at(flagX, 0.52f).x, f.at(flagX, 0.52f).y)
        close()
    }
    drawPath(flag, f.ink.copy(alpha = 0.6f + 0.4f * f.swell()))
}

private fun DrawScope.drawPrivacy(f: Frame) {
    card(f, 0.30f, 0.12f, 0.40f, 0.76f, corner = 0.06f)
    sheet(f, 0.36f, 0.24f, 0.28f, 0.30f, rules = 2)
    val shackle = f.at(0.50f, 0.66f)
    drawArc(
        color = f.ink,
        startAngle = 180f,
        sweepAngle = 180f,
        useCenter = false,
        topLeft = Offset(shackle.x - f.len(0.07f), shackle.y - f.len(0.10f)),
        size = f.span(0.14f, 0.14f),
        style = Stroke(width = f.line * 1.2f, cap = StrokeCap.Round)
    )
    card(f, 0.41f, 0.66f, 0.18f, 0.14f, fill = f.ink, outline = f.ink)
    val dashes = PathEffect.dashPathEffect(floatArrayOf(f.len(0.04f), f.len(0.03f)))
    drawRoundRect(
        color = f.muted.copy(alpha = 0.6f),
        topLeft = f.at(0.16f, 0.04f),
        size = f.span(0.68f, 0.92f),
        cornerRadius = CornerRadius(f.len(0.06f)),
        style = Stroke(width = f.line * 0.8f, pathEffect = dashes)
    )
    val push = f.swell()
    val reach = 0.72f + push * 0.10f
    arrow(f, 0.66f, 0.34f, reach, 0.34f, f.muted)
    if (push > 0.7f) {
        stem(f, 0.84f, 0.28f, 0.90f, 0.40f, f.muted)
        stem(f, 0.90f, 0.28f, 0.84f, 0.40f, f.muted)
    }
}

private fun DrawScope.drawScan(f: Frame) {
    sheet(f, 0.12f, 0.12f, 0.46f, 0.76f, rules = 4)
    val line = f.sweep()
    val y = 0.14f + line * 0.72f
    rule(f, 0.10f, y, 0.60f, f.ink, f.line * 1.6f)
    drawRoundRect(
        color = f.ink.copy(alpha = 0.10f),
        topLeft = f.at(0.12f, 0.12f),
        size = f.span(0.46f, (y - 0.12f).coerceAtLeast(0f)),
        cornerRadius = CornerRadius(f.len(0.02f))
    )
    repeat(3) { index ->
        val appear = f.within(index * 0.28f + 0.1f, index * 0.28f + 0.3f)
        if (appear > 0f) {
            val chipY = 0.26f + index * 0.22f
            drawRoundRect(
                color = f.paper,
                topLeft = f.at(0.66f, chipY - 0.06f),
                size = f.span(0.26f * appear, 0.12f),
                cornerRadius = CornerRadius(f.len(0.03f))
            )
            drawRoundRect(
                color = f.ink,
                topLeft = f.at(0.66f, chipY - 0.06f),
                size = f.span(0.26f * appear, 0.12f),
                cornerRadius = CornerRadius(f.len(0.03f)),
                style = Stroke(width = f.line)
            )
        }
    }
}