package com.poulastaa.core.domain

data object NoInternetException : Exception() {
    private fun readResolve(): Any = NoInternetException
}

data object UnknownRemoteException : Exception() {
    private fun readResolve(): Any = UnknownRemoteException
}