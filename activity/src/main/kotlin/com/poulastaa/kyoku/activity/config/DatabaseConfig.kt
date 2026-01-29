package com.poulastaa.kyoku.activity.config

import com.poulastaa.kyoku.activity.database.content.repository.ArtistDatasource
import com.poulastaa.kyoku.activity.database.content.repository.ArtistInfoDatasource
import com.poulastaa.kyoku.activity.database.content.repository.SongDatasource
import com.poulastaa.kyoku.activity.database.content.repository.SongInfoDatasource
import com.poulastaa.kyoku.activity.database.playlist.repository.PlaylistDatasource
import com.poulastaa.kyoku.activity.database.playlist.repository.SongPlaylistDatasource
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

private val prop = mapOf(
    "hibernate.hbm2ddl.auto" to "non",
    "hibernate.physical_naming_strategy" to "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl",
    "hibernate.show_sql" to true,
    "hibernate.format_sql" to true,
)

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.poulastaa.kyoku.activity.database.content.repository"],
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                SongDatasource::class,
                SongInfoDatasource::class,
                ArtistDatasource::class,
                ArtistInfoDatasource::class
            ]
        )
    ],
    transactionManagerRef = "provideContentTransactionManager",
    entityManagerFactoryRef = "provideContentEntityManagerFactory"
)
class ContentDatabaseConfig {
    @Bean
    @ConfigurationProperties(value = "spring.datasource.content")
    fun provideContentDatasourceProperties() = DataSourceProperties()

    @Bean
    @Primary
    fun provideContentDatasource(
        @Qualifier(value = "provideContentDatasourceProperties")
        dataSourceProperties: DataSourceProperties,
    ) = dataSourceProperties.initializeDataSourceBuilder()
        .type(HikariDataSource::class.java)
        .build().apply {
            maximumPoolSize = 10
            connectionTimeout = 30_000
            poolName = "Content"
        }!!

    @Bean
    @Primary
    fun provideContentEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier(value = "provideContentDatasource") datasource: DataSource,
    ) = builder.dataSource(datasource)
        .packages("com.poulastaa.kyoku.activity.database.content.entity")
        .properties(prop)
        .persistenceUnit("content")
        .build()!!

    @Bean
    @Primary
    fun provideContentTransactionManager(
        @Qualifier(value = "provideContentEntityManagerFactory") factory: LocalContainerEntityManagerFactoryBean,
    ) = JpaTransactionManager(factory.`object`!!)
}

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.poulastaa.kyoku.activity.database.playlist.repository"],
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                PlaylistDatasource::class,
                SongPlaylistDatasource::class
            ]
        )
    ],
    transactionManagerRef = "providePlaylistTransactionManager",
    entityManagerFactoryRef = "providePlaylistEntityManagerFactory"
)
class PlaylistDatabaseConfig {
    @Bean
    @ConfigurationProperties(value = "spring.datasource.playlist")
    fun providePlaylistDatasourceProperties() = DataSourceProperties()

    @Bean
    fun providePlaylistDatasource(
        @Qualifier(value = "providePlaylistDatasourceProperties")
        dataSourceProperties: DataSourceProperties,
    ) = dataSourceProperties.initializeDataSourceBuilder()
        .type(HikariDataSource::class.java)
        .build().apply {
            maximumPoolSize = 10
            connectionTimeout = 30_000
            poolName = "playlist"
        }!!

    @Bean
    fun providePlaylistEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier(value = "providePlaylistDatasource") datasource: DataSource,
    ) = builder.dataSource(datasource)
        .packages("com.poulastaa.kyoku.activity.database.playlist.entity")
        .properties(prop)
        .persistenceUnit("playlist")
        .build()!!

    @Bean
    fun providePlaylistTransactionManager(
        @Qualifier(value = "providePlaylistEntityManagerFactory") factory: LocalContainerEntityManagerFactoryBean,
    ) = JpaTransactionManager(factory.`object`!!)
}
