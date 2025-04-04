package com.poulastaa.kyoku.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.poulastaa.auth.presentation.email.forgot_password.ForgotPasswordRootScreen
import com.poulastaa.auth.presentation.email.forgot_password.ForgotPasswordViewModel
import com.poulastaa.auth.presentation.email.login.EmailLogInRootScreen
import com.poulastaa.auth.presentation.email.signup.EmailSignUpRootScreen
import com.poulastaa.auth.presentation.intro.IntroRootScreen
import com.poulastaa.core.domain.model.DtoCoreScreens
import com.poulastaa.core.domain.model.ExploreScreenType
import com.poulastaa.core.domain.model.SavedScreen
import com.poulastaa.core.domain.model.ViewType
import com.poulastaa.explore.domain.model.ExploreAllowedNavigationScreen
import com.poulastaa.explore.presentation.search.album.ExploreAlbumRootScreen
import com.poulastaa.explore.presentation.search.all_from_artist.AllFromArtistRootScreen
import com.poulastaa.explore.presentation.search.artist.ExploreArtistRootScreen
import com.poulastaa.main.domain.model.MainAllowedNavigationScreens
import com.poulastaa.main.presentation.main.MainRootScreen
import com.poulastaa.main.presentation.main.ScreensCore
import com.poulastaa.profile.domain.model.ProfileAllowedNavigationScreen
import com.poulastaa.profile.presentation.ProfileRootScreen
import com.poulastaa.settings.domain.model.SettingsAllowedNavigationScreens
import com.poulastaa.settings.presentation.SettingsRootScreen
import com.poulastaa.setup.presentation.pic_artist.PicArtistRootScreen
import com.poulastaa.setup.presentation.pic_genre.PicGenreRootScreen
import com.poulastaa.setup.presentation.set_bdate.SetBDateRootScreen
import com.poulastaa.setup.presentation.spotify_playlist.ImportPlaylistRootScreen
import com.poulastaa.view.domain.model.ViewArtistAllowedNavigationScreen
import com.poulastaa.view.domain.model.ViewOtherAllowedNavigationScreen
import com.poulastaa.view.presentation.artist.ViewArtistRootScreen
import com.poulastaa.view.presentation.others.ViewOtherRootScreen

private const val DEFAULT_ANIMATION_TIME = 600

@Composable
fun RootNavigation(
    nav: NavHostController,
    screen: Screens,
    toggleThem: () -> Unit,
) {
    NavHost(
        navController = nav,
        startDestination = screen
    ) {
        authGraph(nav)
        setupGraph(nav)
        coreGraph(nav, toggleThem)
        viewGraph(nav)
        exploreGraph(nav)
    }
}

