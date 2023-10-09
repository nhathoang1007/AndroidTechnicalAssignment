package com.example.domain.di

import com.example.domain.repository.CoreRepository
import com.example.domain.usecase.GetTeamsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetTeamsUseCase(repository: CoreRepository) = GetTeamsUseCase(repository)
}