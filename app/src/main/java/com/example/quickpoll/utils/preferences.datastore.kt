package com.example.quickpoll.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

object PreferencesDataStoreHelper {
    private const val PREFS_NAME = "quick_poll_datastore"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    suspend fun <T> storeData(
        context: Context,
        key: Preferences.Key<T>,
        data: T
    ) {
        context.dataStore.edit {
            it[key] = data
        }
    }

    fun <T> retrieveData(
        context: Context,
        key: Preferences.Key<T>
    ): Flow<T?> = context.dataStore.data.catch { exception ->
        when (exception) {
            is IOException -> emit(emptyPreferences())
            else -> throw exception
        }
    }.map {
        it[key]
    }

    suspend fun removeStringValueWithSpecificKey(
        key: Preferences.Key<String>,
        context: Context
    ) {
        context.dataStore.edit { it.remove(key) }
    }

    suspend fun removeAllValues(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}

object PreferencesDataStoreKey {
    val AUTH_TOKEN = stringPreferencesKey("AUTH_TOKEN")
}
