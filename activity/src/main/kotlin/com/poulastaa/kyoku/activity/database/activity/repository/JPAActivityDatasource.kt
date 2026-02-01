package com.poulastaa.kyoku.activity.database.activity.repository

import com.poulastaa.kyoku.activity.database.activity.entity.EntityUserArtist
import com.poulastaa.kyoku.activity.database.activity.entity.EntityUserGenre
import com.poulastaa.kyoku.activity.utils.UserId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserGenreDatasource : MongoRepository<EntityUserGenre, UserId>

@Repository
interface UserArtistDatasource : MongoRepository<EntityUserArtist, UserId>