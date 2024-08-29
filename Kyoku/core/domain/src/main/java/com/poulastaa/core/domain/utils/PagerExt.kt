package com.poulastaa.core.domain.utils

data object NoInternetException : Exception() {
    private fun readResolve(): Any = NoInternetException
}

data object OtherRemoteException : Exception() {
    private fun readResolve(): Any = OtherRemoteException
}