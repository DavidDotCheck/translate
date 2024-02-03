package ai.hardt.translate.data

import ai.hardt.translate.data.model.OnboardingStep
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.serializersModule
import kotlinx.serialization.serializer
import java.io.IOException
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val dataStore: DataStore<Preferences>) {

    // Get Abstractions:
    suspend fun getBooleanPreference(key: String) =
        getPreference(booleanPreferencesKey(key))

    suspend fun getStringPreference(key: String) =
        getPreference(stringPreferencesKey(key))

    // Set Abstractions:
    suspend fun setBooleanPreference(key: String, value: Boolean) =
        setPreference(booleanPreferencesKey(key), value)

    suspend fun setStringPreference(key: String, value: String) =
        setPreference(stringPreferencesKey(key), value)

    // Base Implementations:
    private suspend fun <T> getPreference(key: Preferences.Key<T>): T? =
        dataStore.data.catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[key]
        }.firstOrNull()

    private suspend fun <T> setPreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { settings ->
            settings[key] = value
        }
    }
}

const val OBJECT_PREFERENCE_KEY_PREFIX = "bound_obj_pref::"

sealed class PreferencesKey<T>(val key: String) {
    data object OnboardingStepKey : PreferencesKey<OnboardingStep>("onboarding_step")
}

suspend inline fun <reified T> DataStoreManager.setObjectPreference(key: PreferencesKey<T>, value: T) {
    val serialized = Json.encodeToString(serializersModule.serializer<T>(), value)
    this.setStringPreference(OBJECT_PREFERENCE_KEY_PREFIX + key, serialized)
}

suspend inline fun <reified T> DataStoreManager.getObjectPreference(key: PreferencesKey<T>): T? {
    val data = this.getStringPreference(OBJECT_PREFERENCE_KEY_PREFIX + key)
    return if (data != null) Json.decodeFromString(
        serializersModule.serializer<T>(), data
    ) else null
}