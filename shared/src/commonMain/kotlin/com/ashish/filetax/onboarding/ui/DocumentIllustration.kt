package com.ashish.filetax.onboarding.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ashish.filetax.onboarding.model.DocumentArt
import com.ashish.filetax.ui.components.ArtTile
import filetax.shared.generated.resources.Res
import filetax.shared.generated.resources.img_chart_increasing
import filetax.shared.generated.resources.img_coin
import filetax.shared.generated.resources.img_credit_card
import filetax.shared.generated.resources.img_file_folder
import filetax.shared.generated.resources.img_globe_showing_asia_australia
import filetax.shared.generated.resources.img_house
import filetax.shared.generated.resources.img_identification_card
import filetax.shared.generated.resources.img_laptop
import filetax.shared.generated.resources.img_page_facing_up
import filetax.shared.generated.resources.img_receipt
import org.jetbrains.compose.resources.DrawableResource

internal val DocumentArt.image: DrawableResource
    get() = when (this) {
        DocumentArt.IDENTITY_CARD -> Res.drawable.img_identification_card
        DocumentArt.SHEET -> Res.drawable.img_page_facing_up
        DocumentArt.PORTAL -> Res.drawable.img_laptop
        DocumentArt.PASSBOOK -> Res.drawable.img_credit_card
        DocumentArt.CHART -> Res.drawable.img_chart_increasing
        DocumentArt.HOUSE -> Res.drawable.img_house
        DocumentArt.COIN -> Res.drawable.img_coin
        DocumentArt.GLOBE -> Res.drawable.img_globe_showing_asia_australia
        DocumentArt.RECEIPT -> Res.drawable.img_receipt
        DocumentArt.FOLDER -> Res.drawable.img_file_folder
    }

@Composable
internal fun DocumentIllustration(
    art: DocumentArt,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    motion: () -> Float = { 0f }
) {
    val sheen = MaterialTheme.colorScheme.primary
    ArtTile(
        resource = art.image,
        size = size,
        containerColor = containerColor,
        modifier = modifier,
        overlay = { drawSheen(sheen, motion()) }
    )
}

private fun DrawScope.drawSheen(ink: Color, motion: Float) {
    if (motion <= 0f) return
    val head = motion * 1.7f - 0.35f
    drawPath(
        path = Path().apply {
            moveTo(size.width * (head - 0.22f), size.height)
            lineTo(size.width * head, 0f)
            lineTo(size.width * (head + 0.12f), 0f)
            lineTo(size.width * (head - 0.10f), size.height)
            close()
        },
        color = ink.copy(alpha = 0.14f)
    )
}