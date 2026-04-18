package com.poulastaa.kyoku.search.database.repository

import com.poulastaa.kyoku.search.database.entity.EntityCountry
import com.poulastaa.kyoku.search.utils.CountryId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Service

@Service
interface ContentJPARepository : JpaRepository<EntityCountry, CountryId>
