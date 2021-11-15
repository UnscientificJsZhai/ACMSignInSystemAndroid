package xyz.orangej.acmsigninsystemandroid.ui.main.fragments.user

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import xyz.orangej.acmsigninsystemandroid.R
import xyz.orangej.acmsigninsystemandroid.SystemApplication
import xyz.orangej.acmsigninsystemandroid.ui.login.LoginActivity
import xyz.orangej.acmsigninsystemandroid.ui.main.MainActivityViewModel
import xyz.orangej.acmsigninsystemandroid.ui.settings.SettingsActivity

/**
 * 显示用户信息的Fragment。
 */
class UserFragment : Fragment() {

    private lateinit var systemApplication: SystemApplication

    private val viewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.systemApplication = requireContext().applicationContext as SystemApplication
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

        viewModel.currentUser.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            } else {
                this.setContent {
                    UserPage(data = it, onLogout = this@UserFragment::onLogoutButtonClick)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_information_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.settings) {
            startActivity(Intent(requireContext(), SettingsActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 当点击登出按钮时执行的操作。
     */
    private fun onLogoutButtonClick() {
        val session = systemApplication.session
        viewModel.viewModelScope.launch {
            viewModel.logout(requireContext(), session)
        }
        viewModel.currentUser.removeObservers(viewLifecycleOwner)
        systemApplication.session = ""
        startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }
}