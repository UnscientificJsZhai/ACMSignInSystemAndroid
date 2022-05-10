package xyz.orangej.acmsigninsystemandroid.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import androidx.preference.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import xyz.orangej.acmsigninsystemandroid.R
import xyz.orangej.acmsigninsystemandroid.SystemApplication
import xyz.orangej.acmsigninsystemandroid.di.RetrofitApiModule.getServerRoot
import xyz.orangej.acmsigninsystemandroid.ui.ProgressDialog
import xyz.orangej.acmsigninsystemandroid.ui.login.LoginActivity
import kotlin.concurrent.thread

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

    private val viewModel by activityViewModels<SettingsActivityViewModel>()

    private var jumpToWebPreference: Preference? = null
    private var refreshPreference: Preference? = null
    private var logoutPreference: Preference? = null
    private var serverPreference: EditTextPreference? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        this.systemApplication = requireContext().applicationContext as SystemApplication

        if (systemApplication.session.isEmpty()) {
            val accountPreferences: PreferenceCategory? = findPreference("account")
            accountPreferences?.isEnabled = false
        }

        // 在网页中打开的选项
        jumpToWebPreference = findPreference(WEB_KEY)
        jumpToWebPreference?.setOnPreferenceClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(getServerRoot(requireContext()))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            true
        }

        // 重新刷新数据的选项
        refreshPreference = findPreference(REFRESH_KEY)
        refreshPreference?.setOnPreferenceClickListener {
            viewModel.viewModelScope.launch {
                val dialog = ProgressDialog(requireActivity())
                dialog.show()
                val list = viewModel.getTrainHistory(systemApplication.session)
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

        // 登出的选项
        logoutPreference = findPreference(LOGOUT_KEY)
        logoutPreference?.setOnPreferenceClickListener {
            val session = systemApplication.session
            viewModel.viewModelScope.launch {
                viewModel.logout(session)
            }
            systemApplication.session = ""
            startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })
            requireActivity().finish()
            true
        }

        // 手动输入服务器的选项
        serverPreference = findPreference(SERVER_KEY)
        serverPreference?.setOnPreferenceChangeListener { _, newValue ->
            if (Patterns.WEB_URL.matcher(newValue.toString()).matches()) {
                val oldValue = getServerRoot(requireContext())
                thread {
                    runBlocking(Dispatchers.Main) {
                        onUpdateServerConfig(oldValue, newValue.toString())
                    }
                }
                true
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.settings_server_illegal,
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
        }
    }

    /**
     * 更新服务器设置前调用。会清理数据
     *
     * @return 验证要更改的服务器是否是注册服务器。是则返回true，否则返回false。
     */
    private suspend fun onUpdateServerConfig(oldValue: String, newValue: String) {
        val dialog = ProgressDialog(requireActivity()).show()
        if (!viewModel.checkApi(newValue)) {
            Toast.makeText(
                requireContext(),
                R.string.settings_server_changeFail,
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            sharedPreferences.edit().putString("server", oldValue).apply()
        } else {
            // 清理之前的登录状况
            systemApplication.session = ""

            withContext(Dispatchers.IO) {
                val dao = systemApplication.getDatabase().userDao()
                dao.deleteAllRecords()
            }

            findPreference<PreferenceCategory>("account")?.isEnabled = false

            Toast.makeText(
                requireContext(),
                R.string.settings_server_changeSuccess,
                Toast.LENGTH_SHORT
            ).show()
            dialog.dismiss()
        }
    }
}