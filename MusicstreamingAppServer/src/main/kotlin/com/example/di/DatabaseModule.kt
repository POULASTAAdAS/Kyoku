package com.example.di

import com.example.data.repository.UserServiceRepositoryImpl
import com.example.data.repository.jwt.JWTRepositoryImpl
import com.example.data.repository.song_db.SongRepositoryImpl
import com.example.data.repository.user_db.EmailAuthUserRepositoryImpl
import com.example.data.repository.user_db.GoogleAuthUserRepositoryImpl
import com.example.domain.repository.UserServiceRepository
import com.example.domain.repository.jwt.JWTRepository
import com.example.domain.repository.song_db.SongRepository
import com.example.domain.repository.user_db.EmailAuthUserRepository
import com.example.domain.repository.user_db.GoogleAuthUserRepository
import io.ktor.server.application.*
import org.koin.dsl.module


fun provideJWTRepo(call: Application) = module {
    single<JWTRepository> {
        JWTRepositoryImpl(call)
    }
}

fun provideDatabaseRepo() = module {
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


fun provideService() = module {
    single<UserServiceRepository> {
        UserServiceRepositoryImpl(
            emailAuthUser = get(),
            jwtRepository = get(),
            googleAuthUser = get()
        )
    }
}
