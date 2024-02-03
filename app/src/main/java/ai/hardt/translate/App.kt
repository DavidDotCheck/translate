package ai.hardt.translate

import ai.hardt.translate.data.DataStoreManager
import ai.hardt.translate.data.PreferencesKey
import ai.hardt.translate.data.getObjectPreference
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialization code here (if needed)
    }
    @Inject
    lateinit var dataStoreManager: DataStoreManager

    suspend fun test() {
        dataStoreManager.getObjectPreference(PreferencesKey.OnboardingStepKey)
    }
}