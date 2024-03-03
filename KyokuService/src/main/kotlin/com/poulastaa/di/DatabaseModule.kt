package com.poulastaa.di

import com.poulastaa.data.model.utils.DbUsers
import com.poulastaa.data.repository.UserServiceRepositoryImpl
import com.poulastaa.data.repository.album.AlbumRepositoryImpl
import com.poulastaa.data.repository.artist.ArtistRepositoryImpl
import com.poulastaa.data.repository.genre.GenreRepositoryImpl
import com.poulastaa.data.repository.jwt.JWTRepositoryImpl
import com.poulastaa.data.repository.playlist.PlaylistRepositoryImpl
import com.poulastaa.data.repository.song.SongRepositoryImpl
import com.poulastaa.data.repository.users.EmailAuthUserRepositoryImpl
import com.poulastaa.data.repository.users.GoogleAuthUserRepositoryImpl
import com.poulastaa.data.repository.users.PasskeyAuthUserRepositoryImpl
import com.poulastaa.domain.repository.UserServiceRepository
import com.poulastaa.domain.repository.album.AlbumRepository
import com.poulastaa.domain.repository.aritst.ArtistRepository
import com.poulastaa.domain.repository.genre.GenreRepository
import com.poulastaa.domain.repository.jwt.JWTRepository
import com.poulastaa.domain.repository.playlist.PlaylistRepository
import com.poulastaa.domain.repository.song.SongRepository
import com.poulastaa.domain.repository.users.EmailAuthUserRepository
import com.poulastaa.domain.repository.users.GoogleAuthUserRepository
import com.poulastaa.domain.repository.users.PasskeyAuthUserRepository
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
    single<GenreRepository> {
        GenreRepositoryImpl()
    }
    single<ArtistRepository> {
        ArtistRepositoryImpl()
    }
    single<AlbumRepository> {
        AlbumRepositoryImpl()
    }
}

fun provideDbUsers() = module {
    single<DbUsers> {
        DbUsers(
            emailUser = get(),
            googleUser = get(),
            passekyUser = get()
        )
    }
}


fun provideService() = module {
    single<UserServiceRepository> {
        UserServiceRepositoryImpl(
            song = get(),
            playlist = get(),
            dbUsers = get(),
            genre = get(),
            artist = get(),
            album = get()
        )
    }
}