package com.poulastaa.data.repository.suggest_grenre

import com.poulastaa.data.model.db_table.ArtistTable
import com.poulastaa.data.model.db_table.CountryGenreRelationTable
import com.poulastaa.data.model.db_table.CountryTable
import com.poulastaa.data.model.db_table.GenreTable
import com.poulastaa.data.model.setup.suggest_genre.SuggestGenreReq
import com.poulastaa.data.model.setup.suggest_genre.SuggestGenreResponse
import com.poulastaa.domain.dao.Artist
import com.poulastaa.domain.dao.Country
import com.poulastaa.domain.dao.CountryGenreRelation
import com.poulastaa.domain.dao.Genre
import com.poulastaa.domain.repository.suggest_genre.SuggestGenreRepository
import com.poulastaa.plugins.dbQuery

class SuggestGenreRepositoryImpl : SuggestGenreRepository {
    private fun List<Pair<Int, String>>.removeDuplicateGenre(
        oldList: List<String>
    ): Map<Int, String> {
        val filteredMap = this.filterNot { (_, u) -> oldList.contains(u) }
        return filteredMap.sortedBy { it.first }.take(10).toMap()  // works like paging
    }

    private fun List<Pair<Int, String>>.removeDuplicateArtist(): List<String> {
        return this.associateBy({ it.first }, { it.second }).values.toList()
    }

    override suspend fun suggestGenre(req: SuggestGenreReq): List<SuggestGenreResponse> {
        val responseList = ArrayList<SuggestGenreResponse>()

        val countryId = dbQuery {
            Country.find {
                CountryTable.name eq req.countryName
            }.firstOrNull()?.id?.value
        } ?: return emptyList()

        val genreIdList = dbQuery {
            CountryGenreRelation.find {
                CountryGenreRelationTable.countryId eq countryId
            }.map { it.genreId }
        }


        val genreMap = dbQuery {
            Genre.find {
                GenreTable.id inList genreIdList
            }.map {
                it.id.value to it.genre
            }.removeDuplicateGenre(req.alreadySendGenreList ?: emptyList())
        }


        val artistUrlList = dbQuery {
            Artist.find {
                ArtistTable.genre inList genreMap.keys
            }.map {
                it.genre to it.profilePicUrl
            }.removeDuplicateArtist()
        }

        println(
            "genreLen: ${genreMap.keys.size}" +
                    " artistUrlLen: ${artistUrlList.size}"
        )
        println(
            "genreLen: ${genreMap.keys.size}" +
                    " artistUrlLen: ${artistUrlList.size}"
        )
        println(
            "genreLen: ${genreMap.keys.size}" +
                    " artistUrlLen: ${artistUrlList.size}"
        )
        println(
            "genreLen: ${genreMap.keys.size}" +
                    " artistUrlLen: ${artistUrlList.size}"
        )
        println(
            "genreLen: ${genreMap.keys.size}" +
                    " artistUrlLen: ${artistUrlList.size}"
        )


//        for (i in artistUrlList.indices) {
//            responseList.add(
//                SuggestGenreResponse(
//                    genre = genreList[i],
//                    artistUrl = artistUrlList[i]
//                )
//            )
//        }
//
        return responseList
    }
}