package xyz.orangej.acmsigninsystemandroid.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import xyz.orangej.acmsigninsystemandroid.R
import xyz.orangej.acmsigninsystemandroid.SystemApplication
import xyz.orangej.acmsigninsystemandroid.api.SERVER_ADDRESS
import xyz.orangej.acmsigninsystemandroid.ui.login.LoginActivity

/**
 * 管理设置Preference的Fragment。
 */
class SettingsFragment : PreferenceFragmentCompat() {

    companion object {

        const val WEB_KEY = "web"

        const val REFRESH_KEY = "refresh"

        const val LOGOUT_KEY = "logout"

        const val INFO_KEY = "info"
    }

    private lateinit var systemApplication: SystemApplication

    private var jumpToWebPreference: Preference? = null
    private var refreshPreference: Preference? = null
    private var logoutPreference: Preference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        this.systemApplication = requireContext().applicationContext as SystemApplication

        jumpToWebPreference = findPreference(WEB_KEY)
        jumpToWebPreference?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(SERVER_ADDRESS)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            true
        }

        refreshPreference = findPreference(REFRESH_KEY)
        refreshPreference?.setOnPreferenceClickListener {

            true
        }

        logoutPreference = findPreference(LOGOUT_KEY)
        logoutPreference?.setOnPreferenceClickListener {
            systemApplication.session = ""
            startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            requireActivity().finish()
            true
        }
    }
}