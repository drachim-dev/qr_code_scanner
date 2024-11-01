package dr.achim.code_scanner.data.source

interface SettingsSource {

    fun getHasSeenOnboarding(): Boolean
    fun setHasSeenOnboarding(value: Boolean)

    fun getAutoStartCamera(): Boolean
    fun setAutoStartCamera(value: Boolean)

    fun getShowSupportHint(): Boolean
    fun setShowSupportHint(value: Boolean)
}