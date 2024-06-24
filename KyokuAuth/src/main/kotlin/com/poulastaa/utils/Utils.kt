package com.poulastaa.utils

import com.poulastaa.data.model.EndPoints

fun constructProfileUrl(): String = "${System.getenv("SERVICE_URL")}${EndPoints.ProfilePic.route}"
