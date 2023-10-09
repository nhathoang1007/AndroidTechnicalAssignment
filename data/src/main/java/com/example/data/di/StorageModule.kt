package com.example.data.di

import com.example.data.store.MyPref
import com.example.data.store.MyPrefImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class StorageModule {

    @Binds
    abstract fun bindMyPref(repository: MyPrefImpl): MyPref
}