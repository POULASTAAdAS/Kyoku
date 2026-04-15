package com.poulastaa.kyoku.search.database.repository

import com.poulastaa.kyoku.search.database.entity.EntityCountry
import org.springframework.stereotype.Service

@Service
interface ContentJPARepository {
    fun getAllCountry(): List<EntityCountry>
}