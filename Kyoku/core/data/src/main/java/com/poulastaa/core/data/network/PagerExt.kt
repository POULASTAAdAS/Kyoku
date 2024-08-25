package com.poulastaa.core.data.network

data object CustomNullPointerException : Exception() {
    private fun readResolve(): Any = CustomNullPointerException
}