package com.poulastaa.domain.repository.favourite

import com.poulastaa.data.model.utils.UserTypeHelper

interface FavouriteRepository {
    fun handelFavourite(songId: Long, helper: UserTypeHelper, operation: Boolean)

    fun insertIntoFavourite(songId: Long, helper: UserTypeHelper)
    fun removeFromFavourite(songId: Long, helper: UserTypeHelper)
}