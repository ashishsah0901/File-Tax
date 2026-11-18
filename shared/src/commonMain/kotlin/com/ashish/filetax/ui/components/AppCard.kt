package com.ashish.filetax.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
internal fun AppCard(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    outlined: Boolean = true,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = MaterialTheme.shapes.large
    val border = if (outlined) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant) else null
    if (onClick != null) {
        Surface(
            onClick = onClick,
            shape = shape,
            color = color,
            border = border,
            modifier = modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(contentPadding), content = content)
        }
    } else {
        Surface(
            shape = shape,
            color = color,
            border = border,
            modifier = modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(contentPadding), content = content)
        }
    }
}

@Composable
internal fun ArtTile(
    resource: DrawableResource,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    containerColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    overlay: DrawScope.() -> Unit = {}
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.28f))
            .background(containerColor)
            .drawWithContent {
                drawContent()
                overlay()
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(resource),
            contentDescription = null,
            modifier = Modifier.size(size * 0.62f)
        )
    }
}

@Composable
internal fun InlineArt(
    resource: DrawableResource,
    modifier: Modifier = Modifier,
    size: Dp = 20.dp
) {
    Image(
        painter = painterResource(resource),
        contentDescription = null,
        modifier = modifier.size(size)
    )
}