package com.poulastaa.kyoku.user.config

import com.poulastaa.kyoku.user.database.playlist.repository.PlaylistDataSource
import com.poulastaa.kyoku.user.database.playlist.repository.UserPlaylistDataSource
import com.poulastaa.kyoku.user.database.user.repository.CountryDataSource
import com.poulastaa.kyoku.user.database.user.repository.UserDataSource
import com.poulastaa.kyoku.user.database.user.repository.UserTypeDataSource
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
    basePackages = ["com.poulastaa.kyoku.user.database.playlist.repository"],
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                UserPlaylistDataSource::class,
                PlaylistDataSource::class
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
    fun playlistEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier(value = "playlistDataSourcee") datasource: DataSource,
    ): LocalContainerEntityManagerFactoryBean {
        val properties = HashMap<String, Any>()
        properties["hibernate.hbm2ddl.auto"] = "none"
        properties["hibernate.physical_naming_strategy"] =
            "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl"

        return builder.dataSource(datasource)
            .packages("com.poulastaa.kyoku.user.database.playlist.entity")
            .properties(properties)
            .persistenceUnit("playlist-mysql")
            .build()
    }

    @Bean(name = ["playlistTransactionManger"])
    fun playlistTransactionManger(
        @Qualifier(value = "playlistEntityManagerFactory") factory: LocalContainerEntityManagerFactoryBean,
    ) = JpaTransactionManager(factory.`object`!!)
}

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = ["com.poulastaa.kyoku.user.database.user.repository"],
    includeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [
                UserTypeDataSource::class,
                UserDataSource::class,
                CountryDataSource::class
            ]
        )
    ],
    transactionManagerRef = "userTransactionManger",
    entityManagerFactoryRef = "userEntityManagerFactory",
)
class UserDatasourceConfig {
    @Bean(name = ["userDataSourceProperties"])
    @ConfigurationProperties(value = "spring.datasource.user")
    fun userDataSourceProperties() = DataSourceProperties()

    @Bean(name = ["userDataSourcee"])
    @Primary
    fun userDataSourcee(
        @Qualifier(value = "userDataSourceProperties") prop: DataSourceProperties,
    ): DataSource = prop.initializeDataSourceBuilder()
        .type(HikariDataSource::class.java)
        .build()
        .apply {
            maximumPoolSize = 10
            connectionTimeout = 30000
            poolName = "user-HikariPool"
        }!!

    @Bean(name = ["userEntityManagerFactory"])
    @Primary
    fun userEntityManagerFactory(
        builder: EntityManagerFactoryBuilder,
        @Qualifier(value = "userDataSourcee") datasource: DataSource,
    ): LocalContainerEntityManagerFactoryBean {
        val properties = HashMap<String, Any>()
        properties["hibernate.hbm2ddl.auto"] = "none"
        properties["hibernate.physical_naming_strategy"] =
            "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl"

        return builder.dataSource(datasource)
            .packages("com.poulastaa.kyoku.user.database.user.entity")
            .properties(properties)
            .persistenceUnit("user-mysql")
            .build()
    }

    @Bean(name = ["userTransactionManger"])
    @Primary
    fun userTransactionManger(
        @Qualifier(value = "userEntityManagerFactory") factory: LocalContainerEntityManagerFactoryBean,
    ) = JpaTransactionManager(factory.`object`!!)
}
