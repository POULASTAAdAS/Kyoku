package com.poulastaa.kyoku.content.service.setup

import com.poulastaa.kyoku.content.model.dto.DtoGenre
import com.poulastaa.kyoku.content.utils.HasMoreGenre
import org.springframework.stereotype.Service

@Service
class SetupService(
    private val db: SetupJPADatasource,
    private val cache: SetUpCacheService,
) {
    fun getGenre(page: Int, size: Int, query: String?): Pair<List<DtoGenre>, HasMoreGenre> {
        val offSet = page * size

        val cachedGenre = cache.cacheGenrePerPage(offSet, size, query?.ifEmpty { null })

        return if (cachedGenre.first.isEmpty()) { // cache miss
            if (query.isNullOrEmpty().not()) { // empty query
                val list = db.getAllGenre().also { cache.setAllGenre(it) }
                    .filter { it.type.startsWith(query, ignoreCase = true) }
                    .drop(offSet)

                list.take(size) to (list.size > size)
            } else { // with query
                val list = db.getAllGenre().also { list ->
                    cache.setAllGenre(list)
                }.drop(offSet)

                list.take(size) to (list.size > size)
            }
        } else cachedGenre
    }
}