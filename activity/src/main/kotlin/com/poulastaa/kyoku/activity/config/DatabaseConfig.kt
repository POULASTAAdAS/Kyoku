package com.poulastaa.kyoku.activity.config

import com.poulastaa.kyoku.activity.database.content.repository.ArtistDatasource
import com.poulastaa.kyoku.activity.database.content.repository.ArtistInfoDatasource
import com.poulastaa.kyoku.activity.database.content.repository.SongDatasource
import com.poulastaa.kyoku.activity.database.content.repository.SongInfoDatasource
import com.poulastaa.kyoku.activity.database.playlist.repository.PlaylistDatasource
import com.poulastaa.kyoku.activity.database.playlist.repository.SongPlaylistDatasource
import com.poulastaa.kyoku.activity.database.user.repository.UserCountryDatasource
import com.poulastaa.kyoku.activity.database.user.repository.UserDatasource
import com.poulastaa.kyoku.activity.database.user.repository.UserTypeDatasource
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
    "hibernate.hbm2ddl.auto" to "none",
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

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.poulastaa.kyoku.activity.database.user.repository"],
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                UserDatasource::class,
                UserTypeDatasource::class,
                UserCountryDatasource::class
            ]
        )
    ],
    transactionManagerRef = "provideUserTransactionManager",
    entityManagerFactoryRef = "provideUserEntityManagerFactory"
)
class UserDatabaseConfig {
    @Bean
    @ConfigurationProperties(value = "spring.datasource.user")
    fun provideUserDatasourceProperties() = DataSourceProperties()

    @Bean
    fun provideUserDatasource(
        @Qualifier(value = "provideUserDatasourceProperties")
        dp: DataSourceProperties,
    ) = dp.initializeDataSourceBuilder()
        .type(HikariDataSource::class.java)
        .build().apply {
            maximumPoolSize = 10
            connectionTimeout = 30_000
            poolName = "user"
        }!!

    @Bean
    fun provideUserEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier(value = "provideUserDatasource") datasource: DataSource,
    ) = builder.dataSource(datasource)
        .packages("com.poulastaa.kyoku.activity.database.user.entity")
        .properties(prop)
        .persistenceUnit("user")
        .build()!!

    @Bean
    fun provideUserTransactionManager(
        @Qualifier(value = "provideUserEntityManagerFactory") factory: LocalContainerEntityManagerFactoryBean,
    ) = JpaTransactionManager(factory.`object`!!)
}