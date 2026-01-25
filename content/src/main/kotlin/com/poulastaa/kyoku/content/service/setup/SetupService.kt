package com.poulastaa.kyoku.content.service.setup

import com.poulastaa.kyoku.content.model.dto.DtoGenre
import com.poulastaa.kyoku.content.utils.toDtoGenre
import org.springframework.stereotype.Service

@Service
class SetupService(
    private val db: SetupJPADatasource,
    private val cache: SetUpCacheService,
) {
    fun getGenre(page: Int, size: Int, query: String?): List<DtoGenre> {
        val correctedPage = if (page <= 1) 0 else page

        val cachedGenre = cache.cacheGenrePerPage(correctedPage, size, query?.ifEmpty { null })
        return cachedGenre.ifEmpty {
            val offSet = correctedPage * size
            db.getGenre().map { it.toDtoGenre() }.also { list ->
                cache.setAllGenreByType(list)
                cache.setAllGenreByPopularity(list)
            }.drop(offSet).take(size)
        }
    }
}