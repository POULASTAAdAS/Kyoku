package com.example.di

import com.example.data.repository.song_db.SongRepositoryImpl
import com.example.data.repository.user_db.EmailAuthUserRepositoryImpl
import com.example.data.repository.user_db.GoogleAuthUserRepositoryImpl
import com.example.domain.repository.song_db.SongRepository
import com.example.domain.repository.user_db.EmailAuthUserRepository
import com.example.domain.repository.user_db.GoogleAuthUserRepository
import org.koin.dsl.module

val dbModule = module {
    single<EmailAuthUserRepository> {
        EmailAuthUserRepositoryImpl()
    }
    single<GoogleAuthUserRepository> {
        GoogleAuthUserRepositoryImpl()
    }
    single<SongRepository> {
        SongRepositoryImpl()
    }
}