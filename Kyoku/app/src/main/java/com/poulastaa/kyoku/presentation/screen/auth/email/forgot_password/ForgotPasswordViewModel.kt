package com.poulastaa.kyoku.presentation.screen.auth.email.forgot_password

import androidx.lifecycle.ViewModel
import com.poulastaa.kyoku.connectivity.NetworkObserver
import com.poulastaa.kyoku.domain.usecase.ValidateEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val connectivity: NetworkObserver,
    private val validateEmail: ValidateEmail
) : ViewModel() {

}