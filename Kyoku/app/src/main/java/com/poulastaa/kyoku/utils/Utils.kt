package com.poulastaa.kyoku.utils

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.poulastaa.kyoku.data.model.api.service.home.TimeType
import com.poulastaa.kyoku.data.model.api.service.setup.set_b_date.SetBDateReq
import java.net.CookieManager
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Base64


fun Char.isUserName(): Boolean =
    if (this == '_') true
    else this.isLetterOrDigit()

fun String.b64Decode(): ByteArray = Base64.getUrlDecoder().decode(this)

fun CookieManager.extractTokenOrCookie(): String =
    this.cookieStore.cookies[0].toString()

fun String.validateSpotifyLink(): Boolean {
    return (this.startsWith("https://open.spotify.com/playlist/") && this.contains("?si="))
}

fun String.toSpotifyPlaylistId(): String =
    this.removePrefix("https://open.spotify.com/playlist/").split("?si=")[0]

fun Long.toDate(): BDateFroMaterHelper {
    val bDate = BDateFroMaterHelper(
        date = "",
        status = BDateFroMaterHelperStatus.TO_OLD
    )
    val currentYear = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(System.currentTimeMillis()),
        ZoneId.systemDefault()
    )

    val date = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this),
        ZoneId.systemDefault()
    )

    val st = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

    return if (date.year < 1960) {
        bDate.copy(
            date = st,
            status = BDateFroMaterHelperStatus.TO_OLD
        )
    } else if (date.year > currentYear.year + 1) {
        bDate.copy(
            date = st,
            status = BDateFroMaterHelperStatus.FROM_FUTURE
        )
    } else if (date.year > (currentYear.year - 7)) {
        bDate.copy(
            status = BDateFroMaterHelperStatus.TO_YOUNG,
            date = st
        )
    } else {
        bDate.copy(
            date = st,
            status = BDateFroMaterHelperStatus.OK
        )
    }
}

fun Long.toSetBDateReq() = SetBDateReq(
    date = this
)

fun getHomeReqTimeType(): TimeType {
    val localTime = LocalDateTime.now().toLocalTime()

    val currentTime = localTime.format(DateTimeFormatter.ofPattern("hh")).toInt()
    val status = localTime.format(DateTimeFormatter.ofPattern("a")).uppercase()

    return if (status == "AM") {
        if (currentTime == 12 || currentTime < 4) TimeType.MID_NIGHT
        else if (currentTime < 10) TimeType.MORNING
        else TimeType.DAY
    } else {
        if (currentTime == 12 || currentTime < 6) TimeType.DAY
        else if (currentTime in 6..10) TimeType.NIGHT
        else TimeType.MID_NIGHT
    }
}


fun Modifier.shimmerEffect() = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "shimmerEffect")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200)
        ),
        label = "shimmerEffectStartOffsetX"
    )

    val colors = if (isSystemInDarkTheme()) listOf(
        Color(0xFF222831),
        Color(0xFF393E46),
        Color(0xFF222831),
    )
    else listOf(
        MaterialTheme.colorScheme.background,
        Color(0xFFEAF0F0),
        MaterialTheme.colorScheme.background,
    )

    background(
        brush = Brush.linearGradient(
            colors = colors,
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned {
        size = it.size
    }
}


















