package xyz.orangej.acmsigninsystemandroid.ui.main.fragments.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import xyz.orangej.acmsigninsystemandroid.SystemApplication
import xyz.orangej.acmsigninsystemandroid.ui.login.LoginActivity
import xyz.orangej.acmsigninsystemandroid.ui.main.MainActivityViewModel

class UserFragment : Fragment() {

    private val viewModel by activityViewModels<MainActivityViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        val systemApplication = requireContext().applicationContext as SystemApplication

        viewModel.currentUser.observe(viewLifecycleOwner) {
            this.setContent {
                InformationPage(data = it) {
                    systemApplication.session = ""
                    startActivity(Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }
}