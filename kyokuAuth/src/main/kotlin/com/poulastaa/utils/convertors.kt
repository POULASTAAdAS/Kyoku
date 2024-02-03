package com.poulastaa.utils

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.Payload
import com.poulastaa.data.model.auth.res.GoogleSignInResponse
import com.poulastaa.data.model.auth.stat.UserCreationStatus
import com.poulastaa.domain.dao.GoogleAuthUser
import com.poulastaa.utils.Constants.BASE_URL


fun GoogleAuthUser.toGoogleSignInResponse(status: UserCreationStatus): GoogleSignInResponse = GoogleSignInResponse(
    status = status,
    userName = this.userName,
    profilePic = this.profilePicUrl,
    data = emptyList() // todo add data
)

fun GoogleIdToken.toPayload() = Payload(
    sub = this.payload["sub"].toString(),
    userName = this.payload["name"].toString(),
    email = this.payload["email"].toString(),
    pictureUrl = this.payload["picture"].toString()
)

fun constructProfileUrl(): String = "$BASE_URL${EndPoints.ProfilePic.route}"
