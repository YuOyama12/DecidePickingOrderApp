package com.yuoyama12.decidepickingorderapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val SORTING_PREFERENCE_NAME = "SortingPreference"

@Module
@InstallIn(SingletonComponent::class)
class PreferenceModule {

    @Singleton
    @Provides
    fun provideSortingPreferencesDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = {
                context.preferencesDataStoreFile(SORTING_PREFERENCE_NAME)
            }
        )
    }

}