package com.poulastaa.core.presentation.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

// =========================== Device Previews ========================


// =========================== COMPACT ===========================
@Preview(
    name = "Light Small Landscape",
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:parent=pixel_4,orientation=landscape",
)
@Preview(
    name = "Dark Small Landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:parent=pixel_4,orientation=landscape",
)
annotation class PreviewSmallLandscape

@Preview(
    name = "Light Small Portrait",
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:parent=pixel_4",
)
@Preview(
    name = "Dark Small Portrait",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:parent=pixel_4",
)
annotation class PreviewSmallPortrait

// =========================== COMPACT ===========================
@Preview(
    name = "Light Compact Landscape",
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
)
@Preview(
    name = "Dark Compact Landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
)
annotation class PreviewCompactLandscape

@Preview(
    name = "Light Compact Portrait",
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:parent=pixel_9_pro_xl",
)
@Preview(
    name = "Dark Compact Portrait",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:parent=pixel_9_pro_xl",
)
annotation class PreviewCompactPortrait

// =========================== MEDIUM ===========================
@Preview(
    name = "Light Large Portrait",
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    device = "spec:parent=pixel_tablet,orientation=portrait",
)
@Preview(
    name = "Dark Large Portrait",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:parent=pixel_tablet,orientation=portrait",
)
annotation class PreviewLargePortrait


@Preview(
    name = "Light Large Landscape",
    uiMode = Configuration.UI_MODE_TYPE_NORMAL,
    device = "id:pixel_tablet",
)
@Preview(
    name = "Dark Large Landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "id:pixel_tablet",
)
annotation class PreviewLargeLandscape


// =========================== LANDSCAPE ===========================

@Preview(
    name = "Dark A Landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:parent=pixel_4,orientation=landscape",
)
@Preview(
    name = "Dark B Landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "spec:parent=pixel_9_pro_xl,orientation=landscape",
)
@Preview(
    name = "Dark C Landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = "id:pixel_tablet",
)
annotation class PreviewLandscape