package com.poulastaa.suggestion.data.di

import com.poulastaa.suggestion.data.repository.SuggestionRepositoryService
import com.poulastaa.suggestion.domain.repository.SuggestionRepository
import org.koin.dsl.module

fun provideSuggestionDataService() = module {
    single<SuggestionRepository> {
        SuggestionRepositoryService(db = get())
    }
}