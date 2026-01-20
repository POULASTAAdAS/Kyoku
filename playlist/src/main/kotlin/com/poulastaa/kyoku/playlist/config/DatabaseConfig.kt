package com.poulastaa.kyoku.playlist.config

import com.poulastaa.kyoku.playlist.database.content.repository.ArtistInfoDataSource
import com.poulastaa.kyoku.playlist.database.content.repository.SongDataSource
import com.poulastaa.kyoku.playlist.database.content.repository.SongInfoDataSource
import com.poulastaa.kyoku.playlist.database.playlist.repository.PlaylistDataSource
import com.poulastaa.kyoku.playlist.database.playlist.repository.SongPlaylistDataSource
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder
import org.springframework.context.annotation.*
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.poulastaa.kyoku.playlist.database.playlist.repository"],
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                PlaylistDataSource::class,
                SongPlaylistDataSource::class
            ]
        )
    ],
    transactionManagerRef = "playlistTransactionManger",
    entityManagerFactoryRef = "playlistEntityManagerFactory",
)
class PlaylistDatasourceConfig {
    @Bean(name = ["playlistDataSourceProperties"])
    @ConfigurationProperties(value = "spring.datasource.playlist")
    fun playlistDataSourceProperties() = DataSourceProperties()

    @Bean(name = ["playlistDataSourcee"])
    @Primary
    fun playlistDataSourcee(
        @Qualifier(value = "playlistDataSourceProperties") prop: DataSourceProperties,
    ): DataSource = prop.initializeDataSourceBuilder()
        .type(HikariDataSource::class.java)
        .build()
        .apply {
            maximumPoolSize = 10
            connectionTimeout = 30000
            poolName = "Playlist-HikariPool"
        }!!

    @Bean(name = ["playlistEntityManagerFactory"])
    @Primary
    fun playlistEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier(value = "playlistDataSourcee") datasource: DataSource,
    ): LocalContainerEntityManagerFactoryBean {
        val properties = HashMap<String, Any>()
        properties["hibernate.hbm2ddl.auto"] = "none"
        properties["hibernate.physical_naming_strategy"] =
            "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl"

        return builder.dataSource(datasource)
            .packages("com.poulastaa.kyoku.playlist.database.playlist.entity")
            .properties(properties)
            .persistenceUnit("playlist-mysql")
            .build()
    }

    @Bean(name = ["playlistTransactionManger"])
    @Primary
    fun playlistTransactionManger(
        @Qualifier(value = "playlistEntityManagerFactory") factory: LocalContainerEntityManagerFactoryBean,
    ) = JpaTransactionManager(factory.`object`!!)
}


@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.poulastaa.kyoku.playlist.database.content.repository"],
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                SongDataSource::class,
                SongInfoDataSource::class,
                ArtistInfoDataSource::class
            ]
        )
    ],
    transactionManagerRef = "contentTransactionManger",
    entityManagerFactoryRef = "contentEntityManagerFactory",
)
class ContentDatasourceConfig {
    @Bean(name = ["contentDataSourceProperties"])
    @ConfigurationProperties(value = "spring.datasource.content")
    fun contentDataSourceProperties() = DataSourceProperties()

    @Bean(name = ["contentDataSourcee"])
    fun contentDataSourcee(
        @Qualifier(value = "contentDataSourceProperties") prop: DataSourceProperties,
    ): DataSource = prop.initializeDataSourceBuilder()
        .type(HikariDataSource::class.java)
        .build()
        .apply {
            maximumPoolSize = 10
            connectionTimeout = 30000
            poolName = "Content-HikariPool"
        }!!

    @Bean(name = ["contentEntityManagerFactory"])
    fun contentEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier(value = "contentDataSourcee") datasource: DataSource,
    ): LocalContainerEntityManagerFactoryBean {
        val properties = HashMap<String, Any>()
        properties["hibernate.hbm2ddl.auto"] = "none"
        properties["hibernate.physical_naming_strategy"] =
            "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl"

        return builder.dataSource(datasource)
            .packages("com.poulastaa.kyoku.playlist.database.content.entity")
            .properties(properties)
            .persistenceUnit("content-mysql")
            .build()
    }

    @Bean(name = ["contentTransactionManger"])
    fun contentTransactionManger(
        @Qualifier(value = "contentEntityManagerFactory") factory: LocalContainerEntityManagerFactoryBean,
    ) = JpaTransactionManager(factory.`object`!!)
}