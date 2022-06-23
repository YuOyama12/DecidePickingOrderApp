package com.yuoyama12.decidepickingorderapp.di

import android.content.Context
import com.yuoyama12.decidepickingorderapp.data.GroupDao
import com.yuoyama12.decidepickingorderapp.data.GroupDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideGroupDatabase(@ApplicationContext context: Context): GroupDatabase {
        return GroupDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideGroupDao(groupDatabase: GroupDatabase): GroupDao {
        return groupDatabase.groupDao()
    }
}