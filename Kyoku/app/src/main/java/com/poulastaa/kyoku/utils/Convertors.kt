package com.poulastaa.kyoku.utils

import com.google.gson.JsonParser
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyJson
import com.poulastaa.kyoku.data.model.api.auth.passkey.CreatePasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.GetPasskeyUserReq
import com.poulastaa.kyoku.data.model.api.req.PasskeyAuthReq
import com.poulastaa.kyoku.data.model.auth.root.RootAuthScreenState
import com.poulastaa.kyoku.utils.Constants.AUTH_TYPE_PASSKEY
import com.poulastaa.kyoku.utils.Constants.TYPE_PASSKEY_AUTH_REQ

fun RootAuthScreenState.toPasskeyAuthRequest() = PasskeyAuthReq(
    type = TYPE_PASSKEY_AUTH_REQ,
    authType = AUTH_TYPE_PASSKEY,
    email = this.passkeyEmail,
    displayName = this.passkeyEmail.removeSuffix("@gmail.com")
)

fun String.toPasskeyJson(): PasskeyJson {
    val json = JsonParser().parse(this).asJsonObject

    val type = json["type"].asString
    json.remove("type")

    val token = json["token"].asString
    json.remove("token")

    val challenge = json["challenge"].asString.removeSuffix("==")

    return PasskeyJson(
        type = type,
        challenge = challenge,
        req = json.toString(),
        token = token
    )
}

fun String.toCreatePasskeyUserReq(email: String, token: String) = CreatePasskeyUserReq(
    id = this,
    email = email,
    userName = email.removeSuffix("@gmail.com"),
    token = token
)


fun String.toGetPasskeyUserReq(token: String) = GetPasskeyUserReq(
    id = this,
    token = token
)