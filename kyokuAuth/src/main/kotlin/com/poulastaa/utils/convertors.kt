package com.poulastaa.utils

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.poulastaa.data.model.EndPoints
import com.poulastaa.data.model.auth.google.Payload
import com.poulastaa.data.model.User
import com.poulastaa.data.model.auth.google.GoogleAuthResponse
import com.poulastaa.data.model.auth.UserCreationStatus
import com.poulastaa.data.model.auth.passkey.PasskeyAuthResponse
import com.poulastaa.domain.dao.GoogleAuthUser
import com.poulastaa.domain.dao.PasskeyAuthUser
import com.poulastaa.utils.Constants.BASE_URL


fun GoogleAuthUser.toGoogleAuthResponse(status: UserCreationStatus): GoogleAuthResponse = GoogleAuthResponse(
    status = status,
    user = User(
        userName = this.userName,
        profilePic = this.profilePicUrl
    ),
    data = emptyList() // todo add data
)

fun PasskeyAuthUser.toPasskeyAuthResponse(status: UserCreationStatus): PasskeyAuthResponse = PasskeyAuthResponse(
    status = status,
    user = User(
        userName = this.displayName,
        profilePic = this.profilePic
    ),
    data = emptyList() // todo add data
)


fun GoogleIdToken.toPayload() = Payload(
    sub = this.payload["sub"].toString(),
    userName = this.payload["name"].toString(),
    email = this.payload["email"].toString(),
    pictureUrl = this.payload["picture"].toString()
)

fun constructProfileUrl(): String = "$BASE_URL${EndPoints.ProfilePic.route}"
