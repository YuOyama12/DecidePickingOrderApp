package com.yuoyama12.decidepickingorderapp.preference.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.yuoyama12.decidepickingorderapp.dialogs.GROUP_SORTING_PREF_KEY
import com.yuoyama12.decidepickingorderapp.dialogs.MEMBER_SORTING_PREF_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SortingPreferencesDataStoreRepository @Inject constructor(
    private val sortingPreferencesDataStore: DataStore<Preferences>,
) {
    suspend fun setGroupSortingId(id: Int) {
        sortingPreferencesDataStore.edit { preferences ->
            preferences[intPreferencesKey(GROUP_SORTING_PREF_KEY)] = id
        }
    }

    suspend fun setMemberSortingId(id: Int) {
        sortingPreferencesDataStore.edit { preferences ->
            preferences[intPreferencesKey(MEMBER_SORTING_PREF_KEY)] = id
        }
    }

    fun getGroupSortingId(): Flow<Int> =
        sortingPreferencesDataStore.data.map { preferences ->
            preferences[intPreferencesKey(GROUP_SORTING_PREF_KEY)] ?: 0
        }

    fun getMemberSortingId(): Flow<Int> =
        sortingPreferencesDataStore.data.map { preferences ->
            preferences[intPreferencesKey(MEMBER_SORTING_PREF_KEY)] ?: 0
        }
}