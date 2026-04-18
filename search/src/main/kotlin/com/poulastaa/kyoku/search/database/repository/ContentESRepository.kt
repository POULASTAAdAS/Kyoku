package com.poulastaa.kyoku.search.database.repository

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.FieldValue
import co.elastic.clients.elasticsearch._types.SortOrder
import com.poulastaa.kyoku.search.database.entity.EntityEsArtist
import com.poulastaa.kyoku.search.utils.ArtistTitle
import com.poulastaa.kyoku.search.utils.CountryCode
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class ContentESRepository(
    private val client: ElasticsearchClient,
    @param:Value("\${elasticsearch.artist.index}")
    private val index: String,
) {
    fun getMostPopularArtistByCountry(size: Int, country: CountryCode) = client.search(
        { req ->
            req.index(index)
                .size(size)
                .query { q ->
                    q.term {
                        it.field(EntityEsArtist::countryCode.name).value(country)
                    }
                }
                .sort { s ->
                    s.field { it.field(EntityEsArtist::followers.name).order(SortOrder.Desc) }
                }
        },
        EntityEsArtist::class.java
    ).hits().hits().mapNotNull { it.source() }.map { it.toDtoArtist() }

    fun getArtistByCountry(q: String, country: CountryCode, size: Int, offSet: Int) = client.search(
        { req ->
            req.index(index)
                .size(size)
                .from(offSet)
                .query { query ->
                    query.bool { bool ->
                        bool.must { m -> m.match { it.field(EntityEsArtist::name.name).query(q) } }
                            .filter { f -> f.term { it.field(EntityEsArtist::countryCode.name).value(country) } }
                    }
                }.sort { s -> s.field { it.field(EntityEsArtist::followers.name).order(SortOrder.Desc) } }
        },
        EntityEsArtist::class.java
    ).hits().hits().mapNotNull { it.source() }.map { it.toDtoArtist() }

    fun getArtistByCountry(
        q: String,
        country: CountryCode,
        size: Int,
        offSet: Int,
        excludeList: List<ArtistTitle>,
    ) = client.search(
        { req ->
            req.index(index)
                .size(size)
                .from(offSet)
                .query { query ->
                    query.bool { bool ->
                        bool.must { m -> m.match { it.field(EntityEsArtist::name.name).query(q) } }
                            .filter { f -> f.term { it.field(EntityEsArtist::countryCode.name).value(country) } }
                            .mustNot { mn ->
                                mn.terms { t ->
                                    t.field(EntityEsArtist::name.name).terms { tv ->
                                        tv.value(excludeList.map { FieldValue.of(it) })
                                    }
                                }
                            }
                    }
                }.sort { s -> s.field { it.field(EntityEsArtist::followers.name).order(SortOrder.Desc) } }
        },
        EntityEsArtist::class.java
    ).hits().hits().mapNotNull { it.source() }.map { it.toDtoArtist() }
}