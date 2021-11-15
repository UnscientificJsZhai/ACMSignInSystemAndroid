package xyz.orangej.acmsigninsystemandroid.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.orangej.acmsigninsystemandroid.R
import xyz.orangej.acmsigninsystemandroid.SystemApplication
import xyz.orangej.acmsigninsystemandroid.api.getServerRoot
import xyz.orangej.acmsigninsystemandroid.ui.ProgressDialog
import xyz.orangej.acmsigninsystemandroid.ui.login.LoginActivity

/**
 * 管理设置Preference的Fragment。
 */
class SettingsFragment : PreferenceFragmentCompat() {

    companion object {

        const val WEB_KEY = "web"

        const val REFRESH_KEY = "refresh"

        const val LOGOUT_KEY = "logout"

        const val SERVER_KEY = "server"
    }

    private lateinit var systemApplication: SystemApplication

    private val viewModel by viewModels<SettingsActivityViewModel>()

    private var jumpToWebPreference: Preference? = null
    private var refreshPreference: Preference? = null
    private var logoutPreference: Preference? = null
    private var serverPreference: EditTextPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        this.systemApplication = requireContext().applicationContext as SystemApplication

        jumpToWebPreference = findPreference(WEB_KEY)
        jumpToWebPreference?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(requireContext().getServerRoot())
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            true
        }

        refreshPreference = findPreference(REFRESH_KEY)
        refreshPreference?.setOnPreferenceClickListener {
            viewModel.viewModelScope.launch {
                val dialog = ProgressDialog(requireActivity())
                dialog.show()
                val list = viewModel.getTrainHistory(requireContext(), systemApplication.session)
                withContext(Dispatchers.IO) {
                    if (list.isNotEmpty()) {
                        val dao = systemApplication.getDatabase().userDao()
                        dao.deleteAllRecords()
                        for (item in list) {
                            dao.addRecord(item)
                        }
                    }
                    dialog.postDismiss()
                }
            }
            true
        }

        logoutPreference = findPreference(LOGOUT_KEY)
        logoutPreference?.setOnPreferenceClickListener {
            val session = systemApplication.session
            viewModel.viewModelScope.launch {
                viewModel.logout(requireContext(), session)
            }
            systemApplication.session = ""
            startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            requireActivity().finish()
            true
        }

        serverPreference = findPreference(SERVER_KEY)
        serverPreference?.setOnPreferenceChangeListener { _, newValue ->
            if (Patterns.WEB_URL.matcher(newValue.toString()).matches()) {
                true
            } else {
                Toast.makeText(requireContext(), "不合法", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }
}