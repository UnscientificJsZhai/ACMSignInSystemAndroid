package xyz.orangej.acmsigninsystemandroid.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import xyz.orangej.acmsigninsystemandroid.R


class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}