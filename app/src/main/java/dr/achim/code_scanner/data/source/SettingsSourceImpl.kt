package dr.achim.code_scanner.data.source

import android.content.SharedPreferences
import dr.achim.code_scanner.common.PrefKeys
import javax.inject.Inject

class SettingsSourceImpl @Inject constructor(private val prefs: SharedPreferences) :
    SettingsSource {

    override fun getHasSeenOnboarding(): Boolean {
        return prefs.getBoolean(PrefKeys.HasSeenOnboarding, false)
    }

    override fun setHasSeenOnboarding(value: Boolean) {
        prefs.edit()
            .putBoolean(PrefKeys.HasSeenOnboarding, value)
            .apply()
    }

    override fun getAutoStartCamera(): Boolean {
        return prefs.getBoolean(PrefKeys.AutoStartCamera, false)
    }

    override fun setAutoStartCamera(value: Boolean) {
        prefs.edit()
            .putBoolean(PrefKeys.AutoStartCamera, value)
            .apply()
    }

    override fun getShowSupportHint(): Boolean {
        return prefs.getBoolean(PrefKeys.ShowSupportHint, true)
    }

    override fun setShowSupportHint(value: Boolean) {
        prefs.edit()
            .putBoolean(PrefKeys.ShowSupportHint, value)
            .apply()
    }
}