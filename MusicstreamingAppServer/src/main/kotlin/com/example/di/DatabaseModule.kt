package com.example.di

import com.example.data.repository.user.EmailAuthUserRepositoryImpl
import com.example.data.repository.user.GoogleAuthUserRepositoryImpl
import com.example.domain.repository.user.EmailAuthUserRepository
import com.example.domain.repository.user.GoogleAuthUserRepository
import org.koin.dsl.module

val dbModule = module {
    single<EmailAuthUserRepository> {
        EmailAuthUserRepositoryImpl()
    }
    single<GoogleAuthUserRepository> {
        GoogleAuthUserRepositoryImpl()
    }
}