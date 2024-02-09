package com.poulastaa.kyoku.data.remote

import com.poulastaa.kyoku.data.model.api.auth.email.EmailLogInResponse
import com.poulastaa.kyoku.data.model.api.auth.email.EmailSignUpResponse
import com.poulastaa.kyoku.data.model.api.auth.email.ResendVerificationMailResponse
import com.poulastaa.kyoku.data.model.api.auth.email.SendForgotPasswordMail
import com.poulastaa.kyoku.data.model.api.auth.google.GoogleAuthResponse
import com.poulastaa.kyoku.data.model.api.auth.passkey.PasskeyAuthResponse
import com.poulastaa.kyoku.data.model.api.auth.passkey.CreatePasskeyUserReq
import com.poulastaa.kyoku.data.model.api.auth.passkey.GetPasskeyUserReq
import com.poulastaa.kyoku.data.model.api.req.EmailLogInReq
import com.poulastaa.kyoku.data.model.api.req.EmailSignUpReq
import com.poulastaa.kyoku.data.model.api.req.GoogleAuthReq
import com.poulastaa.kyoku.data.model.api.req.PasskeyAuthReq
import com.poulastaa.kyoku.data.model.auth.email.signup.EmailVerificationResponse
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthApi {
    @POST("/api/auth")
    suspend fun passkeyReq(
        @Body request: PasskeyAuthReq
    ): ResponseBody

    @POST("/api/auth/createPasskeyUser")
    suspend fun createPasskeyUser(
        @Body request: CreatePasskeyUserReq
    ): PasskeyAuthResponse

    @POST("/api/auth/getPasskeyUser")
    suspend fun getPasskeyUser(
        @Body request: GetPasskeyUserReq
    ): PasskeyAuthResponse

    @POST("/api/auth")
    suspend fun googleReq(
        @Body request: GoogleAuthReq
    ): GoogleAuthResponse

    @POST("/api/auth")
    suspend fun emailSignUp(
        @Body request: EmailSignUpReq
    ): EmailSignUpResponse

    @GET("/api/auth/emailVerificationCheck")
    suspend fun isEmailVerified(
        @Query("email") email: String
    ): EmailVerificationResponse

    @GET("/api/auth/resendVerificationMail")
    suspend fun resendVerificationMail(
        @Query("email") email: String
    ): ResendVerificationMailResponse

    @POST("/api/auth")
    suspend fun emailLogIn(
        @Body request: EmailLogInReq
    ): EmailLogInResponse

    @GET("/api/auth/forgotPassword")
    suspend fun forgotPassword(
        @Query("email") email: String
    ): SendForgotPasswordMail
}