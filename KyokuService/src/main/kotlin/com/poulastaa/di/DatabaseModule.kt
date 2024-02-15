package com.poulastaa.di

import com.poulastaa.data.model.DbUser
import com.poulastaa.data.repository.JWTRepositoryImpl
import com.poulastaa.data.repository.SongRepositoryImpl
import com.poulastaa.data.repository.UserServiceRepositoryImpl
import com.poulastaa.data.repository.playlist.PlaylistRepositoryImpl
import com.poulastaa.data.repository.user_db.EmailAuthUserRepositoryImpl
import com.poulastaa.data.repository.user_db.GoogleAuthUserRepositoryImpl
import com.poulastaa.data.repository.user_db.PasskeyAuthUserRepositoryImpl
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.domain.repository.jwt.JWTRepository
import com.poulastaa.domain.repository.playlist.PlaylistRepository
import com.poulastaa.domain.repository.song.SongRepository
import com.poulastaa.domain.repository.user_db.EmailAuthUserRepository
import com.poulastaa.domain.repository.user_db.GoogleAuthUserRepository
import com.poulastaa.domain.repository.user_db.PasskeyAuthUserRepository
import io.ktor.server.application.*
import org.koin.dsl.module

fun provideJWTRepo(call: Application) = module {
    single<JWTRepository> {
        JWTRepositoryImpl(call)
    }
}

fun provideDatabaseRepo() = module {
    single<SongRepository> {
        SongRepositoryImpl()
    }
    single<PlaylistRepository> {
        PlaylistRepositoryImpl()
    }
    single<EmailAuthUserRepository> {
        EmailAuthUserRepositoryImpl()
    }
    single<GoogleAuthUserRepository> {
        GoogleAuthUserRepositoryImpl()
    }
    single<PasskeyAuthUserRepository> {
        PasskeyAuthUserRepositoryImpl()
    }
}

fun provideDbUsers() = module {
    single<DbUser> {
        DbUser(
            emailUser = get(),
            googleUser = get(),
            passekyUser = get()
        )
    }
}


fun provideService() = module {
    single<UserServiceRepository> {
        UserServiceRepositoryImpl(
            songRepository = get(),
            playlist = get(),
            users = get()
        )
    }
}