private fun NavGraphBuilder.authGraph(nav: NavHostController) {
    composable<Screens.Auth.Intro> {
        IntroRootScreen(
            navigateToEmailLogIn = {
                nav.navigate(Screens.Auth.EmailLogIn)
            },
            navigateOnSuccess = {
                nav.popBackStack()
                when (it) {
                    SavedScreen.IMPORT_SPOTIFY_PLAYLIST -> nav.navigate(Screens.SetUp.ImportSpotifyPlaylist)
                    SavedScreen.SET_B_DATE -> nav.navigate(Screens.SetUp.SetBirthDate)
                    SavedScreen.PIC_GENRE -> nav.navigate(Screens.SetUp.PickGenre)
                    SavedScreen.PIC_ARTIST -> nav.navigate(Screens.SetUp.PickArtist)
                    SavedScreen.MAIN -> nav.navigate(Screens.Core.Main(true))

                    else -> Unit
                }
            }
        )
    }

    composable<Screens.Auth.EmailLogIn>(
        enterTransition = {
            fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME)) + slideInHorizontally(
                animationSpec = tween(DEFAULT_ANIMATION_TIME),
                initialOffsetX = { it }
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(DEFAULT_ANIMATION_TIME))
        }
    ) {
        EmailLogInRootScreen(
            navigateToEmailSignUp = {
                nav.navigate(Screens.Auth.EmailSignUp)
            },
            navigateToForgotPassword = {
                nav.navigate(Screens.Auth.ForgotPassword(it))
            },
            navigateToScreen = {
                nav.popBackStack()

                when (it) {
                    SavedScreen.IMPORT_SPOTIFY_PLAYLIST -> nav.navigate(Screens.SetUp.ImportSpotifyPlaylist)
                    SavedScreen.SET_B_DATE -> nav.navigate(Screens.SetUp.SetBirthDate)
                    SavedScreen.PIC_GENRE -> nav.navigate(Screens.SetUp.PickGenre)
                    SavedScreen.PIC_ARTIST -> nav.navigate(Screens.SetUp.PickArtist)
                    SavedScreen.MAIN -> nav.navigate(Screens.Core.Main(true))

                    else -> Unit
                }
            }
        )
    }

    composable<Screens.Auth.EmailSignUp>(
        enterTransition = {
            fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                    slideInHorizontally(
                        animationSpec = tween(DEFAULT_ANIMATION_TIME),
                        initialOffsetX = { it })
        },
        exitTransition = {
            fadeOut(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                    slideOutHorizontally(
                        animationSpec = tween(DEFAULT_ANIMATION_TIME),
                        targetOffsetX = { it }
                    )
        }
    ) {
        EmailSignUpRootScreen(
            navigateToLogin = {
                nav.navigate(Screens.Auth.EmailLogIn) {
                    popUpTo(Screens.Auth.EmailLogIn) {
                        inclusive = true
                    }
                }
            },
            navigateToScreen = {
                nav.popBackStack()

                when (it) {
                    SavedScreen.IMPORT_SPOTIFY_PLAYLIST -> nav.navigate(Screens.SetUp.ImportSpotifyPlaylist)
                    SavedScreen.SET_B_DATE -> nav.navigate(Screens.SetUp.SetBirthDate)
                    SavedScreen.PIC_GENRE -> nav.navigate(Screens.SetUp.PickGenre)
                    SavedScreen.PIC_ARTIST -> nav.navigate(Screens.SetUp.PickArtist)
                    SavedScreen.MAIN -> nav.navigate(Screens.Core.Main(true))

                    else -> Unit
                }
            }
        )
    }

    composable<Screens.Auth.ForgotPassword>(
        enterTransition = {
            fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                    slideInVertically(
                        animationSpec = tween(DEFAULT_ANIMATION_TIME),
                        initialOffsetY = { it })
        },
        exitTransition = {
            fadeOut(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                    slideOutVertically(
                        animationSpec = tween(DEFAULT_ANIMATION_TIME),
                        targetOffsetY = { it }
                    )
        }
    ) { stackEntry ->
        val email = stackEntry.toRoute<Screens.Auth.ForgotPassword>().email

        val viewmodel = hiltViewModel<ForgotPasswordViewModel>()
        email?.let { viewmodel.loadEmail(it) }

        ForgotPasswordRootScreen(
            viewModel = viewmodel,
            navigateBack = {
                nav.popBackStack()
            }
        )
    }
}

private fun NavGraphBuilder.setupGraph(nav: NavHostController) {
    composable<Screens.SetUp.ImportSpotifyPlaylist> {
        ImportPlaylistRootScreen {
            nav.navigate(Screens.SetUp.SetBirthDate)
        }
    }

    composable<Screens.SetUp.SetBirthDate>(
        enterTransition = {
            fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                    slideInHorizontally(
                        animationSpec = tween(DEFAULT_ANIMATION_TIME),
                        initialOffsetX = { it }
                    )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                    slideOutHorizontally(
                        animationSpec = tween(DEFAULT_ANIMATION_TIME),
                        targetOffsetX = { it }
                    )
        }
    ) {
        SetBDateRootScreen(
            navigateBack = {
                nav.popBackStack()
            },
            navigateToPicGenre = {
                nav.navigate(Screens.SetUp.PickGenre) {
                    popUpTo(Screens.SetUp.PickGenre) {
                        inclusive = true
                    }
                }
            }
        )
    }

    composable<Screens.SetUp.PickGenre>(
        enterTransition = {
            fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                    slideInHorizontally(
                        animationSpec = tween(DEFAULT_ANIMATION_TIME),
                        initialOffsetX = { it }
                    )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                    slideOutHorizontally(
                        animationSpec = tween(DEFAULT_ANIMATION_TIME),
                        targetOffsetX = { it }
                    )
        }
    ) {
        PicGenreRootScreen {
            nav.navigate(Screens.SetUp.PickArtist) {
                popUpTo(Screens.SetUp.PickArtist) {
                    inclusive = true
                }
            }
        }
    }

    composable<Screens.SetUp.PickArtist>(
        enterTransition = {
            fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME)) +
                    slideInHorizontally(
                        animationSpec = tween(DEFAULT_ANIMATION_TIME),
                        initialOffsetX = { it }
                    )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(DEFAULT_ANIMATION_TIME))
        }
    ) {
        PicArtistRootScreen {
            nav.navigate(Screens.Core.Main(true)) {
                popUpTo(Screens.Core.Main(true)) {
                    inclusive = true
                }
            }
        }
    }
}

