package com.poulastaa.core.data.network

data object NoInternetException : Exception() {
    private fun readResolve(): Any = NoInternetException
}

data object OtherRemoteException : Exception() {
    private fun readResolve(): Any = OtherRemoteException
}