private fun NavGraphBuilder.coreGraph(
    nav: NavHostController,
    toggleThem: () -> Unit,
) {
    composable<Screens.Core.Main>(
        enterTransition = { fadeIn(animationSpec = tween(DEFAULT_ANIMATION_TIME)) }
    ) {
        val payload = it.toRoute<Screens.Core.Main>()

        MainRootScreen(
            isInitial = payload.isInitial,
            screen = if (payload.isHome) ScreensCore.Home else ScreensCore.Library,
            navigateToCoreScreen = { dtoScreens ->
                when (dtoScreens) {
                    DtoCoreScreens.History -> nav.navigate(dtoScreens.toCoreScreen())
                    DtoCoreScreens.Profile -> nav.navigate(dtoScreens.toCoreScreen())
                    DtoCoreScreens.Settings -> nav.navigate(dtoScreens.toCoreScreen())
                    DtoCoreScreens.ToggleTheme -> toggleThem()
                }
            },
            navigateToOtherScreen = { screens ->
                when (screens) {
                    is MainAllowedNavigationScreens.NavigateToView -> when (screens.type) {
                        ViewType.ARTIST -> nav.navigate(Screens.View.Artist(screens.otherId))

                        else -> nav.navigate(
                            Screens.View.Other(
                                otherId = screens.otherId,
                                type = screens.type
                            )
                        )
                    }

                    is MainAllowedNavigationScreens.NavigateToExplore -> when (screens.type) {
                        ExploreScreenType.ARTIST -> TODO("Add explore artist screen")
                        ExploreScreenType.ALBUM -> nav.navigate(Screens.Explore.ExploreAlbum)
                    }
                }
            }
        )
    }

    composable<Screens.Core.Profile> {
        ProfileRootScreen(
            navigate = {
                when (it) {
                    ProfileAllowedNavigationScreen.PLAYLIST -> TODO("Add all saved view playlist screen")
                    ProfileAllowedNavigationScreen.ALBUM -> TODO("Add all saved view album screen")
                    ProfileAllowedNavigationScreen.ARTIST -> TODO("Add all saved view artist screen")
                    ProfileAllowedNavigationScreen.FAVOURITE -> nav.navigate(
                        Screens.View.Other(
                            otherId = 0,
                            type = ViewType.FAVOURITE
                        )
                    )

                    ProfileAllowedNavigationScreen.LIBRARY -> nav.navigate(Screens.Core.Main(isHome = false)) {
                        popUpTo(Screens.Core.Main()) {
                            inclusive = true
                        }
                    }
                }
            },
            navigateBack = {
                nav.popBackStack()
            }
        )
    }

    composable<Screens.Core.History> {

    }

    composable<Screens.Core.Settings> {
        SettingsRootScreen(
            navigateBack = {
                nav.popBackStack()
            },
            onLogOut = {
                nav.navigate(Screens.Auth.Intro) {
                    popUpTo(Screens.Auth.Intro) {
                        inclusive = true
                    }
                }
            },
            navigate = {
                when (it) {
                    SettingsAllowedNavigationScreens.PROFILE -> nav.navigate(Screens.Core.Profile)
                    SettingsAllowedNavigationScreens.HISTORY -> nav.navigate(Screens.Core.History)
                }
            }
        )
    }
}


private fun NavGraphBuilder.viewGraph(nav: NavHostController) {
    composable<Screens.View.Artist> {
        val payload = it.toRoute<Screens.View.Artist>()

        ViewArtistRootScreen(
            artistId = payload.artistId,
            navigate = { screen ->
                when (screen) {
                    is ViewArtistAllowedNavigationScreen.Explore -> nav.navigate(
                        Screens.Explore.AllFromArtist(
                            screen.artistId
                        )
                    )

                    is ViewArtistAllowedNavigationScreen.ViewAlbum -> nav.navigate(
                        Screens.View.Other(
                            otherId = screen.albumId,
                            type = ViewType.ALBUM
                        )
                    )
                }
            },
            navigateBack = {
                nav.popBackStack()
            }
        )
    }

    composable<Screens.View.Other> {
        val payload = it.toRoute<Screens.View.Other>()

        ViewOtherRootScreen(
            otherId = payload.otherId,
            viewType = payload.type,
            navigate = { screen ->
                when (screen) {
                    is ViewOtherAllowedNavigationScreen.Artist -> nav.navigate(
                        Screens.View.Artist(
                            screen.artistId
                        )
                    )
                }
            },
            navigateBack = {
                nav.popBackStack()
            }
        )
    }
}

private fun NavGraphBuilder.exploreGraph(nav: NavHostController) {
    composable<Screens.Explore.AllFromArtist> {
        val payload = it.toRoute<Screens.Explore.AllFromArtist>()

        AllFromArtistRootScreen(
            artistId = payload.artistId,
            navigate = { screen ->
                when (screen) {
                    is ExploreAllowedNavigationScreen.Album -> nav.navigate(
                        Screens.View.Other(
                            screen.albumId,
                            ViewType.ALBUM
                        )
                    )
                }
            },
            navigateBack = {
                nav.popBackStack()
            }
        )
    }

    composable<Screens.Explore.ExploreAlbum> {
        ExploreAlbumRootScreen(
            navigate = { screen ->
                when (screen) {
                    is ExploreAllowedNavigationScreen.Album -> nav.navigate(
                        Screens.View.Other(
                            screen.albumId,
                            ViewType.ALBUM
                        )
                    )
                }
            },
            navigateBack = {
                nav.popBackStack()
            }
        )
    }

    composable<Screens.Explore.ExploreArtist> {
        ExploreArtistRootScreen(
            navigateToArtist = { artistId ->
                nav.navigate(Screens.View.Artist(artistId))
            },
            navigateBack = {
                nav.popBackStack()
            }
        )
    }
}